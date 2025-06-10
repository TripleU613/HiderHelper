package com.android.helper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val tag = "VendingHiderBoot"

    override fun onReceive(context: Context, intent: Intent?) {
        if (Intent.ACTION_BOOT_COMPLETED == intent?.action) {
            context.startService(Intent(context, VendingHiderService::class.java))
            Log.d(tag, "Service started on boot")
        }
    }
}