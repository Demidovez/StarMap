package com.nikolaydemidovez.starmap.templates.classic_v1

import android.content.Context
import android.graphics.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class ClassicV1TemplateCanvas(private val context: Context) : TemplateCanvas(context) {
    private val STROKE_WIDTH = 10f
    private var holst: Paint
    private var border: Paint

    init {
        backgroundColorCanvas = ResourcesCompat.getColor(context.resources, R.color.white, null)
        canvasBorderColor = ResourcesCompat.getColor(context.resources, R.color.black, null)

        holst = Paint().apply {
            style = Paint.Style.FILL
        }

        border = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
        }

        draw()
    }

    override fun draw() {
        bitmap = Bitmap.createBitmap(
            canvasWidth.toInt(),
            canvasHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        holst.color = backgroundColorCanvas
        border.color = canvasBorderColor

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth, canvasHeight, holst)

        // Рисуем рамку
        canvas.drawRect(40F, 40F, canvasWidth - 40F, canvasHeight - 40F, border)

        listener?.onDraw()
    }
}