package com.example.imagesearch.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.imagesearch.R
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.ActivityThemeBinding


class ThemeActivity : AppCompatActivity() {

    lateinit var binding : ActivityThemeBinding
    private var themeType : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityThemeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(Common.getTheme(this) == 1){
            themeType = 1
        }else{
            themeType = 2
        }
        setView()

        onClickChangeTheme()
        binding.btnNext.setOnClickListener {

                    if(themeType == 1){
                        Common.setTheme(this@ThemeActivity,1)
                    }else{
                        Common.setTheme(this@ThemeActivity,2)
                    }
                    val intent = Intent(this@ThemeActivity,LanguageActivity::class.java)
                    intent.putExtra("splash",true)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)



        }
    }

    private fun setView() {
        if(themeType == 1){
            binding.imgTheme.setImageResource(R.drawable.ic_light)
            binding.tvTheme1.setTextColor(resources.getColor(R.color.black))
            binding.tvTheme2.setTextColor(resources.getColor(R.color.second_text_color_light))
            binding.lnSwitch.setBackgroundResource(R.drawable.sw_theme_light)
            binding.parent.setBackgroundColor(resources.getColor(R.color.white))
            binding.tvLight.setTextColor(resources.getColor(R.color.light_switch_theme_tv_light))
            binding.tvDark.setTextColor(resources.getColor(R.color.light_switch_theme_tv_dark))
        }else{
            binding.imgTheme.setImageResource(R.drawable.ic_dark)
            binding.tvTheme1.setTextColor(resources.getColor(R.color.white))
            binding.tvTheme2.setTextColor(resources.getColor(R.color.second_text_color_dark))
            binding.lnSwitch.setBackgroundResource(R.drawable.sw_theme_dark)
            binding.parent.setBackgroundColor(resources.getColor(R.color.black))
            binding.tvLight.setTextColor(resources.getColor(R.color.dark_switch_theme_tv_light))
            binding.tvDark.setTextColor(resources.getColor(R.color.dark_switch_theme_tv_dark))
        }
    }
    private fun onClickChangeTheme() {
        binding.light.setOnClickListener {
            themeType = 1
            setView()
        }
        binding.dark.setOnClickListener {
            themeType = 2
            setView()
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
}