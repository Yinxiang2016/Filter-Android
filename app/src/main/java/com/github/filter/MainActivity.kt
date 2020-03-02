package com.github.filter

import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


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
    }


    fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
    }

    fun takePhoto() {
        val intentToTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val fileDir =
            File(Environment.getExternalStorageDirectory().toString() + File.separator.toString() + "photoTest" + File.separator)

        if (!fileDir.exists()) {
            fileDir.mkdirs()
        }

        val photoFile = File(fileDir, "photo.jpeg")
        mTempPhotoPath = photoFile.absolutePath
        imageUri = FileProvider.getUriForFile(this, "",photoFile)
        intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentToTakePhoto, RC_TAKE_PHOTO)
    }

    //将矩阵设置到图像
    private fun setImageMatrix(bitmap: Bitmap) {
        val mColorMatrix = FloatArray(20)

        val bmp = Bitmap.createBitmap(
            bitmap.getWidth(),
            bitmap.getHeight(),
            Bitmap.Config.ARGB_8888
        )
        val colorMatrix = ColorMatrix()
        colorMatrix.set(mColorMatrix) //将一维数组设置到ColorMatrix
        val canvas = Canvas(bmp)
        val paint = Paint()
        paint.colorFilter = ColorMatrixColorFilter(colorMatrix)
//        canvas.drawBitmap(bitmap, 0, 0, paint)
//        iv_photo.setImageBitmap(bmp)
    }
}
