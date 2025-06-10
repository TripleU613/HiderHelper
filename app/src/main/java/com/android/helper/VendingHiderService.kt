package com.android.helper

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import java.io.DataOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask

class VendingHiderService : Service() {
    private val tag = "VendingHiderService"
    private val targetPackage = "com.android.vending"
    private var timer: Timer? = null
    private var executionCount = 0
    private val notificationId = 1

    override fun onCreate() {
        super.onCreate()
        // Create notification channel for foreground service
        val channel = NotificationChannel(
            "VendingHiderChannel",
            "Vending Hider Service",
            NotificationManager.IMPORTANCE_LOW
        )
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
        // Start foreground service
        val notification = Notification.Builder(this, "VendingHiderChannel")
            .setContentTitle("Vending Hider")
            .setContentText("Running in background (Started)")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .build()
        startForeground(notificationId, notification)
        timer = Timer()
        timer?.scheduleAtFixedRate(VendingCheckTask(), 0, 10 * 1000) // Run every 10 seconds
        Log.d(tag, "Service created and timer started")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
        timer = null
        Log.d(tag, "Service destroyed and timer cancelled")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private inner class VendingCheckTask : TimerTask() {
        override fun run() {
            executionCount++
            val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
            Log.d(tag, "Running VendingCheckTask #$executionCount at $timestamp")
            try {
                Log.d(tag, "Checking if package is hidden")
                if (!isPackageHidden()) {
                    Log.d(tag, "Package not hidden, executing hide command")
                    executeRootCommand("su -c pm hide $targetPackage")
                    Log.d(tag, "Hid package: $targetPackage")
                } else {
                    Log.d(tag, "Package already hidden")
                }
            } catch (e: Exception) {
                Log.e(tag, "Error in VendingCheckTask: ${e.message}", e)
            }
        }

        private fun isPackageHidden(): Boolean {
            return try {
                val intent = Intent(Intent.ACTION_MAIN)
                intent.setPackage(targetPackage)
                val resolveInfo = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY)
                val result = resolveInfo == null
                Log.d(tag, "isPackageHidden (PackageManager) result: $result")
                result
            } catch (e: Exception) {
                Log.e(tag, "Error checking hidden status: ${e.message}")
                false
            }
        }

        private fun executeRootCommand(command: String) {
            try {
                val process = Runtime.getRuntime().exec(command)
                val outputStream = DataOutputStream(process.outputStream)
                outputStream.writeBytes("exit\n")
                outputStream.flush()
                val exitValue = process.waitFor()
                outputStream.close()
                process.destroy()
                Log.d(tag, "Root command executed: $command, exit value: $exitValue")
            } catch (e: Exception) {
                Log.e(tag, "Error executing root command: ${e.message}")
            }
        }
    }
}