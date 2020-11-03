package com.github.filter.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.github.filter.R


/**
 * @author Lion007（yinxiang2016@gmail.com）
 * @date 2020/2/29
 * Description:
 */
class MainActivity : AppCompatActivity() {
    var mGLSurfaceView: GLSurfaceView? = null
    var colorMatrix: ColorMatrix? = null
    var colorMatrixColorFilter: ColorMatrixColorFilter? = null

    val RC_TAKE_PHOTO = 1
    val RC_CHOOSE_PHOTO = 2

    private var mTempPhotoPath: String? = null
    private var imageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        BaseFolderResReplace
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}
