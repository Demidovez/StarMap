package com.nikolaydemidovez.starmap.templates.classic_v1

import android.graphics.*
import android.graphics.Paint.*
import kotlinx.coroutines.*
import android.text.TextPaint
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.extensions.drawMultilineText
import java.util.*
import androidx.core.graphics.drawable.DrawableCompat

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Base64
import android.util.Log
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.pojo.*
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.getBitmapClippedCircle
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList


class ClassicV1TemplateCanvas(private val activity: MainActivity) : TemplateCanvas(activity) {
    private val controllerList = arrayListOf(
        Controller("event_v1",     "Событие",     ContextCompat.getDrawable(activity,R.drawable.ic_event_v1)),
        Controller("canvas_v1",    "Холст",       ContextCompat.getDrawable(activity,R.drawable.ic_canvas_v1)),
        Controller("map_v1",       "Карта",       ContextCompat.getDrawable(activity,R.drawable.ic_map_v1)),
        Controller("stars_v1",     "Звезды",      ContextCompat.getDrawable(activity,R.drawable.ic_stars_v1)),
        Controller("desc_v1",      "Текст",       ContextCompat.getDrawable(activity,R.drawable.ic_desc_v1)),
        Controller("separator_v1", "Разделитель", ContextCompat.getDrawable(activity,R.drawable.ic_separator_v1)),
        Controller("location_v1",  "Локация",     ContextCompat.getDrawable(activity,R.drawable.ic_location_v1)),
        Controller("save_v1",      "Сохранение",  ContextCompat.getDrawable(activity,R.drawable.ic_save_v1)),
    );

    private var isDataInitialized = false
    private var isLoadedStarMap = MutableLiveData<Boolean>()    // Загрузилась ли звездная карта с сервера

    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapMapBorder: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap
    private lateinit var bitmapStarMap: Bitmap
    private lateinit var webViewStarMap: WebView

    init {
        initStarMapBitmap()

        val date = Calendar.getInstance()

        val hourOfDay = date.get(Calendar.HOUR_OF_DAY)
        val minute    = date.get(Calendar.MINUTE)

        holst.value                                     = Holst("A4", "297 x 210 мм", 2480F, 3508F, "#FFFFFF" )
        hasBorderHolst.value                            = true
        borderHolst.value                               = HolstBorder(100F, 10F, "#000000")
        backgroundColorMap.value                        = "#000000"
        radiusMap.value                                 = 1000F
        hasBorderMap.value                              = false
        widthBorderMap.value                            = 15F
        mapBorderColor.value                            = Color.parseColor("#FFFFFF")
        descFont.value                                  = FontText("Comfortaa Regular", R.font.comfortaa_regular, "#000000", 120F)
        descText.value                                  = "День, когда сошлись\nвсе звезды вселенной..."
        hasEventDateInLocation.value                    = true
        eventDate.value                                 = Calendar.getInstance().time
        hasEventTimeInLocation.value                    = true
        eventTime.value                                 = "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        hasEventCityInLocation.value                    = true
        eventLocation.value                             = "Москва"
        eventCountry.value                              = "Россия"
        hasEditResultLocationText.value                 = false
        resultLocationText.value                        = ""
        locationFont.value                              = FontText("Comfortaa Regular", R.font.comfortaa_regular, "#000000", 60F)
        hasEventCoordinatesInLocation.value             = true
        eventLatitude.value                             = 55.755826
        eventLongitude.value                            = 37.6173
        hasSeparator.value                              = true
        separator.value                                 = Separator("#000000", 1000F)
        isLoadedStarMap.value                           = true

        holst.observe(activity)  {
            runBlocking  {
                val drawObjects = launch {
                    launch { drawHolst() }
                    launch { drawHolstBorder() }
                    launch { drawDesc() }
                    launch { drawLocationText() }
                }

                drawObjects.join()
                drawCanvas()
            }
        }
        hasBorderHolst.observe(activity) {
            drawHolstBorder()
        }
        borderHolst.observe(activity) {
            drawHolstBorder()
        }
        backgroundColorMap.observe(activity) {
            requestStarMap()
        }
        radiusMap.observe(activity) {
            requestStarMap()
        }
        hasBorderMap.observe(activity) {
            requestStarMap()
            drawMap()
        }
        widthBorderMap.observe(activity) {
            drawMap()
        }
        mapBorderColor.observe(activity) {
            drawMap()
        }
        isLoadedStarMap.observe(activity) {
            drawMap()
        }
        descFont.observe(activity) {
            drawDesc()
        }
        descText.observe(activity) {
            drawDesc()
        }
        hasEventDateInLocation.observe(activity) {
            correctLocationText()
        }
        eventDate.observe(activity) {
            correctLocationText()
            requestStarMap()
            drawMap()
        }
        hasEventTimeInLocation.observe(activity) {
            correctLocationText()
        }
        eventTime.observe(activity) {
            correctLocationText()
            requestStarMap()
            drawMap()
        }
        hasEventCityInLocation.observe(activity) {
            correctLocationText()
        }
        eventLocation.observe(activity) {
            correctLocationText()
        }
        locationFont.observe(activity) {
            drawLocationText()
        }
        hasEventCoordinatesInLocation.observe(activity) {
            correctLocationText()
        }
        eventLatitude.observe(activity) {
            correctLocationText();
            requestStarMap()
            drawMap()
        }
        eventLongitude.observe(activity) {
            correctLocationText();
            requestStarMap()
            drawMap()
        }
        hasEditResultLocationText.observe(activity) {
            correctLocationText()
        }
        resultLocationText.observe(activity) {
            drawLocationText()
        }
        separator.observe(activity) {
            drawSeparator()
        }
    }

