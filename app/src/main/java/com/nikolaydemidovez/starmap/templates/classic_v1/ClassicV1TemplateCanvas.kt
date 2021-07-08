package com.nikolaydemidovez.starmap.templates.classic_v1

import android.app.Activity
import android.graphics.*
import android.graphics.Paint.*
import android.text.TextPaint
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText
import java.util.*

class ClassicV1TemplateCanvas(private val activity: Activity) : TemplateCanvas(activity) {
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
        indentBorderCanvas.value                        = 30F * PIXELS_IN_ONE_MM
        widthBorderCanvas.value                         = 3F * PIXELS_IN_ONE_MM
        backgroundColorMap.value                        = Color.parseColor("#000000")
        radiusMap.value                                 = 1000F
        hasBorderMap.value                              = false
        widthBorderMap.value                            = 5F * PIXELS_IN_ONE_MM
        mapBorderColor.value                            = Color.parseColor("#FFFFFF")
        descTextSize.value                              = 160F
        eventLocationSize.value                         = 60F
        descText.value                                  = "День, когда сошлись все звезды вселенной..."
        hasEventDateInLocation.value                    = true
        eventDate.value                                 = Date()
        hasEventTimeInLocation.value                    = true
        eventTime.value                                 = Date().time
        hasEventCityInLocation.value                    = true
        eventCity.value                                 = "Москва"
        hasEventLatitudeInLocation.value                = true
        eventLatitude.value                             = 55.7522200F
        hasEventLongitudeInLocation.value               = true
        eventLongitude.value                            = 37.6155600F
        hasSeparator.value                              = true
        separatorColor.value                            = Color.parseColor("#000000")
        separatorWidth.value                            = 1200F
        separatorHeight.value                           = 7F

        canvasWidth.observe(activity as MainActivity, {
            Toast.makeText(activity, "canvasWidth", Toast.LENGTH_SHORT).show()
            drawHolst()

            draw()
        })
//
//        canvasHeight.observe(activity as MainActivity, {
//            Toast.makeText(activity, "canvasHeight", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        backgroundColorCanvas.observe(activity as MainActivity, {
//            Toast.makeText(activity, "backgroundColorCanvas", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        canvasBorderColor.observe(activity as MainActivity, {
//            Toast.makeText(activity, "canvasBorderColor", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasBorderCanvas.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasBorderCanvas", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        indentBorderCanvas.observe(activity as MainActivity, {
//            Toast.makeText(activity, "indentBorderCanvas", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        widthBorderCanvas.observe(activity as MainActivity, {
//            Toast.makeText(activity, "widthBorderCanvas", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        backgroundColorMap.observe(activity as MainActivity, {
//            Toast.makeText(activity, "backgroundColorMap", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        radiusMap.observe(activity as MainActivity, {
//            Toast.makeText(activity, "radiusMap", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasBorderMap.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasBorderMap", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        widthBorderMap.observe(activity as MainActivity, {
//            Toast.makeText(activity, "widthBorderMap", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        mapBorderColor.observe(activity as MainActivity, {
//            Toast.makeText(activity, "mapBorderColor", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        descTextSize.observe(activity as MainActivity, {
//            Toast.makeText(activity, "descTextSize", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventLocationSize.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventLocationSize", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        descText.observe(activity as MainActivity, {
//            Toast.makeText(activity, "descText", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasEventDateInLocation.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasEventDateInLocation", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventDate.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventDate", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasEventTimeInLocation.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasEventTimeInLocation", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventTime.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventTime", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasEventCityInLocation.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasEventCityInLocation", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventCity.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventCity", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasEventLatitudeInLocation.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasEventLatitudeInLocation", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventLatitude.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventLatitude", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasEventLongitudeInLocation.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasEventLongitudeInLocation", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        eventLongitude.observe(activity as MainActivity, {
//            Toast.makeText(activity, "eventLongitude", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        hasSeparator.observe(activity as MainActivity, {
//            Toast.makeText(activity, "hasSeparator", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        separatorColor.observe(activity as MainActivity, {
//            Toast.makeText(activity, "separatorColor", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        separatorWidth.observe(activity as MainActivity, {
//            Toast.makeText(activity, "separatorWidth", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//        separatorHeight.observe(activity as MainActivity, {
//            Toast.makeText(activity, "separatorHeight", Toast.LENGTH_SHORT).show()
//            drawHolst()
//
//            draw()
//        })
//
//
//        firstDraw()
    }
