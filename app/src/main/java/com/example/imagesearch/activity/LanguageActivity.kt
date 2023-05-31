package com.example.imagesearch.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.imagesearch.Model.Language
import com.example.imagesearch.R
import com.example.imagesearch.adapter.AdapterLanguage
import com.example.imagesearch.base.*
import com.example.imagesearch.databinding.ActivityLanguageBinding

import java.util.*

class LanguageActivity : BaseActivity() {

    lateinit var binding: ActivityLanguageBinding
    var adapterLanguage: AdapterLanguage? = null
    var listTitle: ArrayList<Language> = ArrayList()
    var listImage: ArrayList<Int> = ArrayList()
    var language: String? = null
    var language_name: String = ""
    var flag = 0
    var pos = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        Common.setThemeForActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivityLanguageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //test ads
//        AdsManager.loadAdNative(this,AdsManager.nativeIntro)
//        AdsManager.loadInter(this,AdsManager.interIntro)
//        if(intent.getBooleanExtra("theme",false)) {
//            AdsManager.showAdInter(
//                this,
//                AdsManager.interTheme,
//                "inter_language",
//                object : AdsManager.AdListenerNew {
//                    override fun onAdClosed() {}
//
//                    override fun onFailed() {}
//
//                })
//        }
//        AdsManager.showAdNative(this,AdsManager.nativeLanguage,binding.flNative)


        listTitle.add(Language("English", "en"))
        listTitle.add(Language("Hindi", "hi"))
        listTitle.add(Language("Spanish", "es"))
        listTitle.add(Language("French", "fr"))
        listTitle.add(Language("Arabic", "ar"))
        listTitle.add(Language("Bengali", "bn"))
        listTitle.add(Language("Russian", "ru"))
        listTitle.add(Language("Portuguese", "pt"))
        listTitle.add(Language("Indonesian", "in"))
        listTitle.add(Language("German", "de"))
        listTitle.add(Language("Italian", "it"))
        listTitle.add(Language("Korean", "ko"))
        listImage.add(R.drawable.ic_english)
        listImage.add(R.drawable.ic_hindi)
        listImage.add(R.drawable.ic_spanish)
        listImage.add(R.drawable.ic_french)
        listImage.add(R.drawable.ic_arabic)
        listImage.add(R.drawable.ic_bengali)
        listImage.add(R.drawable.ic_russian)
        listImage.add(R.drawable.ic_portuguese)
        listImage.add(R.drawable.ic_indonesian)
        listImage.add(R.drawable.ic_german)
        listImage.add(R.drawable.ic_italian)
        listImage.add(R.drawable.ic_korean)

        language = this.getPreLanguage(this)
        language_name = getPreLanguageName(this).toString()
        flag = this.getPreLanguageflag(this)
        pos = this.getPosition(this)

        adapterLanguage = AdapterLanguage(this, object : AdapterLanguage.onClickLanguage {
            override fun onClicked(position: Int, name: String, img: Int, language_name: String) {
                adapterLanguage?.updateData(position)
                pos = position
                language = name
                flag = img
                this@LanguageActivity.language_name = language_name
            }
        })
//
        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.rcvLanguage.layoutManager = LinearLayoutManager(this)
        adapterLanguage?.setData(listTitle, listImage)
        binding.rcvLanguage.adapter = adapterLanguage
        adapterLanguage?.updateData(pos)

        binding.done.setOnClickListener {
            setLocale(this@LanguageActivity, language)
            setPreLanguageflag(this@LanguageActivity, flag)
            setPosition(this@LanguageActivity, pos)
            //ktra
            setFirtOpen(this, false)
            setPreLanguageName(this, language_name)
//            if (intent.getBooleanExtra("setting", false)) {
//                val i = Intent(this@LanguageActivity, StartActivity::class.java)
//                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(i)
//            } else {
//                val launchIntro: Intent = Intent(this@LanguageActivity, IntroActivity::class.java)
//                startActivity(launchIntro)
//            }
            if (intent.getBooleanExtra("setting", false)) {
                val i = Intent(this@LanguageActivity, MainActivity::class.java)
                binding.backArrow.visibility = View.VISIBLE
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(i)
            } else {
                val launchIntro: Intent = Intent(this@LanguageActivity, IntroActivity::class.java)
                startActivity(launchIntro)
            }
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

    override fun onResume() {
        super.onResume()
        if (intent.getBooleanExtra("setting", false)) {
            binding.backArrow.visibility = View.VISIBLE

        }
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}