    private fun initStarMapBitmap() {
        val webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("MyLog", "${consoleMessage.message()} -- From line ${consoleMessage.lineNumber()} of ${consoleMessage.sourceId()}");
                return true
            }
        }

        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                view!!.loadUrl("javascript:Celestial.addCallback(() => Android.callBackUpdateStarMap(document.querySelector('canvas').toDataURL('image/png').replace('data:image/png;base64,', '')));")
            }
        }

        val handler = Handler(Looper.getMainLooper()) {
            val decodedString: ByteArray = Base64.decode(it.data.getString("png"), Base64.DEFAULT)
            bitmapStarMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            true
        }

        webViewStarMap = WebView(activity)
        webViewStarMap.webChromeClient = webChromeClient
        webViewStarMap.webViewClient = webViewClient
        webViewStarMap.addJavascriptInterface(object {
            @JavascriptInterface
            fun callBackUpdateStarMap(pngString: String) {
                val bundle = Bundle()
                bundle.putString("png", pngString)

                val message = Message()
                message.data = bundle

                handler.sendMessage(message)
            }
        }, "Android")

        webViewStarMap.settings.apply {
            javaScriptEnabled = true
            allowFileAccess = true
            allowContentAccess = true
        }

        webViewStarMap.loadUrl("file:///android_asset/starmap/templates/classic_v1/index.html")
        Log.d("MyLog", "initStarMapBitmap")
    }

    private fun requestStarMap() {
        Log.d("MyLog", "requestStarMap")
        webViewStarMap.loadUrl("""javascript:window.editStartMap(`${getRequestBodyStarMap()}`);""")
    }

    private fun getRequestBodyStarMap(): String {
        val date = Calendar.getInstance()
        date.time = eventDate.value!!

        val year    = date.get(Calendar.YEAR)
        val month   = date.get(Calendar.MONTH)
        val day     = date.get(Calendar.DAY_OF_MONTH)
        val hours   = eventTime.value!!.split(":")[0].toInt()
        val minutes = eventTime.value!!.split(":")[1].toInt()

        date.set(year, month, day, hours, minutes)

        val jsonString =
            """
                {
                    "date": ${date.time.time},
                    "location": [${eventLatitude.value}, ${eventLongitude.value}],
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

        return jsonString
    }

    private fun correctLocationText() {
        if(!hasEditResultLocationText.value!!) {
            var locationText = ""

            if(hasEventDateInLocation.value!!)
                locationText = "$locationText ${SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(eventDate.value!!)}"

            if(hasEventDateInLocation.value!! && hasEventTimeInLocation.value!!)
                locationText = "$locationText,"

            if(hasEventTimeInLocation.value!!) {
                locationText = "${locationText}${eventTime.value!!}\n"
            } else if(hasEventDateInLocation.value!!) {
                locationText = "$locationText\n"
            }

            if(hasEventCityInLocation.value!!) {
                locationText = if(eventCountry.value!!.isNotEmpty()) {
                    "${locationText}г. ${eventLocation.value}, ${eventCountry.value}\n"
                } else {
                    "${locationText}г. ${eventLocation.value}\n"
                }
            }

            if(hasEventCoordinatesInLocation.value!!)
                locationText = "${locationText}${Helper.convert(eventLatitude.value!!, eventLongitude.value!!)}"

            resultLocationText.value = locationText.trim()
        }
    }

    private fun drawHolst() {
        val holstPaint = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(holst.value!!.color)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(0F, 0F, holst.value!!.width!!, holst.value!!.height!!, holstPaint)

        bitmapHolst = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt())
    }

    private fun drawHolstBorder() {
        val border = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.STROKE
            color = Color.parseColor(borderHolst.value!!.color)
            strokeWidth = borderHolst.value!!.width!!  //TODO: Ширина границы не изменяет общую длину отступа от края?
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(borderHolst.value!!.indent!!, borderHolst.value!!.indent!!, holst.value!!.width!!.toInt() - borderHolst.value!!.indent!!, holst.value!!.height!!.toInt() - borderHolst.value!!.indent!!, border)

        bitmapHolstBorder = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt())
    }

    private fun drawMap() {
        var tempBitmap: Bitmap

        if(isLoadedStarMap.value!!) {
            tempBitmap = Bitmap.createScaledBitmap(bitmapStarMap, (radiusMap.value!! * 2).toInt(), (radiusMap.value!! * 2).toInt(), true)
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

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(radiusMap.value!! + widthBorderMap.value!!, radiusMap.value!! + widthBorderMap.value!!, radiusMap.value!! + widthBorderMap.value!!, mapBorder)

        bitmapMapBorder = Bitmap.createBitmap(tempBitmap, 0, 0, ((radiusMap.value!! + widthBorderMap.value!!) * 2).toInt(), ((radiusMap.value!! + widthBorderMap.value!!) * 2).toInt())
    }

    private fun drawDesc() {
        val descTextPaint = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = descFont.value!!.size!!
            typeface = ResourcesCompat.getFont(activity.applicationContext, descFont.value!!.resId!!)
            color = Color.parseColor(descFont.value!!.color!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = descText.value!!.split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, descTextPaint, (holst.value!!.width!!/1.3).toInt() , holst.value!!.width!!/2, totalHeightText)
        }

        bitmapDesc = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), totalHeightText.toInt())
    }

    private fun drawSeparator() {
        val separatorHeight = (separator.value!!.width!! * 0.01F).coerceAtLeast(1F)

        val separatorLine = Paint(ANTI_ALIAS_FLAG).apply {
            isDither = true
            isAntiAlias = true
            strokeWidth = separatorHeight
            color = Color.parseColor(separator.value!!.color!!)
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawLine(0F, 0F, separator.value!!.width!!, 0F, separatorLine)

        bitmapSeparator = Bitmap.createBitmap(tempBitmap, 0, 0, separator.value!!.width!!.toInt(), separatorHeight.toInt())
    }

    private fun drawLocationText() {
        val eventLocation = TextPaint(ANTI_ALIAS_FLAG).apply {
            textAlign = Align.CENTER
            textSize = locationFont.value!!.size!!
            color = Color.parseColor(locationFont.value!!.color)
            typeface = ResourcesCompat.getFont(activity, locationFont.value!!.resId!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = resultLocationText.value!!.split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, eventLocation, holst.value!!.width!!.toInt() , holst.value!!.width!!/2,  totalHeightText)
        }

        bitmapLocationText = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), totalHeightText.toInt())
    }

    private fun getBottomMarginLocationText(): Int {
        var margin = 0F

        margin = margin.plus(
            if(hasBorderHolst.value!!) {
                borderHolst.value!!.indent!! + borderHolst.value!!.width!! + (borderHolst.value!!.indent!!*0.5).toFloat()
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
            holst.value!!.width!!/2 + bitmapMapBorder.height / 2
        } else {
            holst.value!!.width!!/2 + bitmapMap.height / 2
        }

        return margin
    }

    private fun getAutoAlignMargin(): Float {
        // Определяем какой отступ нужен для текста, разделителя и текста локации, чтобы они ровно расположились
        var heightAllObjects = holst.value!!.width!! / 2

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

        return (holst.value!!.height!! - heightAllObjects) / countObjectsForSetMargin
    }

    private fun getLoadingBitmap(): Bitmap {
        val map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(backgroundColorMap.value!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

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

    override fun drawCanvas() {
        Thread {
            bitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

            val canvas = Canvas(bitmap)

            // Рисуем холст
            canvas.drawBitmap(bitmapHolst, 0F, 0F, null)

            // Рисуем рамку холста
            if(hasBorderHolst.value!!) {
                canvas.drawBitmap(bitmapHolstBorder, 0F, 0F, null)
            }

            // Рисуем рамку карты
            if(hasBorderMap.value!!) {
                canvas.drawBitmap(bitmapMapBorder, holst.value!!.width!!/2 - bitmapMapBorder.width / 2, holst.value!!.width!!/2 - bitmapMapBorder.width / 2, null)
            }

            // Рисуем карту
            canvas.drawBitmap(bitmapMap, holst.value!!.width!!/2 - bitmapMap.width / 2, holst.value!!.width!!/2 - bitmapMap.width / 2, null)

            val autoMargin = getAutoAlignMargin()

            // Рисуем текст описание
            canvas.drawBitmap(bitmapDesc, 0F, getAbsoluteHeightMap() + autoMargin, null)

            // Рисуем разделитель
            if(hasSeparator.value!!) {
                canvas.drawBitmap(bitmapSeparator, (holst.value!!.width!! - separator.value!!.width!!) / 2, getAbsoluteHeightMap() + autoMargin + bitmapDesc.height + autoMargin, null)
            }

            // Рисуем текст локации
            canvas.drawBitmap(bitmapLocationText, 0F, holst.value!!.height!! - bitmapLocationText.height - getBottomMarginLocationText(), null)

            activity.runOnUiThread {
                listener?.onDraw()
            }

        }.start()
    }

    override fun getControllerList(): ArrayList<Controller> {
        return controllerList
    }
}