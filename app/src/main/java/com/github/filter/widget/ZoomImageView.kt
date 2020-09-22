package com.github.filter.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.*
import androidx.appcompat.widget.AppCompatImageView
import com.github.filter.R
import kotlin.math.sqrt
import kotlin.properties.Delegates


/**
 * @author xiang.yin.o（xiang.yin.o@nio.com）
 * @date 2020/3/23
 * Description: 可以根据手势放大缩小的ImageView
 *
 * 边界
 */

class ZoomImageView : AppCompatImageView, View.OnTouchListener,
    ScaleGestureDetector.OnScaleGestureListener, ViewTreeObserver.OnGlobalLayoutListener {

    /**
     * 手势检测
     */
    private lateinit var mScaleGestureDetector: ScaleGestureDetector

    private var mTouchSlop by Delegates.notNull<Int>()

    private var mScaleMatrix: Matrix = Matrix()

    /**
     * 最大放大倍数
     */
    val SCALE_MAX = 4.0f

    /**
     * 默认缩放
     */
    private val initScale = 1.0f

    /**
     * 处理矩阵的9个值
     */
    var matrixValue = FloatArray(9)


    private val mLastPointerCount = 0


    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.ZoomImageView, defStyle, 0
        )
        a.recycle()

        scaleType = ScaleType.MATRIX
        mScaleGestureDetector = ScaleGestureDetector(context, this)
        this.setOnTouchListener(this)

        mTouchSlop = ViewConfiguration.get(context).scaledDoubleTapSlop
    }


    /**
     * 双击
     */
    fun doubleClick() {

    }

    /**
     * 单击
     */
    fun singleClick() {

    }

    /**
     * 缩小
     */
    fun zoomOut() {

    }

    /**
     * 放大
     */
    fun zoomIn() {

    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (mScaleGestureDetector.onTouchEvent(event)) return true


        if (event?.pointerCount ?: 0 > 1) {
            mScaleGestureDetector.onTouchEvent(event)
        }


        var x = 0f
        var y = 0f


        val pointerCount = event?.pointerCount ?: 0

        for (i in 0 until pointerCount) {
            x += (event?.getX(i) ?: 0f)
            y += (event?.getY(i) ?: 0f)
        }

        //中心点
        x /= pointerCount
        y /= pointerCount


        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {

            }
            MotionEvent.ACTION_MOVE -> {

            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {

            }
        }







        return false
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        val scaleFactor = detector?.scaleFactor ?: 1f
        if (drawable == null) {
            return true
        }
        mScaleMatrix.postScale(scaleFactor, scaleFactor, width / 2f, height / 2f);
        imageMatrix = mScaleMatrix
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
        return true
    }

    override fun onScaleEnd(detector: ScaleGestureDetector?) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    private var isInit = true
    override fun onGlobalLayout() {
        if (!isInit) return
        if (drawable == null) return

        val imgWidth = drawable.intrinsicWidth
        val imgHeight = drawable.intrinsicHeight
        var scale = 1.0f

        //如果图片的宽或高大于屏幕，缩放至屏幕的宽或者高
        if (imgWidth > width && imgHeight <= height) {
            scale = width.toFloat() / imgWidth
        }
        if (imgHeight > height && imgWidth <= width) {
            scale = height.toFloat() / imgHeight
        }
        //如果图片宽高都大于屏幕，按比例缩小
        if (imgWidth > width && imgHeight > height) {
            scale = (imgWidth.toFloat() / width).coerceAtMost(imgHeight.toFloat() / height)
        }

        //将图片移动至屏幕中心
        mScaleMatrix.postTranslate((width - imgWidth) / 2f, (height - imgHeight) / 2f)
        mScaleMatrix.postScale(scale, scale, width / 2f, height / 2f)
        imageMatrix = mScaleMatrix
        isInit = false
    }

    /**
     * 根据当前图片的Matrix获得图片的范围
     *
     */
    private fun getMatrixRectF(): RectF {
        val matrix = mScaleMatrix;
        val rectF = RectF()
        if (drawable != null) {
            rectF.set(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
            matrix.mapRect(rectF)
        }
        return rectF
    }


    /**
     * 移动时，进行边界判断，主要判断宽或高大于屏幕的
     */
    private fun checkMatrixBounds() {

        val rectF = getMatrixRectF()
        var deltaX = 0f
        var deltaY = 0f

        val viewWidth = width
        val viewHeight = height
    }


    /**
     * 是否是推动行为
     * mTouchSlop 100
     *
     * @param dx
     * @param dy
     */
    private fun isCanDrag(dx: Float, dy: Float): Boolean {
        return sqrt(((dx * dx) + (dy * dy)).toDouble()) >= mTouchSlop;

    }

    /**
     * 获取当前缩放比例
     */
    private fun getScale(): Float {
        return 1f;
    }

    override fun setImageBitmap(bm: Bitmap?) {
        reSetSate()
        super.setImageBitmap(bm)
    }

    override fun setImageResource(resId: Int) {
        reSetSate()
        super.setImageResource(resId)
    }

    override fun setImageDrawable(drawable: Drawable?) {
        reSetSate()
        super.setImageDrawable(drawable)
    }

    /**
     * 设置图片资源后重新初始化
     */
    private fun reSetSate() {
        isInit = true
        tag = null//????
        mScaleMatrix.reset()
    }




}