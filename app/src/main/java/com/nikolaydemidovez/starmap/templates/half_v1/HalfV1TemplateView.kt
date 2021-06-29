package com.nikolaydemidovez.starmap.templates.half_v1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.applyCanvas
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateView

class HalfV1TemplateView(context: Context?, attrs: AttributeSet?) : TemplateView(context, attrs) {
    private val STROKE_WIDTH = 12f

    init {
        backgroundColorCanvas = ResourcesCompat.getColor(resources, R.color.white, null)
    }

    private val paint = Paint().apply {
        color = ResourcesCompat.getColor(resources, R.color.red, null)
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
        canvas.drawRect(80F, 80F, 220F, 220F, paint)
    }
}