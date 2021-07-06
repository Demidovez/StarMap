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
        holst = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            isDither = true
            isAntiAlias = true
        }

        border = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.STROKE
            isDither = true
            isAntiAlias = true
        }

        map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            isDither = true
            isAntiAlias = true
        }

        mapBorder = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            isDither = true
            isAntiAlias = true
        }

        descTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            isDither = true
            isAntiAlias = true
        }

        eventLocation = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            isDither = true
            isAntiAlias = true
        }

        separator = Paint(ANTI_ALIAS_FLAG).apply {
            isDither = true
            isAntiAlias = true
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

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth, canvasHeight, holst)

        // Рисуем рамку
        if(hasBorderCanvas) {
            border.color = canvasBorderColor
            border.strokeWidth = widthBorderCanvas // TODO: Ширина границы не изменяет общую длину отступа от края?

            canvas.drawRect(indentBorderCanvas, indentBorderCanvas, canvasWidth - indentBorderCanvas, canvasHeight - indentBorderCanvas, border)
        }

        // Рисуем рамку карты
        if(hasBorderMap) {
            mapBorder.color = mapBorderColor
            canvas.drawCircle(canvasWidth/2, canvasWidth/2, radiusMap + widthBorderMap, mapBorder)
        }

        // Рисуем карту
        map.color = backgroundColorMap
        canvas.drawCircle(canvasWidth/2, canvasWidth/2, radiusMap, map)

        // Рисуем текст описание
        descTextPaint.textSize = descTextSize
        descTextPaint.typeface = ResourcesCompat.getFont(context, R.font.caveat_regular)
        canvas.drawMultilineText(descText, descTextPaint, (canvasWidth/1.5).toInt() , canvasWidth/2, canvasWidth/2 + radiusMap + widthBorderMap + 100F)

        // Рисуем разделитель
        if(hasSeparator) {
            separator.strokeWidth = separatorHeight
            separator.color = separatorColor
            canvas.drawLine(
                (canvasWidth - separatorWidth) / 2,
                canvasHeight - 350F - 200F,
                canvasWidth - (canvasWidth - separatorWidth) / 2,
                canvasHeight - 350F - 200F,
                separator
            )
        }

        // Рисуем текст локации
        eventLocation.textSize = eventLocationSize
        canvas.drawMultilineText(getLocationText(), eventLocation, (canvasWidth/2).toInt() , canvasWidth/2,  canvasHeight - 350F)

        //bitmap.density = 300

        listener?.onDraw()
    }
}