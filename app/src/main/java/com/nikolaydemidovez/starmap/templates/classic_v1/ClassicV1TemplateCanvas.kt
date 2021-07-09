package com.nikolaydemidovez.starmap.templates.classic_v1

import android.graphics.*
import android.graphics.Paint.*
import android.text.TextPaint
import android.util.Log
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText
import java.sql.Time
import java.util.*

class ClassicV1TemplateCanvas(private val activity: MainActivity) : TemplateCanvas(activity) {
    private var isDataInitialized = false

    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapMapBorder: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap

    init {
        canvasWidth.value                               = 2480F
        canvasHeight.value                              = 3508F
        backgroundColorCanvas.value                     = Color.parseColor("#FFFFFF")
        canvasBorderColor.value                         = Color.parseColor("#000000")
        hasBorderCanvas.value                           = true
        indentBorderCanvas.value                        = 100F
        widthBorderCanvas.value                         = 10F
        backgroundColorMap.value                        = Color.parseColor("#000000")
        radiusMap.value                                 = 1000F
        hasBorderMap.value                              = false
        widthBorderMap.value                            = 15F
        mapBorderColor.value                            = Color.parseColor("#FFFFFF")
        descTextSize.value                              = 160F
        eventLocationSize.value                         = 60F
        descText.value                                  = "День, когда сошлись\nвсе звезды вселенной..."
        hasEventDateInLocation.value                    = true
        eventDate.value                                 = Date()
        hasEventTimeInLocation.value                    = true
        eventTime.value                                 = Date()
        hasEventCityInLocation.value                    = true
        eventCity.value                                 = "Москва"
        hasEventCoordinatesInLocation.value             = true
        eventLatitude.value                             = 41.40338
        eventLongitude.value                            = 2.17403
        hasSeparator.value                              = true
        separatorColor.value                            = Color.parseColor("#000000")
        separatorWidth.value                            = 1200F
        separatorHeight.value                           = 7F

        canvasWidth.observe(activity,                   { redraw(arrayOf(::drawHolst, ::drawHolstBorder)) })
        canvasHeight.observe(activity,                  { redraw(arrayOf(::drawHolst, ::drawHolstBorder)) })
        backgroundColorCanvas.observe(activity,         { redraw(arrayOf(::drawHolst)) })
        canvasBorderColor.observe(activity,             { redraw(arrayOf(::drawHolstBorder)) })
        hasBorderCanvas.observe(activity,               { redraw(arrayOf(::drawHolstBorder)) })
        indentBorderCanvas.observe(activity,            { redraw(arrayOf(::drawHolstBorder)) })
        widthBorderCanvas.observe(activity,             { redraw(arrayOf(::drawHolstBorder)) })
        backgroundColorMap.observe(activity,            { redraw(arrayOf(::drawMap)) })
        radiusMap.observe(activity,                     { redraw(arrayOf(::drawMap)) })
        hasBorderMap.observe(activity,                  { redraw(arrayOf(::drawMap)) })
        widthBorderMap.observe(activity,                { redraw(arrayOf(::drawMap)) })
        mapBorderColor.observe(activity,                { redraw(arrayOf(::drawMap)) })
        descTextSize.observe(activity,                  { redraw(arrayOf(::drawDesc)) })
        eventLocationSize.observe(activity,             { redraw(arrayOf(::drawLocationText)) })
        descText.observe(activity,                      { redraw(arrayOf(::drawDesc)) })
        hasEventDateInLocation.observe(activity,        { redraw(arrayOf(::drawLocationText)) })
        eventDate.observe(activity,                     { redraw(arrayOf(::drawLocationText)) })
        hasEventTimeInLocation.observe(activity,        { redraw(arrayOf(::drawLocationText)) })
        eventTime.observe(activity,                     { redraw(arrayOf(::drawLocationText)) })
        hasEventCityInLocation.observe(activity,        { redraw(arrayOf(::drawLocationText)) })
        eventCity.observe(activity,                     { redraw(arrayOf(::drawLocationText)) })
        hasEventCoordinatesInLocation.observe(activity, { redraw(arrayOf(::drawLocationText)) })
        eventLatitude.observe(activity,                 { redraw(arrayOf(::drawLocationText)) })
        eventLongitude.observe(activity,                { redraw(arrayOf(::drawLocationText)) })
        hasSeparator.observe(activity,                  { redraw(null) })
        separatorColor.observe(activity,                { redraw(arrayOf(::drawSeparator)) })
        separatorWidth.observe(activity,                { redraw(arrayOf(::drawSeparator)) })
        separatorHeight.observe(activity,               { redraw(arrayOf(::drawSeparator)) })

        firstDraw()
    }

    private fun firstDraw() {
        drawHolst()
        drawHolstBorder()
        drawMap()
        drawMapBorder()
        drawDesc()
        drawSeparator()
        drawLocationText()
        draw()

        isDataInitialized = true
    }

    private fun redraw(callbacks: Array<() -> Unit?>?) {
        if(isDataInitialized) {
            callbacks?.forEach { it() }

            draw()
        }
    }

