package com.shajib.selectimagefromgallery

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat.checkSelfPermission
import com.shajib.selectimagefromgallery.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val REQUEST_CODE_STORAGE_PERMISSION = 1000
    private val REQUEST_CODE_GALLERY = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPermission.setOnClickListener {
            checkStoragePermission() //check permission
        }

    }

    private fun checkStoragePermission() {
        if ((checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) ||
            (checkSelfPermission(this, WRITE_EXTERNAL_STORAGE) != PERMISSION_GRANTED)
        ) {
            requestPermissions(
                this,
                arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), //multiple permission
                REQUEST_CODE_STORAGE_PERMISSION
            )
        } else {
            selectImageFromGallery()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION && grantResults.isNotEmpty()) {
            selectImageFromGallery()
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    //select the image and convert to bitmap and set to imageView
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK) {
            data?.data?.let { imageUri ->
                try {
                    val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, imageUri)
                    runOnUiThread {
                        binding.ivImage.setImageBitmap(imageBitmap)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    private fun selectImageFromGallery() {
        startActivityForResult(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI), REQUEST_CODE_GALLERY)
    }
}