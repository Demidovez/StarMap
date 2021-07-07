package com.nikolaydemidovez.starmap.templates.classic_v1

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.Paint.*
import android.os.AsyncTask
import android.os.Looper
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.Log
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.FragmentActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText
import kotlinx.coroutines.delay
import java.lang.Thread.sleep




class ClassicV1TemplateCanvas(private val activity: Activity) : TemplateCanvas(activity) {
    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapMapBorder: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap

    init {
        drawHolst()
        drawHolstBorder()
        drawMap()
        drawMapBorder()
        drawDesc()
        drawSeparator()
        drawLocationText()

        draw()
    }

    private fun drawHolst() {
        val holst = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = backgroundColorCanvas
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(0F, 0F, canvasWidth, canvasHeight, holst)

        bitmapHolst = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), canvasHeight.toInt())
    }

    private fun drawHolstBorder() {
        val border = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.STROKE
            color = canvasBorderColor
            strokeWidth = widthBorderCanvas // TODO: Ширина границы не изменяет общую длину отступа от края?
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(indentBorderCanvas, indentBorderCanvas, canvasWidth - indentBorderCanvas, canvasHeight - indentBorderCanvas, border)

        bitmapHolstBorder = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), canvasHeight.toInt())
    }

    private fun drawMap() {
        val map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = backgroundColorMap
            isDither = true
            isAntiAlias = true
        }
        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(radiusMap, radiusMap, radiusMap, map)
        bitmapMap = Bitmap.createBitmap(tempBitmap, 0, 0, (radiusMap * 2).toInt(), (radiusMap * 2).toInt())
    }

    private fun drawMapBorder() {
        val mapBorder = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = mapBorderColor
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(radiusMap + widthBorderMap, radiusMap + widthBorderMap, radiusMap + widthBorderMap, mapBorder)

        bitmapMapBorder = Bitmap.createBitmap(tempBitmap, 0, 0, ((radiusMap + widthBorderMap) * 2).toInt(), ((radiusMap + widthBorderMap) * 2).toInt())
    }

    private fun drawDesc() {
        val descTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = descTextSize
            typeface = ResourcesCompat.getFont(activity.applicationContext, R.font.caveat_regular)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        val heightAllText = Canvas(tempBitmap).drawMultilineText(descText, descTextPaint, (canvasWidth/1.5).toInt() , canvasWidth/2, 0F)

        bitmapDesc = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), heightAllText)
    }

    private fun drawSeparator() {
        val separator = Paint(ANTI_ALIAS_FLAG).apply {
            isDither = true
            isAntiAlias = true
            strokeWidth = separatorHeight
            color = separatorColor
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawLine(0F, 0F, separatorWidth, 0F, separator)

        bitmapSeparator = Bitmap.createBitmap(tempBitmap, 0, 0, separatorWidth.toInt(), separatorHeight.toInt())
    }

    private fun drawLocationText() {
        val eventLocation = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = eventLocationSize
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

        val heightAllText = Canvas(tempBitmap).drawMultilineText(getLocationText(), eventLocation, (canvasWidth/2).toInt() , canvasWidth/2,  0F)

        bitmapLocationText = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), heightAllText)
    }

    private fun getBottomMarginLocationText(): Int {
        var margin = bitmapLocationText.height.toFloat()

        if(hasBorderCanvas) {
            margin += indentBorderCanvas + widthBorderCanvas + (indentBorderCanvas*0.5).toFloat()
        } else {
            margin += (canvasHeight * 0.6).toFloat()
        }

        return margin.toInt()
    }

    private fun getTopMarginDescText(): Float {
        var margin = 0F

        if(hasBorderMap) {
            margin += canvasWidth/2 + bitmapMapBorder.height / 2
        } else {
            margin += canvasWidth/2 + bitmapMap.height / 2
        }

        return margin
    }

    override fun draw() {
            Thread {
                // TODO: Оптимизировать (избыточная прорисовка)
                drawHolst()
                drawHolstBorder()
                drawMap()
                drawMapBorder()
                drawDesc()
                drawSeparator()
                drawLocationText()

                bitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

                val canvas = Canvas(bitmap)

                // Рисуем холст
                canvas.drawBitmap(bitmapHolst, 0F, 0F, Paint())

                // Рисуем рамку холста
                if(hasBorderCanvas) {
                    canvas.drawBitmap(bitmapHolstBorder, 0F, 0F, Paint())
                }

                // Рисуем рамку карты
                if(hasBorderMap) {
                    canvas.drawBitmap(bitmapMapBorder, canvasWidth/2 - bitmapMapBorder.width / 2, canvasWidth/2 - bitmapMapBorder.width / 2, Paint())
                }

                // Рисуем карту
                canvas.drawBitmap(bitmapMap, canvasWidth/2 - bitmapMap.width / 2, canvasWidth/2 - bitmapMap.width / 2, Paint())


                // Рисуем текст описание
                canvas.drawBitmap(bitmapDesc, 0F, getTopMarginDescText() , Paint())

                // Рисуем разделитель
                if(hasSeparator) {
                    canvas.drawBitmap(bitmapSeparator, 100F, getTopMarginDescText(), Paint())
                }

                // Рисуем текст локации
                canvas.drawBitmap(bitmapLocationText, 0F, canvasHeight - getBottomMarginLocationText(), Paint())

                activity.runOnUiThread {
                    listener?.onDraw()
                }

            }.start()

    }
}