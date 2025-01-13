package com.oreca.kotlinbaseforopengles

import android.app.ActivityManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.oreca.kotlinbaseforopengles.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var rendererSet = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initOpenGLES()
    }

    private fun initOpenGLES() {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val configurationInfo = activityManager.deviceConfigurationInfo
        val supportsEs3 = configurationInfo.reqGlEsVersion >= 0x30000

        if (supportsEs3) {
            binding.myGLSurfaceView.setEGLContextClientVersion(3)
            binding.myGLSurfaceView.setRenderer(MyBaseRenderer())
            rendererSet = true
        } else {
            Toast.makeText(this@MainActivity, "This device doesn't support OpenGL ES 3.0", Toast.LENGTH_SHORT).show()
            return
        }
    }

    override fun onResume() {
        super.onResume()
        if (rendererSet) {
            binding.myGLSurfaceView.onResume()
        }
    }

    override fun onPause() {
        super.onPause()
        if (rendererSet) {
            binding.myGLSurfaceView.onPause()
        }
    }
}