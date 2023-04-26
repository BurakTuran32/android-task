package com.infos.androidtask.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.SurfaceHolder
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.infos.androidtask.R
import com.infos.androidtask.databinding.ActivityQrBinding
import java.io.IOException

class QrActivity : AppCompatActivity() {

    private val requestCodeCameraPermission = 1001
    private lateinit var cameraSource: CameraSource
    private lateinit var barcodeDetector: BarcodeDetector
    private var scannedValue = ""
    private lateinit var binding: ActivityQrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQrBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        if (ContextCompat.checkSelfPermission(
                applicationContext, Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            askForCameraPermission()
        } else {
            setupControls()
        }

        val aniSlide: Animation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.scanner_animation)
        binding.barcodeLine.startAnimation(aniSlide)
    }


    private fun setupControls() {
        barcodeDetector =
            BarcodeDetector.Builder(applicationContext).setBarcodeFormats(Barcode.ALL_FORMATS).build()

        cameraSource = CameraSource.Builder(applicationContext, barcodeDetector)
            .setRequestedPreviewSize(1920, 1080)
            .setAutoFocusEnabled(true)
            .build()

        binding.cameraSurfaceView.getHolder().addCallback(object : SurfaceHolder.Callback {

            override fun surfaceCreated(holder: SurfaceHolder) {
                try {

                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    }
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }


            override fun surfaceChanged(
                holder: SurfaceHolder,
                format: Int,
                width: Int,
                height: Int
            ) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            applicationContext,
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {

                        return
                    }
                    cameraSource.start(holder)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })


        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                Toast.makeText(applicationContext, "Scanner has been closed", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val barcodes = detections.detectedItems
                if (barcodes.size() == 1) {
                    scannedValue = barcodes.valueAt(0).rawValue



                    runOnUiThread {
                        cameraSource.stop()
                        Toast.makeText(applicationContext, "value- $scannedValue", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }else
                {
                    Toast.makeText(applicationContext, "value- else", Toast.LENGTH_SHORT).show()

                }
            }
        })
    }

    private fun askForCameraPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.CAMERA),
            requestCodeCameraPermission
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCodeCameraPermission && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupControls()
            } else {
                Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.stop()
    }
}