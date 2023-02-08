package com.cnpm.vnews.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.cnpm.vnews.R
import com.cnpm.vnews.databinding.ActivityMainBinding
import com.cnpm.vnews.ui.adapter.viewpager2.AdapterViewPager2Main

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        binding.bottomNav.itemIconTintList = null
        val adapterViewPager2Main = AdapterViewPager2Main(this)
        binding.viewPager2.adapter = adapterViewPager2Main
        binding.viewPager2.offscreenPageLimit = 5
        binding.bottomNav.setOnNavigationItemSelectedListener {
            supportFragmentManager.popBackStack()
            when (it.itemId) {
                R.id.navHome -> {
                    binding.viewPager2.setCurrentItem(0, true)
                }
                R.id.navVideo -> {
                    binding.viewPager2.setCurrentItem(1, true)
                }
                R.id.navNews -> {
                    binding.viewPager2.setCurrentItem(2, true)
                }
                R.id.navLive -> {
                    binding.viewPager2.setCurrentItem(3, true)
                }
                R.id.navCategory -> {
                    binding.viewPager2.setCurrentItem(4, true)
                }
            }
            true
        }
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when (position) {
                    0 -> binding.bottomNav.menu.findItem(R.id.navHome).isChecked = true
                    1 -> binding.bottomNav.menu.findItem(R.id.navVideo).isChecked = true
                    2 -> binding.bottomNav.menu.findItem(R.id.navNews).isChecked = true
                    3 -> binding.bottomNav.menu.findItem(R.id.navLive).isChecked = true
                    4 -> binding.bottomNav.menu.findItem(R.id.navCategory).isChecked = true
                }
            }
        })
        binding.viewPager2.isUserInputEnabled = false
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
            val phone = "0337322765"
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