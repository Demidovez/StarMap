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

    }

    private val holst = Paint().apply {
        style = Paint.Style.FILL
    }

    private val border = Paint().apply {
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    override fun onSizeChanged(width: Int, height: Int, oldwidth: Int, oldheight: Int) {
        super.onSizeChanged(width, height, oldwidth, oldheight)

        // Default values
        updateContainerSize(width.toFloat(), height.toFloat())
        updateCanvasOriginalSize(2480F, 3508F)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //canvas.drawColor(backgroundColorCanvas)
        holst.color = backgroundColorCanvas
        border.color = canvasBorderColor

        // Рисуем холст
        canvas.drawRect(startX, startY, startX + canvasWidth, startY + canvasHeight, holst)

        // Рисуем рамку
        canvas.drawRect(startX + 40F, startY + 40F, startX + canvasWidth - 40F, startY + canvasHeight - 40F, border)
    }
}