//
//    private fun firstDraw() {
//        drawHolst(backgroundColorCanvas.value!!)
//        drawHolstBorder()
//        drawMap()
//        drawMapBorder()
//        drawDesc()
//        drawSeparator()
//        drawLocationText()
//
//        draw()
//    }
//
//    private fun drawHolst() {
//        val holst = Paint(ANTI_ALIAS_FLAG).apply {
//            style = Style.FILL
//            color = backgroundColorCanvas.value
//            isDither = true
//            isAntiAlias = true
//        }
//
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)
//
//        Canvas(tempBitmap).drawRect(0F, 0F, canvasWidth.value!!, canvasHeight.value!!, holst)
//
//        bitmapHolst = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt())
//    }
//
//    private fun drawHolstBorder() {
//        val border = Paint(ANTI_ALIAS_FLAG).apply {
//            style = Style.STROKE
//            color = canvasBorderColor
//            strokeWidth = widthBorderCanvas // TODO: Ширина границы не изменяет общую длину отступа от края?
//            isDither = true
//            isAntiAlias = true
//        }
//
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)
//
//        Canvas(tempBitmap).drawRect(indentBorderCanvas, indentBorderCanvas, canvasWidth - indentBorderCanvas, canvasHeight - indentBorderCanvas, border)
//
//        bitmapHolstBorder = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), canvasHeight.toInt())
//    }
//
//    private fun drawMap() {
//        val map = Paint(ANTI_ALIAS_FLAG).apply {
//            style = Style.FILL
//            color = backgroundColorMap
//            isDither = true
//            isAntiAlias = true
//        }
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)
//
//        Canvas(tempBitmap).drawCircle(radiusMap, radiusMap, radiusMap, map)
//        bitmapMap = Bitmap.createBitmap(tempBitmap, 0, 0, (radiusMap * 2).toInt(), (radiusMap * 2).toInt())
//    }
//
//    private fun drawMapBorder() {
//        val mapBorder = Paint(ANTI_ALIAS_FLAG).apply {
//            style = Style.FILL
//            color = mapBorderColor
//            isDither = true
//            isAntiAlias = true
//        }
//
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)
//
//        Canvas(tempBitmap).drawCircle(radiusMap + widthBorderMap, radiusMap + widthBorderMap, radiusMap + widthBorderMap, mapBorder)
//
//        bitmapMapBorder = Bitmap.createBitmap(tempBitmap, 0, 0, ((radiusMap + widthBorderMap) * 2).toInt(), ((radiusMap + widthBorderMap) * 2).toInt())
//    }
//
//    private fun drawDesc() {
//        val descTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
//            textAlign = Align.CENTER
//            textSize = descTextSize
//            typeface = ResourcesCompat.getFont(activity.applicationContext, R.font.caveat_regular)
//            isDither = true
//            isAntiAlias = true
//        }
//
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)
//
//        val heightAllText = Canvas(tempBitmap).drawMultilineText(descText, descTextPaint, (canvasWidth/1.5).toInt() , canvasWidth/2, 0F)
//
//        bitmapDesc = Bitmap.createBitmap(tempBitmap, 0, 0, canvasWidth.toInt(), heightAllText)
//    }
//
//    private fun drawSeparator() {
//        val separator = Paint(ANTI_ALIAS_FLAG).apply {
//            isDither = true
//            isAntiAlias = true
//            strokeWidth = separatorHeight
//            color = separatorColor
//        }
//
//        val tempBitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)
//
//        Canvas(tempBitmap).drawLine(0F, 0F, separatorWidth, 0F, separator)
//
//        bitmapSeparator = Bitmap.createBitmap(tempBitmap, 0, 0, separatorWidth.toInt(), separatorHeight.toInt())
//    }
//
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
            //margin += indentBorderCanvas + widthBorderCanvas + (indentBorderCanvas*0.5).toFloat()
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
                bitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(), Bitmap.Config.ARGB_8888)

                val canvas = Canvas(bitmap)

                // Рисуем холст
                canvas.drawBitmap(bitmapHolst, 0F, 0F, Paint()) // TODO: Paint можно заменить на null

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
 //               canvas.drawBitmap(bitmapLocationText, 0F, canvasHeight - getBottomMarginLocationText(), Paint())

                activity.runOnUiThread {
                    listener?.onDraw()
                }

            }.start()

    }
}