package com.android.helper

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Start the background service
        startService(Intent(this, VendingHiderService::class.java))
        Toast.makeText(this, "Vending Hider Service Started", Toast.LENGTH_SHORT).show()
        // Finish activity as it's only used to start the service
        finish()
    }
}