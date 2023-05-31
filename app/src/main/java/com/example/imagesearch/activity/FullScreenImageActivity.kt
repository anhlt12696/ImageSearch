package com.example.imagesearch.activity

import android.Manifest
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.DownloadManager
import com.example.imagesearch.Interface.AdapterClick
import com.example.imagesearch.Model.Color
import com.example.imagesearch.Model.Hits
import com.example.imagesearch.Model.Response
import com.example.imagesearch.OnDownloadProgressListener
import com.example.imagesearch.adapter.ImageAdapter
import com.example.imagesearch.apiClient.RetrofitClient
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.ActivityFullScreenImageBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.*

class FullScreenImageActivity : BaseActivity(), AdapterClick {
    private var binding: ActivityFullScreenImageBinding? = null
    private var imageList: List<Hits>? = null
    private val position = 0
    private val context: Context? = null
    private var urlImg: String? = null
    private var query: String? = null
    private var intent1: Intent?= null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(
            layoutInflater
        )
        setContentView(binding!!.root)
        val intent = intent
        if (intent != null) {
            urlImg = intent.getStringExtra("position")
            query = intent.getStringExtra("query")
        }
        binding!!.rvImageFullScreen.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(this, 3)
        binding!!.rvImageFullScreen.layoutManager = gridLayoutManager

        Picasso.get()
            .load(urlImg)
            .into(binding!!.imgDetail)

        intent1 = Intent(this, ZoomImageActivity::class.java)
        intent1!!.putExtra("url_image", urlImg)
        binding!!.imgDetail.setOnClickListener { startActivity(intent1) }

        thumbnail

        binding!!.floatingSearchView.setSearchText(query)
        binding!!.homeButton.setOnClickListener {
            val intent2 = Intent(this, MainActivity::class.java)
            startActivity(intent2)
        }
        //Share
        binding!!.btnShare.setOnClickListener {
//            urlImg?.let { it1 -> shareImage(it1) }
            ShareImage()
        }

        //save image
        binding!!.btnSave.setOnClickListener { //                if (checkPermissionForWriteExtertalStorage()) {
//                    downLoad();
//                } else {
//                    requestPermissionForWriteExtertalStorage();
//                }
            DownloadImage(urlImg)
            Common.setPerMission(this,true)
        }
        binding!!.backArrow.setOnClickListener {
            onBackPressed()
        }

        //set wallpaper
        binding!!.btnSetWall.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                val result: Bitmap = try {
                    Picasso.get()
                        .load(urlImg)
                        .get()
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
                val bitmap = Bitmap.createScaledBitmap(result,400,600,true)
                val wallpaperManager = WallpaperManager.getInstance(baseContext)
                try {

                    wallpaperManager.setBitmap(result)

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                withContext(Dispatchers.Main){
                    val wallpaperManager = WallpaperManager.getInstance(baseContext)
                    try {
                        wallpaperManager.setBitmap(result)
                        Toast.makeText(applicationContext, "Set wallpaper successfully", Toast.LENGTH_SHORT) .show()
                    } catch (ex: IOException) {
                        Toast.makeText(applicationContext, "Set wallpaper error", Toast.LENGTH_SHORT) .show()
                        ex.printStackTrace()
                    }
                }
            }

        }
    }

    fun Context.shareImage(path: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "*/*"
        val file = File(Uri.parse(path).path)
        val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
//    share.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        share.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(share, "Share Image"))
    }
    fun ShareImage(){
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, urlImg)
        startActivity(Intent.createChooser(i, "Share Image"))
    }


    private val thumbnail: Unit
        private get() {
            val responseCall = RetrofitClient.getInstance().myApi.getImages(
                1,
                50,
                query,
                Common.getOrientation(this),
                Common.getCategory(this),
                Common.getMinWidth(this),
                Common.getMinHeight(this),
                Common.getColors(this),
                Common.getOrder(this)
            )
            responseCall.enqueue(object : Callback<Response?> {
                override fun onResponse(
                    call: Call<Response?>,
                    response: retrofit2.Response<Response?>
                ) {
                    if (response.isSuccessful) {
                        val myResponse = response.body()
                        if (myResponse != null) {
                            imageList = myResponse.hits
                            if (imageList != null && imageList!!.size > 0) {
                                val adapter =
                                    ImageAdapter(this@FullScreenImageActivity, imageList, this@FullScreenImageActivity, 0)
                                binding!!.rvImageFullScreen.adapter = adapter
                                binding!!.rvImageFullScreen.scrollToPosition(position)
                            }
                            //   binding!!.pbImageFullScreen.visibility = View.GONE
                        }
                    }
                }

                override fun onFailure(call: Call<Response?>, t: Throwable) {
                    Toast.makeText(
                        this@FullScreenImageActivity,
                        t.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                    //    binding!!.pbImageFullScreen.visibility = View.GONE
                }
            })
        }

    fun checkPermissionForWriteExtertalStorage(): Boolean {
        val result = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionForWriteExtertalStorage() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            WRITE_STORAGE_PERMISSION_REQUEST_CODE
        )
    }

    fun DownloadImage(ImageUrl: String?) {
        if (ContextCompat.checkSelfPermission(
                this@FullScreenImageActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this@FullScreenImageActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@FullScreenImageActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
            ActivityCompat.requestPermissions(
                this@FullScreenImageActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                123
            )
        } else {
            lifecycleScope.launch(Dispatchers.IO) {
                var mImage: Bitmap?= null
                urlImg?.let { mImage= mLoad(it) }
                mImage?.let {
                    mSaveMediaToStorage(it)
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(this@FullScreenImageActivity, "image saved", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    private fun mSaveMediaToStorage(bitmap: Bitmap?) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        val imagesDir = File("$filesDir/ImageSearch")
        val image = File(imagesDir, filename)
        fos = FileOutputStream(image)
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
    }

    companion object {
        private const val WRITE_STORAGE_PERMISSION_REQUEST_CODE = 41
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

    override fun itemClick(hits: Hits?, position: Int) {
        urlImg = hits!!.largeImageURL
        Picasso.get()
            .load(urlImg)
            .into(binding!!.imgDetail)
        intent1 = Intent(this, ZoomImageActivity::class.java)
        intent1!!.putExtra("url_image", urlImg)
    }

    override fun itemImageClick(string: String?, position: Int) {
        TODO("Not yet implemented")
    }

    override fun itemColorClick(color: Color?, position: Int) {
        TODO("Not yet implemented")
    }
}