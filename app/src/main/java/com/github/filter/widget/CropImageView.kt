package com.github.filter.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.github.filter.R

/**
 * @author Lion007（yinxiang2016@gmail.com）
 * @date 2020/3/3 9:23 AM
 * Description:
 */

class CropImageView : AppCompatImageView {

    private var _exampleString: String? = null
    private var _exampleColor: Int = Color.RED
    private var _exampleDimension: Float = 0f

    private var textPaint: TextPaint? = null
    private var textWidth: Float = 0f
    private var textHeight: Float = 0f


    var exampleString: String?
        get() = _exampleString
        set(value) {
            _exampleString = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleColor: Int
        get() = _exampleColor
        set(value) {
            _exampleColor = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleDimension: Float
        get() = _exampleDimension
        set(value) {
            _exampleDimension = value
            invalidateTextPaintAndMeasurements()
        }

    var exampleDrawable: Drawable? = null

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
        // Load attributes
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.CropImageView, defStyle, 0
        )

        _exampleString = a.getString(
            R.styleable.CropImageView_exampleString
        )
        _exampleColor = a.getColor(
            R.styleable.CropImageView_exampleColor,
            exampleColor
        )
        _exampleDimension = a.getDimension(
            R.styleable.CropImageView_exampleDimension,
            exampleDimension
        )

        if (a.hasValue(R.styleable.CropImageView_exampleDrawable)) {
            exampleDrawable = a.getDrawable(
                R.styleable.CropImageView_exampleDrawable
            )
            exampleDrawable?.callback = this
        }
        a.recycle()
        textPaint = TextPaint().apply {
            flags = Paint.ANTI_ALIAS_FLAG
            textAlign = Paint.Align.LEFT
        }
        invalidateTextPaintAndMeasurements()
    }

    private fun invalidateTextPaintAndMeasurements() {
        textPaint?.let {
            it.textSize = exampleDimension
            it.color = exampleColor
            textWidth = it.measureText(exampleString)
            textHeight = it.fontMetrics.bottom
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = paddingLeft
        val paddingTop = paddingTop
        val paddingRight = paddingRight
        val paddingBottom = paddingBottom

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        exampleString?.let {
            // Draw the text.
            canvas.drawText(
                it,
                paddingLeft + (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + textHeight) / 2,
                textPaint
            )
        }

        // Draw the example drawable on top of the text.
        exampleDrawable?.let {
            it.setBounds(
                paddingLeft, paddingTop,
                paddingLeft + contentWidth, paddingTop + contentHeight
            )
            it.draw(canvas)
        }
    }

    /**
     * 四周，四条线
     */
    fun drawOutside(canvas: Canvas) {
        val paint = Paint()

        paint.setARGB(125, 50, 50, 50)
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true

        canvas.drawLine(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
        canvas.drawLine(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), paint)
    }

    /**
     * 四个角
     */
    fun drawFourCorners(canvas: Canvas) {

    }


}
