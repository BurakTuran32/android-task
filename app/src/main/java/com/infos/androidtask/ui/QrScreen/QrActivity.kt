package com.infos.androidtask.ui.QrScreen

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.budiyev.android.codescanner.*
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.infos.androidtask.R
import com.infos.androidtask.databinding.ActivityQrBinding

class QrActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 200
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityQrBinding
    private var codeScanner: CodeScanner? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        barcodeDetector =
            BarcodeDetector.Builder(applicationContext).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        if (!isPermissionGranted()){
            askForCameraPermission()

        } else {
            setupControls()
        }

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }
    private fun isPermissionGranted():Boolean{
       val permission = ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.CAMERA)
        return permission == PackageManager.PERMISSION_GRANTED
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted()){
            codeScanner?.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        codeScanner?.releaseResources()
    }




    private fun setupControls() {
        codeScanner = applicationContext?.let { CodeScanner(it, binding.scannerView) }

        codeScanner?.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        codeScanner?.decodeCallback = DecodeCallback {
            runOnUiThread {
                scannedValue = it.text
                val resultIntent = Intent()
                resultIntent.putExtra("result", scannedValue)
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }
        codeScanner?.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(applicationContext, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()

            }
        }
        binding.scannerView.setOnClickListener {
            codeScanner?.startPreview()
        }
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED ){
                setupControls()

            }


        }
    }



    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
}