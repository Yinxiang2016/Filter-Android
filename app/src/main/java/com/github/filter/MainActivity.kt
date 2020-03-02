package com.github.filter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 * @author Lion007（yinxiang2016@gmail.com）
 * @date 2020/2/29
 * Description:
 */
class MainActivity : AppCompatActivity() {
    var mGLSurfaceView: GLSurfaceView? = null
    var colorMatrix: ColorMatrix? = null
    var colorMatrixColorFilter: ColorMatrixColorFilter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
