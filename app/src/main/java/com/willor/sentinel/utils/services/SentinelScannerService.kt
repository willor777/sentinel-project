package com.willor.sentinel.utils.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.willor.ktstockdata.historical_data.charts.advancedchart.AdvancedStockChart
import com.willor.lib_data.action.Strategies
import com.willor.lib_data.data.local.local_preferences.DatastorePrefsManager
import com.willor.lib_data.data.local.local_preferences.SentinelSettings
import com.willor.lib_data.domain.abstraction.IRepo
import com.willor.lib_data.domain.abstraction.Resource
import com.willor.sentinel.MainActivity
import com.willor.sentinel.R
import com.willor.sentinel_bots.domain.models.TriggerBase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import javax.inject.Inject


@AndroidEntryPoint
class SentinelScannerService: Service() {

    private val locTAG = "SentinelScannerService()"

    @Inject lateinit var prefs: DatastorePrefsManager

    @Inject lateinit var repo: IRepo

    private val myBinder = SentinelScannerBinder()

    private lateinit var scannerCoroutine: Job

    private val notificationText = "Sentinel Scanner Running...Tap to stop."

    private var scannerIsRunning = false


    /*------------------------------------ Methods Start Here ------------------------------------*/


    // TODO Should be able to specify chart frequency here
    /**
     * Accepts a list of tickers. Collects charts for each one.
     */
    private suspend fun collectChartsForScan(tickers: List<String>): List<AdvancedStockChart>{

        val charts = mutableListOf<AdvancedStockChart>()
        for (t: String in tickers){
            repo.getHistoricDataAsAdvancedStockChart(t).collect{
                when (it){
                    is Resource.Success -> {
                        charts.add(it.data!!)
                    }
                    is Resource.Error -> {
                        Log.d("INFO", "$locTAG.collectChartsForScan() Failed to get " +
                                "chart for :$t")
                    }
                    else -> {
                        Log.d("INFO", "$locTAG.collectChartsForScan() $t chart" +
                                "is Loading.")
                    }
                }
            }
            delay(1000)
        }
        return charts
    }


    /**
     * Saves TriggerBase to database using TriggerTableDAO
     */
    private fun saveTrigger(triggerBase: TriggerBase){

        // Build and save the entity using repo
        val entity = repo.buildAndSaveTriggerEntity(
            triggerBase
        )
    }


    // TODO User should be able to choose what algo to run. That will affect this method
    /**
     * Starts the scannerCoroutine worker which periodically runs a scan of the Sentinel Watchlist.
     * If the scannerCoroutine has already been started and is currently active, will not start
     * another.
     */
    private fun startScanner(){
        Log.d("INFO", "$locTAG.startScanner() has been called." +
                " Starting Scanner Coroutine")

        // Check if scanner is already active and initialized, returns if so
        if (this::scannerCoroutine.isInitialized){
            if (scannerCoroutine.isActive){
                Log.d("INFO", "Blocked the starting of new scannerCoroutine. " +
                        "scannerCoroutine is already initialized and active")
                return
            }
        }

        // Start the coroutine
        scannerCoroutine = MainScope().launch(Dispatchers.IO){

            // Checks the scannerIsRunning each time
            while (scannerIsRunning){
                Log.d("INFO", "$locTAG.startScanner() Scanner Coroutine Starting Scan")

                // Re-collect sentinel settings (watchlist, scanInterval, etc)
                val curSentinelSettings = loadSentinelSettings()

                // Get charts and Analyze   /* TODO Add Scan Algo Option Here */
                val charts = collectChartsForScan(curSentinelSettings.currentWatchlist)
                val triggers = Strategies().testStrategy.runAnalysisOnCharts(charts)

                // Loop through triggers. Valid triggers are added to triggersList stateflow
                for (t in triggers.triggers){
                    Log.d("INFO", "Ticker:Trigger:Rating ->" +
                            " ${t.ticker}:${t.triggerValue}:${t.rating}")
                    if (t.triggerValue != 0){
                        saveTrigger(t)
                    }
                }

                // Update lastScan in sentinelSettings
                curSentinelSettings.lastScan = System.currentTimeMillis()
                prefs.updateSentinelSettings(curSentinelSettings)

                // Sleep for scanInterval time
                delay(curSentinelSettings.scanInterval)
            }
        }
    }


