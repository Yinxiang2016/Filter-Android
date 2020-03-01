package com.github.filter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    var mGLSurfaceView: GLSurfaceView? = null
    var colorMatrix: ColorMatrix? = null
    var colorMatrixColorFilter: ColorMatrixColorFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
