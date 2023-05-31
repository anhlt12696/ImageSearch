package com.example.imagesearch.activity


import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.example.imagesearch.R
import com.example.imagesearch.adapter.IntroAdapter
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    private lateinit var binding: ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Common.setThemeForActivity(this)
        super.onCreate(savedInstanceState)

        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val pagerAdapter = IntroAdapter(supportFragmentManager)
        binding.viewPager.adapter = pagerAdapter
        binding.dotsIndicator.setViewPager(binding.viewPager)
        binding.viewPager.addOnPageChangeListener(viewPagerPageChangeListener)



        binding.btnNext.setOnClickListener {
            val currentPos = binding.viewPager.currentItem
            if (currentPos != 2) {
                binding.viewPager.currentItem = currentPos + 1
            } else {
                val intent = Intent(this@IntroActivity, PermissionActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)

            }
        }

    }

    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener = object :
        ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {

        }

        override fun onPageScrollStateChanged(state: Int) {
        }


    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    }

    override fun onBackPressed() {
        val currentPos = binding.viewPager.currentItem
        if (currentPos == 0){
            finishAffinity()
        }

    }
}