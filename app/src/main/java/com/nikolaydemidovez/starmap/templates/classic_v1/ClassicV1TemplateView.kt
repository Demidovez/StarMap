package com.nikolaydemidovez.starmap.templates.classic_v1

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateView

class ClassicV1TemplateView(context: Context?, attrs: AttributeSet?) : TemplateView(context, attrs) {
    private val STROKE_WIDTH = 10f

    init {
        backgroundColorCanvas = ResourcesCompat.getColor(resources, R.color.white, null)
        canvasBorderColor = ResourcesCompat.getColor(resources, R.color.black, null)

        updateCanvasSize(2480F, 3508F)
    }

    private val holst = Paint().apply {
        style = Paint.Style.FILL
    }

    private val border = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        holst.color = backgroundColorCanvas
        border.color = canvasBorderColor

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth, canvasHeight, holst)

        // Рисуем рамку
        canvas.drawRect(40F, 40F, canvasWidth - 40F, canvasHeight - 40F, border)
    }
}