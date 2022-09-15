package com.willor.sentinel.utils.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class StopScannerReceiver: BroadcastReceiver() {

    private val locTAG = "StopScannerReceiver()"

    override fun onReceive(context: Context?, intent: Intent?) {

        // Shut scanner off
        if (SentinelScannerService.isRunning.value){
            val serv = Intent(context, SentinelScannerService::class.java)
            context!!.stopService(serv)
            Log.d("INFO", "$locTAG.onReceive() Called... Scanner has been turned off")
        }
        else{
            Log.d("INFO", "$locTAG.onReceive() Called... Scanner is already off")
        }

    }
}