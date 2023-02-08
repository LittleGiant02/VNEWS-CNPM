package com.cnpm.vnews.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cnpm.vnews.R
import com.cnpm.vnews.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        actionGoToAnotherApp()
    }

    private fun actionGoToAnotherApp() {
        binding.imageButtonFacebook.setOnClickListener {
            handlerGoToAnotherApp("https://www.facebook.com/")
        }
        binding.imageButtonYouTube.setOnClickListener {
            handlerGoToAnotherApp("https://www.youtube.com/")
        }
        binding.imageButtonTikTok.setOnClickListener {
            handlerGoToAnotherApp("https://www.tiktok.com/")
        }
        binding.imageButtonPhone.setOnClickListener {
            val phone = "0123456789"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        binding.imageButtonLogin.setOnClickListener {
//            FragmentSetting.openWith(supportFragmentManager)
        }
        binding.imageButtonSearch.setOnClickListener {
//            FragmentSearch.openWith(supportFragmentManager)
        }
    }

    private fun handlerGoToAnotherApp(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

}