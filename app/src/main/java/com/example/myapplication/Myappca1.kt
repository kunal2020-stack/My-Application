package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class Myappca1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myappca1)
        Handler(Looper.getMainLooper()).postDelayed({
            val i=Intent(this,nextscreen::class.java)
                                                 startActivity(i)
                                                 finish()},5000)

    }
}