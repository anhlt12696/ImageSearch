package com.example.imagesearch.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.imagesearch.BuildConfig
import com.example.imagesearch.R
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.ActivitySettingsBinding
import com.vapp.admoblibrary.ads.AppOpenManager
import java.util.concurrent.TimeUnit

class SettingsActivity : BaseActivity() {
    lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Common.setThemeForActivity(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

       // binding.swVibrate.isChecked = Common.getVibrate(this)

        binding.btnBack.setOnClickListener {
            finish()
        }

//
//        binding.lnShare.setOnClickListener(){
//            AppOpenManager.getInstance().disableAppResumeWithActivity(SettingsActivity::class.java)
//            val intent = Intent()
//            intent.action = Intent.ACTION_SEND
//            intent.type = "text/plain"
//            intent.putExtra(
//                Intent.EXTRA_TEXT,
//                getString(R.string.app_name) + " app for Android - https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID
//            )
//            startActivity(Intent.createChooser(intent, "Share to"))
//        }
        binding.lnLanguage.setOnClickListener(){
            startActivity(Intent(this,LanguageActivity::class.java).putExtra("setting",true))
        }
//        binding.tvMoreAPp.clicks().throttleFirst(2, TimeUnit.SECONDS).subscribe {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(SettingsActivity::class.java)
//            val moreAppIntent = Intent(
//                Intent.ACTION_VIEW,
//                Uri.parse("https://play.google.com/store/apps/developer?id=Dktech+app+publishing&hl=en")
//            )
//            startActivity(moreAppIntent)
//        }
//        binding.tvEmail.setOnClickListener() {
//            AppOpenManager.getInstance().disableAppResumeWithActivity(SettingsActivity::class.java)
//            val emailIntent = Intent(Intent.ACTION_SENDTO)
//            emailIntent.data = Uri.parse("mailto:")
//            emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("duonghuynh0240@gmail.com"))
//            emailIntent.putExtra(
//                Intent.EXTRA_SUBJECT,
//                "Feedback App " + getString(R.string.app_name)
//            )
//            startActivity(Intent.createChooser(emailIntent, "Send email..."))
//        }
//        binding.tvPrivacyPolicy.setOnClickListener() {
//            startActivity(Intent(this,PrivacyPolicyActivity::class.java))
//        }
    }

    override fun onResume() {
        super.onResume()
        AppOpenManager.getInstance().enableAppResumeWithActivity(SettingsActivity::class.java)
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