package com.example.imagesearch.activity


import android.Manifest
import android.R
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
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
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.databinding.ActivityZoomImageBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class ZoomImageActivity : BaseActivity() {
    lateinit var binding: ActivityZoomImageBinding
    var urlImg: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityZoomImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backArrow.setOnClickListener {
            onBackPressed()
        }
        if (intent != null) {
            urlImg = intent.getStringExtra("url_image")
        }

        if (intent.getBooleanExtra("saved",false)){
            binding.btnSave.isVisible = false
            val file = File(Uri.parse(urlImg).path)
            val uri = FileProvider.getUriForFile(this, "$packageName.provider", file)
            //  urlImg = uri.path
            Log.d("===path",uri.path.toString())
            Glide.with(this).load(uri).into(binding.imgZoom)

//            binding.btnSetWall.setOnClickListener {
//                CoroutineScope(Dispatchers.IO).launch{
//                    val result: BitmapDrawable = binding.imgZoom.drawable as BitmapDrawable
//                    val bitmap = Bitmap.createScaledBitmap(result.bitmap,400,600,true)
//                    val wallpaperManager = WallpaperManager.getInstance(baseContext)
//                    try {
//                        wallpaperManager.setBitmap(bitmap)
//                    } catch (ex: IOException) {
//                        ex.printStackTrace()
//                    }
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(applicationContext, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }
        }else{
            Glide.with(this).load(urlImg).into(binding.imgZoom)

//            binding.btnSetWall.setOnClickListener {
//                CoroutineScope(Dispatchers.IO).launch{
//                    val result: BitmapDrawable = binding.imgZoom.drawable as BitmapDrawable
//                    val bitmap = Bitmap.createScaledBitmap(result.bitmap,400,600,true)
//                    val wallpaperManager = WallpaperManager.getInstance(baseContext)
//                    try {
//                        wallpaperManager.setBitmap(bitmap)
//                    } catch (ex: IOException) {
//                        ex.printStackTrace()
//                    }
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(applicationContext, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//            }

        }
        binding.btnSetWall.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch{
                val result: BitmapDrawable = binding.imgZoom.drawable as BitmapDrawable
                val bitmap = Bitmap.createScaledBitmap(result.bitmap,400,600,true)
                val wallpaperManager = WallpaperManager.getInstance(baseContext)
                try {
                    wallpaperManager.setBitmap(bitmap)
                    //  wallpaperManager.setBitmap(bitmap,null,true,WallpaperManager.FLAG_LOCK)

                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
                withContext(Dispatchers.Main){
                    Toast.makeText(applicationContext, "Set wallpaper successfully", Toast.LENGTH_SHORT).show();
                }

            }

        }
        binding.btnShare.setOnClickListener {
            ShareImage()
        }

        binding.btnSave.setOnClickListener {
            DownloadImage(urlImg)
        }
    }

    fun ShareImage(){
        val i = Intent(Intent.ACTION_SEND)
        i.type = "text/plain"
        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL")
        i.putExtra(Intent.EXTRA_TEXT, urlImg)
        startActivity(Intent.createChooser(i, "Share Image"))
    }
    fun DownloadImage(ImageUrl: String?) {
        if (ContextCompat.checkSelfPermission(
                this@ZoomImageActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this@ZoomImageActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@ZoomImageActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                123
            )
            ActivityCompat.requestPermissions(
                this@ZoomImageActivity,
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
                    Toast.makeText(this@ZoomImageActivity, "image saved", Toast.LENGTH_LONG).show()
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
}