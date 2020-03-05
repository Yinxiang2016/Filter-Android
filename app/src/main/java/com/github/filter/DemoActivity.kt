package com.github.filter

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_demo.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Lion007（yinxiang2016@gmail.com）
 * @date 2020/3/5 8:45 PM
 * Description: 测试Activity
 */

class DemoActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks {

    var mGLSurfaceView: GLSurfaceView? = null
    var colorMatrix: ColorMatrix? = null
    var colorMatrixColorFilter: ColorMatrixColorFilter? = null

    private val RC_TAKE_PHOTO = 1
    private val RC_CHOOSE_PHOTO = 2


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo)

        button_take_photo.setOnClickListener { requestCamera() }
        button_choose_photo.setOnClickListener { choosePhoto() }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RC_CHOOSE_PHOTO) {
                setPhoto(data?.data)
            } else if (requestCode == RC_TAKE_PHOTO) {
                setPic()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        //TODO
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        //TODO
        dispatchTakePhoto()
    }

    private fun requestCamera() {
        if (EasyPermissions.hasPermissions(
                this,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            dispatchTakePhoto()
        } else {
            EasyPermissions.requestPermissions(this, "ssss", 111, Manifest.permission.CAMERA)
        }
    }


    private fun setPhoto() {

    }

    private fun setPhoto(uri: Uri?) {
        Glide.with(this).load(uri).into(imageView)
    }

    /**
     * 拍照缩率图
     */
    private fun setthumbnail(data: Intent?) {
        val imageBitmap = data?.extras?.get("data") as Bitmap
        imageView.setImageBitmap(imageBitmap)
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, RC_TAKE_PHOTO)
            }
        }
    }

    private var currentPhotoPath: String? = null

    @Throws(IOException::class)
    fun createFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
        }
    }

    private fun dispatchTakePhoto() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePhotoIntent ->
            takePhotoIntent.resolveActivity(packageManager)?.also {
                val photoFile: File? = try {
                    createFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "com.github.filter.file_provider",
                        it
                    )
                    takePhotoIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
                    takePhotoIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    startActivityForResult(takePhotoIntent, RC_TAKE_PHOTO)
                }
            }
        }
    }

    private fun galleryAddPic() {
        Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE).also { mediaScanIntent ->
            val f = File(currentPhotoPath)
            mediaScanIntent.data = Uri.fromFile(f)
            sendBroadcast(mediaScanIntent)
        }
    }

    private fun setPic() {
//        Glide.with(this).load(currentPhotoPath).into(imageView)

        // Get the dimensions of the View
        val targetW: Int = imageView.width
        val targetH: Int = imageView.height

        val bmOptions = BitmapFactory.Options().apply {
            // Get the dimensions of the bitmap
            inJustDecodeBounds = true

            val photoW: Int = outWidth
            val photoH: Int = outHeight

            // Determine how much to scale down the image
            val scaleFactor: Int = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            inJustDecodeBounds = false
            inSampleSize = scaleFactor
            inPurgeable = true
        }
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions)?.also { bitmap ->
            imageView.setImageBitmap(bitmap)
        }
    }


    fun choosePhoto() {
        val intentToPickPic = Intent(Intent.ACTION_PICK, null)
        intentToPickPic.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(intentToPickPic, RC_CHOOSE_PHOTO)
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
