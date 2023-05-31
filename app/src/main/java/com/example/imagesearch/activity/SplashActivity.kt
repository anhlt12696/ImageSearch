package com.example.imagesearch.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import com.example.imagesearch.R
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.base.getFirtOpen3
import com.example.imagesearch.databinding.ActivitySplashBinding

class SplashActivity : BaseActivity() {
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        Common.setThemeForActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

       showChooseTheme()

//        Handler().postDelayed({
//            val launchActivity3: Intent = Intent(
//                this@SplashActivity,
//                ThemeScreenActivity::class.java
//            )
//            startActivity(launchActivity3)
//        },5000)
        val Image: String? = getColoredSpanned(resources.getString(R.string.image), "#FFFFFF")
        val Search: String? = getColoredSpanned(resources.getString(R.string.search), "#B0EAFF")
        binding.tvAppName.text = Html.fromHtml(
            "$Image $Search"
        )
    }

    fun getColoredSpanned(text: String, color: String): String? {
        return "<font color=$color>$text</font>"
    }
    private fun showChooseTheme(){

        Handler().postDelayed({
            val launchActivity3: Intent = Intent(
                this@SplashActivity,
                LanguageActivity::class.java
            )
            startActivity(launchActivity3)
        },5000)
    }
//    private fun nextActivity() {
//        if (this.getFirtOpen3(this)) {
//            val intent = Intent(this@SplashActivity, ThemeActivity::class.java)
//            intent.putExtra("start", true)
//            startActivity(intent)
//            finish()
//        } else {
//            val intent = Intent(this@SplashActivity, StartActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }
}