    /**
     * Loads the SentinelSettings from datastore. SentinelSettings contains: Watchlist,
     * ScanAlgo, ScanInterval, etc.
     */
    private suspend fun loadSentinelSettings(): SentinelSettings{
        return prefs.getSentinelSettings().first()
    }


    /**
     * Sets up a channel for the notification.
     */
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val descriptionText = notificationText
            val importance = NotificationManager.IMPORTANCE_MAX
            val channel = NotificationChannel(
                SCANNER_NOTIFICATION_CHANNEL_ID,
                SCANNER_NOTIFICATION_CHANNEL_NAME,
                importance
            ).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager = getSystemService(
                Context.NOTIFICATION_SERVICE
            )
            (notificationManager as NotificationManager).createNotificationChannel(channel)
        }
    }


    /**
     * Builds the notification displayed to user when Sentinel Scanner first starts. Does Not
     * display the notification
     */
    private fun buildStartUpNotification(): Notification{

        // Create an intent connected to the MainActivity class (class context that will launch it)
        val notificationIntent = Intent(this, MainActivity::class.java)

        // Make it into a PendingIntent flagged as a Notification Intent
        val stopServicePendingIntent = PendingIntent.getBroadcast(
            this, 5,
            Intent(this, StopScannerReceiver::class.java),
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )


        val note = NotificationCompat.Builder(this, SCANNER_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_radar_logo_24)
            .setContentTitle("Sentinel Background Scanner")
            .setContentText(notificationText)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
            .setContentIntent(stopServicePendingIntent)
            .build()

        return note
    } /* TODO OnClick Action */


    /**
     * Updates the Notification live
     */
    private fun updateNotification(newNotification: Notification){
        // Docs say to use this to update a notification. Just pass it a new note and it will
        // remove the old one and show the new one. But it's not working for some reason?



        // Now that i think of it...Try just showing another notification on the same channel.
        // It will probably just replace it
//        NotificationManagerCompat.notify(SCANNER_NOTIFICATION_CHANNEL_ID)
    } /* TODO */


    /**
     * Checks if scannerCoroutine is active. If so, shuts it down
     */
    private fun stopScannerCoroutine(){
        // Check if scannerCoroutine has been initialized yet
        val coroutineIsInitialized = this::scannerCoroutine.isInitialized

        MainScope().launch(Dispatchers.IO){

            // Check if coroutine is active, if so, shut down
            if (coroutineIsInitialized){
                if (scannerCoroutine.isActive){
                    scannerIsRunning = false
                    scannerCoroutine.cancel()
                }
            }
        }
    }


    private fun shutDownService(){
        stopSelf()
    }


    /*----------------------------------- Overrides Start Here -----------------------------------*/


    override fun onBind(intent: Intent?): IBinder {
        return myBinder
    }


    override fun stopService(name: Intent?): Boolean {
        Log.d("INFO", "$locTAG.stopService() called")
        scannerIsRunning = false
        stopScannerCoroutine()
        return super.stopService(name)
    }


    override fun onDestroy() {
        Log.d("INFO", "$locTAG.onDestroy() called")

        scannerIsRunning = false
        stopScannerCoroutine()
        super.onDestroy()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.d("INFO", "SentinelScannerService() onStartCommand() Called")

        // Set flag showing the service is running and start scanner
        if (!scannerIsRunning){

            // Create channel and notification and startForeground
            createNotificationChannel()
            startForeground(SCANNER_NOTIFICATION_ID_NUMBER, buildStartUpNotification())

            scannerIsRunning = true
            startScanner()
        }

        return START_STICKY
    }


    /*---------------------------------- Other Stuff Start Here ----------------------------------*/


    inner class SentinelScannerBinder: Binder(){

        fun getService(): SentinelScannerService{
            return this@SentinelScannerService
        }

    }


    companion object{
        const val SCANNER_NOTIFICATION_CHANNEL_NAME = "SENTINEL_SCANNER_NOTIFICATION_CHANNEL"
        const val SCANNER_NOTIFICATION_CHANNEL_ID = "SENTINEL_SCANNER_CHANNEL"
        const val SCANNER_NOTIFICATION_ID_NUMBER = 69
    }

}