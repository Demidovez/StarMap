package com.nikolaydemidovez.starmap.templates.classic_v1

import android.content.Context
import android.graphics.*
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class ClassicV1TemplateCanvas(private val context: Context) : TemplateCanvas(context) {
    private var holst: Paint
    private var border: Paint
    private var map: Paint
    private var mapBorder: Paint
    private var descTextPaint: Paint
    private var eventLocation: Paint
    private var separator: Paint

    init {
        backgroundColorCanvas = ResourcesCompat.getColor(context.resources, R.color.white, null)
        canvasBorderColor = ResourcesCompat.getColor(context.resources, R.color.black, null)

        holst         = Paint().apply { style = Paint.Style.FILL }
        border        = Paint().apply { style = Paint.Style.STROKE }
        map           = Paint().apply { style = Paint.Style.FILL }
        mapBorder     = Paint().apply { style = Paint.Style.FILL }
        descTextPaint = Paint().apply { textAlign = Paint.Align.CENTER }
        eventLocation = Paint().apply { textAlign = Paint.Align.CENTER }
        separator     = Paint().apply { }

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

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth, canvasHeight, holst)

        // Рисуем рамку
        if(hasBorderCanvas) {
            border.color = canvasBorderColor
            border.strokeWidth = widthBorderCanvas

            canvas.drawRect(indentBorderCanvas, indentBorderCanvas, canvasWidth - indentBorderCanvas, canvasHeight - indentBorderCanvas, border)
        }

        // Рисуем рамку карты
        mapBorder.color = Color.parseColor("#FF0000")
        canvas.drawCircle(canvasWidth/2, canvasWidth/2, radiusMap + 100F, mapBorder)

        // Рисуем карту
        map.color = Color.parseColor("#000000")
        canvas.drawCircle(canvasWidth/2, canvasWidth/2, radiusMap, map)

        // Рисуем текст описание
        descTextPaint.textSize = descTextSize
        canvas.drawText(descText,  canvasWidth/2, canvasWidth/2 + radiusMap + 200F, descTextPaint)

        // Рисуем разделитель
        separator.strokeWidth = 50F
        separator.color = Color.parseColor("#DD0000")
        canvas.drawLine(100F, canvasWidth/2 + radiusMap + 200F + 100F, canvasWidth - 2 * 100F, canvasWidth/2 + radiusMap + 200F + 100F, separator)

        // Рисуем текст локации
        eventLocation.textSize = eventLocationSize
        canvas.drawText(getLocationText(),  canvasWidth/2, canvasWidth/2 + radiusMap + 200F + 100F + 50F + 200F, eventLocation)

        listener?.onDraw()
    }
}