package com.nikolaydemidovez.starmap.templates.classic_v1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateView

class ClassicV1TemplateView(context: Context, attrs: AttributeSet) : TemplateView(context, attrs) {
    private val STROKE_WIDTH = 12f

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TemplateView, 0, 0).apply {
            try {
                backgroundColorCanvas = getColor(
                    R.styleable.TemplateView_background_canvas,
                    ResourcesCompat.getColor(resources, R.color.white, null)
                )
            } finally {
                recycle()
            }
        }
    }

    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.white, null)
        isAntiAlias = true
        isDither = true
        style = Paint.Style.STROKE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
        strokeWidth = STROKE_WIDTH
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawColor(backgroundColorCanvas)
        canvas.drawRect(40F, 40F, 260F, 260F, paint)
    }
}