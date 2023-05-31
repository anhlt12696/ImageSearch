package com.example.imagesearch.activity

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import com.arlib.floatingsearchview.FloatingSearchView.OnSearchListener
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.Response
import com.example.imagesearch.R
import com.example.imagesearch.adapter.ImageAdapter
import com.example.imagesearch.apiClient.RetrofitClient
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.base.visible
import com.example.imagesearch.databinding.ActivityMainBinding
import com.example.imagesearch.fragment.SavedFragment
import com.example.imagesearch.fragment.SearchFragment
import com.vapp.admoblibrary.ads.AppOpenManager
import retrofit2.Call
import retrofit2.Callback
import java.io.File

class MainActivity : BaseActivity()  {
    private var binding: ActivityMainBinding? = null
    private var imageList: List<Hits>? = null

    private var fragmentManager: FragmentManager? = null
    private var fingerOrEye = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding!!.tvSearch.isSelected = true
        binding!!.tvSaved.isSelected = true
        fragmentManager = supportFragmentManager
        bottomNavigationClick()
        changeFragment()
        binding!!.tvSearch.setTextColor(getColor(R.color.bottom_checked))
        binding!!.imgSearch.imageTintList = getColorStateList(R.color.bottom_checked)
        binding!!.lineSearch.isVisible = true

        File("$filesDir/ImageSearch").mkdir();
    }


    private fun bottomNavigationClick() {
        binding!!.btnSearch.setOnClickListener { v ->
            fingerOrEye = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                changeFragment()

            }
        }
        binding!!.btnSaved.setOnClickListener { v ->
            //  checkPermission()
            fingerOrEye = false
            changeFragment()
        }
    }

    private fun changeFragment() {
        if (fingerOrEye) {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentMain, SearchFragment())?.commit()

            //search
            binding!!.tvSearch.setTextColor(getColor(R.color.bottom_checked))
            binding!!.imgSearch.imageTintList = getColorStateList(R.color.bottom_checked)
            binding!!.lineSearch.isVisible = true

            //saved
            binding!!.tvSaved.setTextColor(getColor(R.color.bottom_un_checked))
            binding!!.imgSave.imageTintList = getColorStateList(R.color.bottom_un_checked)
            binding!!.lineSave.isVisible = false

        } else {
            fragmentManager?.beginTransaction()?.replace(R.id.fragmentMain, SavedFragment())?.commit()

            //search
            binding!!.tvSearch.setTextColor(getColor(R.color.bottom_un_checked))
            binding!!.imgSearch.imageTintList = getColorStateList(R.color.bottom_un_checked)
            binding!!.lineSearch.isVisible = false

            //saved
            binding!!.tvSaved.setTextColor(getColor(R.color.bottom_checked))
            binding!!.imgSave.imageTintList = getColorStateList(R.color.bottom_checked)
            binding!!.lineSave.isVisible = true


        }
    }
//    override fun itemClick(hits: Hits, position: Int) {
//        val intent = Intent(this@MainActivity, FullScreenImageActivity::class.java)
//        intent.putExtra("position", hits.largeImageURL)
//        startActivity(intent)
//    }

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