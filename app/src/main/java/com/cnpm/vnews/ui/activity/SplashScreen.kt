package com.cnpm.vnews.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.cnpm.vnews.R
import java.util.*

const val TIMER_DELAY_MILLISECONDS: Long = 2500

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val intent = Intent(this@SplashScreen, MainActivity::class.java)
        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, TIMER_DELAY_MILLISECONDS)
    }
}