    private fun drawHolst() {
        val holst = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = backgroundColorCanvas.value!!
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(0F, 0F, canvasWidth.value!!, canvasHeight.value!!, holst)

        bitmapHolst = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt())
    }

    private fun drawHolstBorder() {
        val border = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.STROKE
            color = canvasBorderColor.value!!
            strokeWidth = widthBorderCanvas.value!!  //TODO: Ширина границы не изменяет общую длину отступа от края?
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(indentBorderCanvas.value!!, indentBorderCanvas.value!!, canvasWidth.value!! - indentBorderCanvas.value!!, canvasHeight.value!! - indentBorderCanvas.value!!, border)

        bitmapHolstBorder = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt())
    }

    private fun drawMap() {
        val map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = backgroundColorMap.value!!
            isDither = true
            isAntiAlias = true
        }
        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(radiusMap.value!!, radiusMap.value!!, radiusMap.value!!, map)
        bitmapMap = Bitmap.createBitmap(tempBitmap, 0, 0, (radiusMap.value!! * 2).toInt(), (radiusMap.value!! * 2).toInt())
    }

    private fun drawMapBorder() {
        val mapBorder = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = mapBorderColor.value!!
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(radiusMap.value!! + widthBorderMap.value!!, radiusMap.value!! + widthBorderMap.value!!, radiusMap.value!! + widthBorderMap.value!!, mapBorder)

        bitmapMapBorder = Bitmap.createBitmap(tempBitmap, 0, 0, ((radiusMap.value!! + widthBorderMap.value!!) * 2).toInt(), ((radiusMap.value!! + widthBorderMap.value!!) * 2).toInt())
    }

    private fun drawDesc() {
        val descTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = descTextSize.value!!
            typeface = ResourcesCompat.getFont(activity.applicationContext, R.font.caveat_regular)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = descText.value!!.split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, descTextPaint, (canvasWidth.value!!/1.5).toInt() , canvasWidth.value!!/2, totalHeightText)
        }

        bitmapDesc = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.value!!.toInt(), totalHeightText.toInt())
    }

    private fun drawSeparator() {
        val separator = Paint(ANTI_ALIAS_FLAG).apply {
            isDither = true
            isAntiAlias = true
            strokeWidth = separatorHeight.value!!
            color = separatorColor.value!!
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawLine(0F, 0F, separatorWidth.value!!, 0F, separator)

        bitmapSeparator = Bitmap.createBitmap(tempBitmap, 0, 0, separatorWidth.value!!.toInt(), separatorHeight.value!!.toInt())
    }

    private fun drawLocationText() {
        val eventLocation = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = eventLocationSize.value!!
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = getLocationText().split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, eventLocation, (canvasWidth.value!!/2).toInt() , canvasWidth.value!!/2,  totalHeightText)
        }

        bitmapLocationText = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.value!!.toInt(), totalHeightText.toInt())
    }

    private fun getBottomMarginLocationText(): Int {
        var margin = bitmapLocationText.height.toFloat()


        margin = margin.plus(
            if(hasBorderCanvas.value!!) {
                indentBorderCanvas.value!! + widthBorderCanvas.value!! + (indentBorderCanvas.value!!*0.5).toFloat()
            } else {
                (canvasHeight.value!! * 0.6).toFloat()
            }
        )


        return margin.toInt()
    }

    private fun getTopMarginDescText(): Float {
        var margin = 0F

        margin += if(hasBorderMap.value!!) {
            canvasWidth.value!!/2 + bitmapMapBorder.height / 2
        } else {
            canvasWidth.value!!/2 + bitmapMap.height / 2
        }

        return margin
    }

    override fun draw() {
        Thread {
            bitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)

            // Рисуем холст
            canvas.drawBitmap(bitmapHolst, 0F, 0F, null)

            // Рисуем рамку холста
            if(hasBorderCanvas.value!!) {
                canvas.drawBitmap(bitmapHolstBorder, 0F, 0F, null)
            }

            // Рисуем рамку карты
            if(hasBorderMap.value!!) {
                canvas.drawBitmap(bitmapMapBorder, canvasWidth.value!!/2 - bitmapMapBorder.width / 2, canvasWidth.value!!/2 - bitmapMapBorder.width / 2, null)
            }

            // Рисуем карту
            canvas.drawBitmap(bitmapMap, canvasWidth.value!!/2 - bitmapMap.width / 2, canvasWidth.value!!/2 - bitmapMap.width / 2, null)


            // Рисуем текст описание
            canvas.drawBitmap(bitmapDesc, 0F, getTopMarginDescText(), null)

            // Рисуем разделитель
            if(hasSeparator.value!!) {
                canvas.drawBitmap(bitmapSeparator, 100F, getTopMarginDescText(), null)
            }

            // Рисуем текст локации
            canvas.drawBitmap(bitmapLocationText, 0F, canvasHeight.value!! - getBottomMarginLocationText(), null)

            activity.runOnUiThread {
                listener?.onDraw()
            }

        }.start()
    }
}