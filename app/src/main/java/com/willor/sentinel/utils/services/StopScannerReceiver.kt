package com.willor.sentinel.utils.services

import android.content.*
import android.os.IBinder
import android.util.Log

class StopScannerReceiver: BroadcastReceiver() {

    val locTAG = "StopScannerReceiver()"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("INFO", "$locTAG.onReceive() Called")
        val serv = Intent(context, SentinelScannerService::class.java)
        context!!.stopService(serv)
    }
}