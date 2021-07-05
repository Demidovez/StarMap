package com.nikolaydemidovez.starmap.templates.classic_v1

import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.text.TextPaint
import android.util.DisplayMetrics
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText

class ClassicV1TemplateCanvas(private val context: Context) : TemplateCanvas(context) {
    private var holst: Paint
    private var border: Paint
    private var map: Paint
    private var mapBorder: Paint
    private var descTextPaint: TextPaint
    private var eventLocation: TextPaint
    private var separator: Paint

    init {
        backgroundColorCanvas = ResourcesCompat.getColor(context.resources, R.color.white, null)
        canvasBorderColor = ResourcesCompat.getColor(context.resources, R.color.black, null)

        holst = Paint().apply {
            style = Style.FILL
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        border = Paint().apply {
            style = Style.STROKE
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        map = Paint().apply {
            style = Style.FILL
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        mapBorder = Paint().apply {
            style = Style.FILL
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        descTextPaint = TextPaint().apply {
            textAlign = Align.CENTER
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        eventLocation = TextPaint().apply {
            textAlign = Align.CENTER
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        separator = Paint().apply {
            flags = ANTI_ALIAS_FLAG and FILTER_BITMAP_FLAG and DITHER_FLAG
        }

        draw()
    }

    override fun draw() {
        bitmap = Bitmap.createBitmap(
            //DisplayMetrics().apply { densityDpi = 300; density = 300F } ,
            canvasWidth.toInt(),
            canvasHeight.toInt(),
            Bitmap.Config.ARGB_8888
        )
        //bitmap.density = 300

        val canvas = Canvas(bitmap)
        //canvas.density = 300

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
        canvas.drawMultilineText(descText, descTextPaint, (canvasWidth/1.2).toInt() , canvasWidth/2, canvasWidth/2 + radiusMap + 200F)

        // Рисуем разделитель
        separator.strokeWidth = 50F
        separator.color = Color.parseColor("#DD0000")
        canvas.drawLine(100F, canvasWidth/2 + radiusMap + 200F + 100F, canvasWidth - 2 * 100F, canvasWidth/2 + radiusMap + 200F + 100F, separator)

        // Рисуем текст локации
        eventLocation.textSize = eventLocationSize
        canvas.drawMultilineText(getLocationText(), eventLocation, (canvasWidth/2).toInt() , canvasWidth/2, canvasWidth/2 + radiusMap + 200F + 100F + 50F + 200F)

        //bitmap.density = 300

        listener?.onDraw()
    }
}