package com.nikolaydemidovez.starmap.templates.classic_v1

import android.graphics.*
import android.graphics.Paint.*
import android.text.TextPaint
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText
import java.util.*
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.drawable.Drawable
import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.nikolaydemidovez.starmap.retrofit.common.Common
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.getBitmapClippedCircle
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ClassicV1TemplateCanvas(private val activity: MainActivity) : TemplateCanvas(activity) {
    private var isDataInitialized = false
    private var isLoadedStarMap                         = MutableLiveData<Boolean>()    // Загрузилась ли звездная карта с сервера

    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapMapBorder: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap
    private var bitmapStarMap: Bitmap? = null

    init {
        canvasWidth.value                               = 2480F
        canvasHeight.value                              = 3508F
        backgroundColorCanvas.value                     = Color.parseColor("#FFFFFF")
        canvasBorderColor.value                         = Color.parseColor("#000000")
        hasBorderCanvas.value                           = true
        indentBorderCanvas.value                        = 100F
        widthBorderCanvas.value                         = 10F
        backgroundColorMap.value                        = "#000000"
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

        canvasWidth.observe(activity,                   { redraw(arrayOf(::drawHolst, ::drawHolstBorder, ::drawDesc, ::drawLocationText)) })
        canvasHeight.observe(activity,                  { redraw(arrayOf(::drawHolst, ::drawHolstBorder, ::drawDesc, ::drawLocationText)) })
        backgroundColorCanvas.observe(activity,         { redraw(arrayOf(::drawHolst)) })
        canvasBorderColor.observe(activity,             { redraw(arrayOf(::drawHolstBorder)) })
        hasBorderCanvas.observe(activity,               { redraw(arrayOf(::drawHolstBorder)) })
        indentBorderCanvas.observe(activity,            { redraw(arrayOf(::drawHolstBorder)) })
        widthBorderCanvas.observe(activity,             { redraw(arrayOf(::drawHolstBorder)) })
        backgroundColorMap.observe(activity,            { redraw(arrayOf(::requestStarMap)) })
        radiusMap.observe(activity,                     { redraw(arrayOf(::requestStarMap)) })
        hasBorderMap.observe(activity,                  { redraw(arrayOf(::requestStarMap, ::drawMap)) })
        widthBorderMap.observe(activity,                { redraw(arrayOf(::drawMap)) })
        mapBorderColor.observe(activity,                { redraw(arrayOf(::drawMap)) })
        isLoadedStarMap.observe(activity,               { redraw(arrayOf(::drawMap)) })
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
        Thread {
            requestStarMap()
            drawHolst()
            drawHolstBorder()
            drawMap()
            drawMapBorder()
            drawDesc()
            drawSeparator()
            drawLocationText()
            draw()

            isDataInitialized = true
        }.start()
    }

    private fun requestStarMap() {
        bitmapStarMap = null

        Common.retrofitService.getClassicV1Map(getRequestBodyStarMap()).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {}

            override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                val bodyBytes = response?.body()!!.bytes()

                bitmapStarMap = BitmapFactory.decodeByteArray(bodyBytes, 0, bodyBytes.size)

                isLoadedStarMap.value = true
            }
        })
    }

    private fun getRequestBodyStarMap(): RequestBody {
        val jsonString =
            """
                {
                    "date": 1626283443816,
                    "location": [25.1, 25.1],
                    "width": 1000,
                    "options": {
                        "background": {
                            "fill": "${backgroundColorMap.value}"
                        },
                        "stars": {
                            "propername": false,
                            "propernameType": "ru",
                            "propernameStyle": {
                                "fill": "#ddddbb",
                                "font": "13px Arial, Times, 'Times Roman', serif",
                                "align": "right",
                                "baseline": "bottom"
                            },
                            "style": { 
                                "fill": "#ffffff", 
                                "opacity": 1 
                            },
                            "size": 12
                        },
                        "dsos": {
                            "names": false,
                            "namesType": "ru",
                            "nameStyle": {
                                "fill": "#000000",
                                "font": "11px Helvetica, Arial, serif",
                                "align": "left",
                                "baseline": "top"
                            }
                        },
                        "planets": {
                            "show": true,
                            "names": false,
                            "namesType": "ru",
                            "nameStyle": {
                                "fill": "#00ccff",
                                "font": "14px 'Lucida Sans Unicode', Consolas, sans-serif",
                                "align": "right",
                                "baseline": "top"
                            }
                        },
                        "constellations": {
                            "names": false,
                            "namesType": "ru",
                            "nameStyle": {
                                "fill": "#cccc99",
                                "align": "center",
                                "baseline": "middle",
                                "font": [
                                    "14px Helvetica, Arial, sans-serif",
                                    "12px Helvetica, Arial, sans-serif",
                                    "11px Helvetica, Arial, sans-serif"
                                ]
                            },
                            "lineStyle": { 
                                "stroke": "#FFFFFF", 
                                "width": 3, 
                                "opacity": 1 
                            }
                        },
                        "mw": {
                            "show": true
                        },
                        "lines": {
                            "graticule": {
                                "show": true,
                                "stroke": "#cccccc",
                                "dash": [5, 5],
                                "width": 0.6,
                                "opacity": 0.8
                            }
                        }
                    }
                }       
            """

        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString)
    }

    private fun redraw(callbacks: Array<() -> Unit?>?) {
        Thread {
            if(isDataInitialized) {
                callbacks?.forEach { it() }

                draw()
            }
        }.start()
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
        var tempBitmap: Bitmap

        if(bitmapStarMap != null) {
            tempBitmap = Bitmap.createScaledBitmap(bitmapStarMap!!, (radiusMap.value!! * 2).toInt(), (radiusMap.value!! * 2).toInt(), true)
            tempBitmap = getBitmapClippedCircle(tempBitmap)!!

            val x = (tempBitmap.width / 2) - radiusMap.value!!
            val y = (tempBitmap.height / 2) - radiusMap.value!!
            val width = (radiusMap.value!! * 2).toInt()

            bitmapMap = Bitmap.createBitmap(tempBitmap, x.toInt(), y.toInt(), width, width)
        } else {
            bitmapMap = getLoadingBitmap()
        }
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
        var margin = 0F

        margin = margin.plus(
            if(hasBorderCanvas.value!!) {
                indentBorderCanvas.value!! + widthBorderCanvas.value!! + (indentBorderCanvas.value!!*0.5).toFloat()
            } else {
                (bitmapLocationText.height * 0.5).toFloat()
            }
        )


        return margin.toInt()
    }

    private fun getAbsoluteHeightMap(): Float {
        // Определяем высоту карты (с рамкой и без) с ее верхним отступом от края холста
        var margin = 0F

        margin += if(hasBorderMap.value!!) {
            canvasWidth.value!!/2 + bitmapMapBorder.height / 2
        } else {
            canvasWidth.value!!/2 + bitmapMap.height / 2
        }

        return margin
    }

    private fun getAutoAlignMargin(): Float {
        // Определяем какой отступ нужен для текста, разделителя и текста локации, чтобы они ровно расположились
        var heightAllObjects = canvasWidth.value!!.toFloat() / 2

        heightAllObjects += if(hasBorderMap.value!!) {
            bitmapMapBorder.height.toFloat() / 2
        } else {
            bitmapMap.height.toFloat() / 2
        }

        heightAllObjects += bitmapDesc.height

        if(hasSeparator.value!!) {
            heightAllObjects += bitmapSeparator.height
        }

        heightAllObjects += bitmapLocationText.height + getBottomMarginLocationText()

        var countObjectsForSetMargin = 0

        countObjectsForSetMargin = if(hasSeparator.value!!) 3  else 2

        return (canvasHeight.value!! - heightAllObjects) / countObjectsForSetMargin
    }

    private fun getLoadingBitmap(): Bitmap {
        val map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(backgroundColorMap.value!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(), Bitmap.Config.ARGB_8888)

        val loadingCanvas = Canvas(tempBitmap)

        loadingCanvas.drawCircle(radiusMap.value!!, radiusMap.value!!, radiusMap.value!!, map)

        val widthLoadingIcon = radiusMap.value!! * 0.5
        val heightLoadingIcon = radiusMap.value!! * 0.5
        val drawableLoadingIcon = ContextCompat.getDrawable(activity, R.drawable.ic_loading_map)!!
        val wrappedDrawable = DrawableCompat.wrap(drawableLoadingIcon)
        DrawableCompat.setTint(wrappedDrawable, if (backgroundColorMap.value!! == "#FFFFFF") Color.BLACK else Color.WHITE)
        val bitmapLoadingIcon = wrappedDrawable.toBitmap(widthLoadingIcon.toInt(), heightLoadingIcon.toInt())

        loadingCanvas.drawBitmap(bitmapLoadingIcon, (radiusMap.value!! - widthLoadingIcon / 2).toFloat(), (radiusMap.value!! - heightLoadingIcon / 2).toFloat(), null)

        return Bitmap.createBitmap(tempBitmap, 0, 0, (radiusMap.value!! * 2).toInt(), (radiusMap.value!! * 2).toInt())
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

            val autoMargin = getAutoAlignMargin()

            // Рисуем текст описание
            canvas.drawBitmap(bitmapDesc, 0F, getAbsoluteHeightMap() + autoMargin, null)

            // Рисуем разделитель
            if(hasSeparator.value!!) {
                canvas.drawBitmap(bitmapSeparator, (canvasWidth.value!! - separatorWidth.value!!) / 2, getAbsoluteHeightMap() + autoMargin + bitmapDesc.height + autoMargin, null)
            }

            // Рисуем текст локации
            canvas.drawBitmap(bitmapLocationText, 0F, canvasHeight.value!! - bitmapLocationText.height - getBottomMarginLocationText(), null)

            activity.runOnUiThread {
                listener?.onDraw()
            }

        }.start()
    }
}