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
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.imagesearch.base.BaseActivity
import com.example.imagesearch.base.Common
import com.example.imagesearch.databinding.ActivityPermissionBinding
import com.vapp.admoblibrary.ads.AppOpenManager


class PermissionActivity : BaseActivity() {
    lateinit var binding: ActivityPermissionBinding
    private var checkPermission : Boolean = false

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        Common.setThemeForActivity(this)
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)


        checkPermission = Common.getPerMission(this)

        if (checkPermission){
            binding.swPermission.isChecked = true
            binding.tvPermission.isVisible = true
            binding.tvNotPermission.isVisible = false
            binding.tvPermissionLater.isVisible = false
            binding.animSwitch.isVisible = false
            binding.swPermission.isEnabled = false

            binding.btnNext.setOnClickListener {
                nextActivity()
            }
        }else{
            binding.tvPermission.isVisible = false
            binding.tvNotPermission.isVisible = true
            binding.tvPermissionLater.isVisible = true
            binding.animSwitch.isVisible = true
            binding.btnNext.setOnClickListener {
                Toast.makeText(this, "Please allow access to continue", Toast.LENGTH_LONG).show()
            }
            binding.tvPermissionLater.setOnClickListener {
                nextActivity()
            }
        }

        binding.swPermission.setOnCheckedChangeListener { buttonView, isChecked ->
            CheckPer()
        }

    }

    private fun nextActivity() {
        val intent = Intent(this@PermissionActivity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }


    fun CheckPer(){
        if (
            ContextCompat.checkSelfPermission(
                this@PermissionActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this@PermissionActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val permissions = arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(this, permissions, 123)
        }

    }




    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 123){
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Common.setPerMission(this, true)
                binding.swPermission.isChecked = true
                binding.swPermission.isEnabled = false
                binding.tvPermission.isVisible = true
                binding.tvNotPermission.isVisible = false
                binding.tvPermissionLater.isVisible = false
                binding.animSwitch.isVisible = false
                binding.btnNext.setOnClickListener {
                    nextActivity()
                }
            }else{
                binding.swPermission.isChecked = false
                Common.setPerMission(this,false)
                binding.tvPermission.isVisible = false
                binding.tvNotPermission.isVisible = true
                binding.tvPermissionLater.isVisible = true
                binding.animSwitch.isVisible = true
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    displayDialogCamera()
                }

            }
        }
    }
    private fun displayDialogCamera() {
        val builder = AlertDialog.Builder(this@PermissionActivity)
        builder.setMessage(resources.getString(com.example.imagesearch.R.string.per_mission_message_camera))
        builder.setTitle(resources.getString(com.example.imagesearch.R.string.per_mission_title_camera))
        builder.setCancelable(false)
        builder.setPositiveButton(resources.getString(com.example.imagesearch.R.string.permission_go_to_setting),
            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                AppOpenManager.getInstance().disableAppResumeWithActivity(
                    PermissionActivity::class.java
                )
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.parse("package:$packageName")
                startActivityForResult(intent, 0)
                dialog!!.cancel()
            })
        builder.setNegativeButton(
            resources.getString(com.example.imagesearch.R.string.permission_cancel),
            DialogInterface.OnClickListener { dialog: DialogInterface, which: Int -> dialog.cancel() } as DialogInterface.OnClickListener)
        val alertDialog = builder.create()
        alertDialog.show()
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

        if (
            ContextCompat.checkSelfPermission(
                this@PermissionActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this@PermissionActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        )
        {
            binding.tvPermission.isVisible = false
            binding.tvNotPermission.isVisible = true
            binding.tvPermissionLater.isVisible = true
            binding.animSwitch.isVisible = true
            binding.btnNext.setOnClickListener {
                Toast.makeText(this, "Please allow access to continue", Toast.LENGTH_LONG).show()
            }
            binding.tvPermissionLater.setOnClickListener {
                nextActivity()
            }
        }else{

            binding.swPermission.isChecked = true
            binding.tvPermission.isVisible = true
            binding.tvNotPermission.isVisible = false
            binding.tvPermissionLater.isVisible = false
            binding.animSwitch.isVisible = false
            binding.swPermission.isEnabled = false

            binding.btnNext.setOnClickListener {
                nextActivity()
            }
        }

    }

}