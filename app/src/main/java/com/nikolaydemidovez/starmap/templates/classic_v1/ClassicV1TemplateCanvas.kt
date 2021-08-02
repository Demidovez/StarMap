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
import android.view.View
import android.webkit.*
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.pojo.*
import com.nikolaydemidovez.starmap.pojo.Graticule.Companion.LINE
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.getBitmapClippedCircle
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import android.webkit.WebView

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
    )

    private var isDataInitialized = false

    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapMapBorder: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap
    private lateinit var bitmapStarMap: Bitmap
    private val webViewStarMap = WebView(activity)
    private  var isLoadedStarMap = MutableLiveData(false)    // Загрузилась ли звездная карта с сервера

    init {
        holst.value =                           Holst("A4", "297 x 210 мм", 2480F, 3508F, "#FFFFFF" )
        hasBorderHolst.value =                  true
        borderHolst.value =                     HolstBorder(100F, 10F, "#000000")
        starMap.value =                         StarMap(1000F, "#000000")
        hasBorderMap.value =                    false
        starMapBorder.value =                   StarMapBorder(15F, "#FFFFFF", StarMapBorder.LINE)
        descFont.value =                        FontText("Comfortaa Regular", R.font.comfortaa_regular, "#000000", 120F)
        descText.value =                        "День, когда сошлись\nвсе звезды вселенной..."
        hasEventDateInLocation.value =          true
        eventDate.value =                       Calendar.getInstance().time
        hasEventTimeInLocation.value =          true
        eventTime.value =                       ""
        hasEventCityInLocation.value =          true
        eventLocation.value =                   "Москва"
        eventCountry.value =                    "Россия"
        hasEditResultLocationText.value =       false
        resultLocationText.value =              ""
        locationFont.value =                    FontText("Comfortaa Regular", R.font.comfortaa_regular, "#000000", 60F)
        hasEventCoordinatesInLocation.value =   true
        eventLatitude.value =                   55.755826
        eventLongitude.value =                  37.6173
        hasSeparator.value =                    true
        separator.value =                       Separator("#000000", 1000F)
        hasGraticule.value =                    true
        graticule.value =                       Graticule(2F, "#FFFFFF", 70, LINE)
        hasMilkyWay.value =                     true
        hasNames.value =                        false
        namesStars.value =                      NamesStars(12, "#FFFFFF", Lang("Русский", "ru"))
        hasConstellations.value =               true
        constellations.value =                  Constellations(3F, "#FFFFFF", 100)
        stars.value =                           Stars(12F, "#FFFFFF", 100)
    }

    init {
        CoroutineScope(Dispatchers.IO).launch  {
            launch { initStarMapBitmap() }

            val drawObjects = launch {
                launch { drawHolst() }
                launch { drawHolstBorder() }
                launch { drawMap() }
                launch { drawMapBorder() }
                launch { drawDesc() }
                launch { drawSeparator() }
                launch { correctLocationText() }
                launch { drawLocationText() }
            }

            drawObjects.join()
            drawCanvas()

            isDataInitialized = true
        }

        holst.observe(activity)  {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
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
        }
        hasBorderHolst.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawHolstBorder() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        borderHolst.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawHolstBorder() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        starMap.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
//                    requestStarMap()


                    val drawObjects = launch {
                        launch {  drawMap() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasGraticule.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    simpleRequestStarMap("showGraticule", it.toString())
                }
            }
        }
        graticule.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        hasMilkyWay.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        hasNames.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        namesStars.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        hasConstellations.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        constellations.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        stars.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap()
                }
            }
        }
        hasBorderMap.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawCanvas()
                }
            }
        }
        starMapBorder.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawMapBorder()
                    drawCanvas()
                }
            }
        }
        isLoadedStarMap.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawMap()

                    if(hasBorderMap.value!!) {
                        drawMapBorder()
                    }

                    drawCanvas()
                }
            }
        }
        descFont.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch {  drawDesc() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        descText.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawDesc() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasEventDateInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        eventDate.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                        launch { requestStarMap() }
                        launch { drawMap() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasEventTimeInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        eventTime.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                        launch { requestStarMap() }
                        launch { drawMap() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasEventCityInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        eventLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        locationFont.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch {  drawLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasEventCoordinatesInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        eventLatitude.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                        launch { requestStarMap() }
                        launch { drawMap() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        eventLongitude.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                        launch { requestStarMap() }
                        launch { drawMap() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        hasEditResultLocationText.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { correctLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        resultLocationText.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawLocationText() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        separator.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawSeparator() }
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
    }

    private fun initStarMapBitmap() {
        Log.d("MyLog", "Start initStarMapBitmap")

        val webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("Console", "${consoleMessage.message()} -- From line ${consoleMessage.lineNumber()} of ${consoleMessage.sourceId()}");
                return true
            }
        }

        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("Console", "onPageFinished")
                view!!.loadUrl("""
                   javascript: initStarMap(`${getConfigStarMap()}`);  androidCallback = (png) => {Android.callBackStarMap(png)};
                """)
            }
        }

        val handler = Handler(Looper.getMainLooper()) {
           val decodedString: ByteArray = Base64.decode(it.data.getString("png"), Base64.DEFAULT)
           bitmapStarMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

           isLoadedStarMap.postValue(true)

           true
        }

        activity.runOnUiThread(Runnable {
            webViewStarMap.loadUrl("file:///android_asset/starmap/templates/classic_v1/index.html")
            webViewStarMap.webChromeClient = webChromeClient
            webViewStarMap.webViewClient = webViewClient
            webViewStarMap.addJavascriptInterface(object {
                @JavascriptInterface
                fun callBackStarMap(png: String) {
                    val bundle = Bundle()
                    bundle.putString("png", png)

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
        })
        
        Log.d("MyLog", "Done initStarMapBitmap")
    }
    private fun requestStarMap() {
        Log.d("MyLog", "Start requestStarMap")

        activity.runOnUiThread(Runnable {
            webViewStarMap.loadUrl("""
                   javascript: initStarMap(`${getConfigStarMap()}`);
                """)
        })


        Log.d("MyLog", "Done requestStarMap")
    }
    private fun simpleRequestStarMap(funcName: String, value: String) {
        Log.d("MyLog", "Start requestStarMap")

        activity.runOnUiThread(Runnable {
            webViewStarMap.loadUrl("""
                   javascript: $funcName($value);
                """)
        })


        Log.d("MyLog", "Done requestStarMap")
    }
    private fun getConfigStarMap(): String {
        Log.d("MyLog", "Start getRequestBodyStarMap")

        val date = Calendar.getInstance()
        date.time = eventDate.value!!

        val year    = date.get(Calendar.YEAR)
        val month   = date.get(Calendar.MONTH)
        val day     = date.get(Calendar.DAY_OF_MONTH)
        val hourOfDay = date.get(Calendar.HOUR_OF_DAY)
        val minute    = date.get(Calendar.MINUTE)

        val hours   = if(eventTime.value!!.isNotEmpty()) eventTime.value!!.split(":")[0].toInt() else hourOfDay
        val minutes = if(eventTime.value!!.isNotEmpty()) eventTime.value!!.split(":")[1].toInt() else minute

        date.set(year, month, day, hours, minutes)

        val jsonString =
            """
                            {
                                "date": ${date.time.time},
                                "latitude": ${eventLatitude.value}, 
                                "longtitude" :${eventLongitude.value},
                                "width": ${starMap.value!!.radius},
                                "stars": {
                                    "size": ${stars.value!!.size!!},
                                    "exponent": -0.28,
                                    "dataPath": "http://62.75.195.219:3000/data/stars.6.json",
                                    "propername": ${hasNames.value},
                                    "propernamePath": "http://62.75.195.219:3000/data/starnames.json",
                                    "propernameLang": "${namesStars.value!!.lang!!.name}",
                                    "propernameStyle": {
                                        "fill": "${namesStars.value!!.color}",
                                        "font": "${namesStars.value!!.size}px Arial, Times, 'Times Roman', serif",
                                        "align": "right"
                                    },
                                    "propernameLimit": 2.5,
                                    "fill": "${stars.value!!.color}", 
                                    "opacity": ${stars.value!!.opacity!!.toFloat() / 100} 
                                },
                                "dsos": {
                                    "show": true,
                                    "limit": 6,
                                    "style": { "fill": "${stars.value!!.color}", "width": 2, "opacity": ${stars.value!!.opacity!!.toFloat() / 100}  }, 
                                    "size": ${stars.value!!.size!!},
                                    "names": ${hasNames.value},
                                    "nameLang": "${namesStars.value!!.lang!!.name}",
                                    "namePath": "http://62.75.195.219:3000/data/dsonames.json",
                                    "nameStyle": {
                                        "fill": "${namesStars.value!!.color}",
                                        "font": "${namesStars.value!!.size}px Helvetica, Arial, serif",
                                        "align": "left"
                                    },
                                    "nameLimit": 4,
                                    "exponent": -0.28,
                                    "dataPath": "http://62.75.195.219:3000/data/dsos.bright.json"
                                },
                                "planets": {
                                    "show": true,
                                    "which": [ "sol", "mer", "ven", "ter", "lun", "mar", "jup", "sat", "ura", "nep", "cer", "plu"],
                                    "style": {
                                      "sol": { "fill": "#ffff00", "size": 12 },
                                      "mer": { "fill": "#cccccc" },
                                      "ven": { "fill": "#eeeecc" },
                                      "ter": { "fill": "#00ccff" },
                                      "lun": { "fill": "#ffffff", "size": 12 },
                                      "mar": { "fill": "#ff6600" },
                                      "cer": { "fill": "#cccccc" },
                                      "ves": { "fill": "#cccccc" },
                                      "jup": { "fill": "#ffaa33" },
                                      "sat": { "fill": "#ffdd66" },
                                      "ura": { "fill": "#66ccff" },
                                      "nep": { "fill": "#6666ff" },
                                      "plu": { "fill": "#aaaaaa" },
                                      "eri": { "fill": "#eeeeee" }
                                    },
                                    "dataPath": "http://62.75.195.219:3000/data/planets.json",
                                    "names": ${hasNames.value},
                                    "nameLang": "${namesStars.value!!.lang!!.name}",
                                    "nameStyle": {
                                        "fill": "${namesStars.value!!.color}",
                                        "font": "${namesStars.value!!.size}px 'Lucida Sans Unicode', Consolas, sans-serif",
                                        "align": "right"
                                    }
                                },
                                "constellations": {
                                    "show": ${hasConstellations.value},
                                    "dataPath": "http://62.75.195.219:3000/data/constellations.lines.json",
                                    "style": {
                                      "stroke": "${constellations.value!!.color}", 
                                      "strokeWidth": ${constellations.value!!.width},
                                      "opacity": ${constellations.value!!.opacity!!.toFloat() / 100} 
                                    },
                                    "names": ${hasNames.value},
                                    "nameLang": "${namesStars.value!!.lang!!.name}",
                                    "namePath": "http://62.75.195.219:3000/data/constellations.json",
                                    "nameStyle": {
                                        "fill": "${namesStars.value!!.color}",
                                        "align": "center",
                                        "baseline": "middle",
                                        "opacity": 0.8,
                                        "font": "${namesStars.value!!.size}px Helvetica, Arial, sans-serif"
                                    }
                                },
                                "mw": {
                                    "show": ${hasMilkyWay.value},
                                    "style": { "fill": "#ffffff", "opacity": "0.15" },
                                    "dataPath": "http://62.75.195.219:3000/data/mw.json"
                                },
                                "graticule": {
                                    "show": ${hasGraticule.value},
                                    "style": {
                                        "stroke": "${graticule.value!!.color}",
                                        "strokeWidth": ${graticule.value!!.width},
                                        "opacity": ${graticule.value!!.opacity!!.toFloat() / 100}
                                    }
                                }
                            }       
            """.trimIndent()

        Log.d("MyLog", "Done getRequestBodyStarMap")
        return jsonString
    }
    private fun correctLocationText() {
        Log.d("MyLog", "Start correctLocationText")

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

            resultLocationText.postValue(locationText.trim())
        }

        Log.d("MyLog", "Done correctLocationText")
    }
    private fun drawHolst() {
        Log.d("MyLog", "Start drawHolst")

        val holstPaint = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(holst.value!!.color)
            isDither = true
            isAntiAlias = true
        }

        bitmapHolst = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(bitmapHolst).drawRect(0F, 0F, holst.value!!.width!!, holst.value!!.height!!, holstPaint)

        Log.d("MyLog", "Done drawHolst")
    }
    private fun drawHolstBorder() {
        Log.d("MyLog", "Start drawHolstBorder")

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

        Log.d("MyLog", "Done drawHolstBorder")
    }
    private fun drawMap() {
        Log.d("MyLog", "Start drawMap")

        var tempBitmap: Bitmap

        if(isLoadedStarMap.value!!) {
            tempBitmap = Bitmap.createScaledBitmap(bitmapStarMap, (starMap.value!!.radius!! * 2).toInt(), (starMap.value!!.radius!! * 2).toInt(), true)
            tempBitmap = getBitmapClippedCircle(tempBitmap)!!

            val x = (tempBitmap.width / 2) - starMap.value!!.radius!!
            val y = (tempBitmap.height / 2) - starMap.value!!.radius!!
            val width = (starMap.value!!.radius!! * 2).toInt()

            tempBitmap = Bitmap.createBitmap(tempBitmap, x.toInt(), y.toInt(), width, width)
        } else {
            tempBitmap = getLoadingBitmap()
        }

        val newBitmap = Bitmap.createBitmap(tempBitmap.width, tempBitmap.height, tempBitmap.config)

        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.parseColor(starMap.value!!.color))
        canvas.drawBitmap(tempBitmap, 0f, 0f, null)

        bitmapMap = getBitmapClippedCircle(newBitmap)!!

        Log.d("MyLog", "Done drawMap")
    }
    private fun drawMapBorder() {
        Log.d("MyLog", "Start drawMapBorder")

        val mapBorder = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(starMapBorder.value!!.color)
            isDither = true
            isAntiAlias = true
        }

        val tempSize = (starMap.value!!.radius!! + starMapBorder.value!!.width!!) * 2

        val tempBitmap = Bitmap.createBitmap(tempSize.toInt(), tempSize.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawCircle(starMap.value!!.radius!! + starMapBorder.value!!.width!!, starMap.value!!.radius!! + starMapBorder.value!!.width!!, starMap.value!!.radius!! + starMapBorder.value!!.width!!, mapBorder)

        bitmapMapBorder = Bitmap.createBitmap(tempBitmap, 0, 0, ((starMap.value!!.radius!! + starMapBorder.value!!.width!!) * 2).toInt(), ((starMap.value!!.radius!! + starMapBorder.value!!.width!!) * 2).toInt())

        Log.d("MyLog", "Done drawMapBorder")
    }
    private fun drawDesc() {
        Log.d("MyLog", "Start drawDesc")

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

        Log.d("MyLog", "Done drawDesc")
    }
    private fun drawSeparator() {
        Log.d("MyLog", "Start drawSeparator")

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

        Log.d("MyLog", "Done drawSeparator")
    }
    private fun drawLocationText() {
        Log.d("MyLog", "Start drawLocationText")

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

        Log.d("MyLog", "Done drawLocationText")
    }
    private fun getBottomMarginLocationText(): Int {
        Log.d("MyLog", "Start getBottomMarginLocationText")

        var margin = 0F

        margin = margin.plus(
            if(hasBorderHolst.value!!) {
                borderHolst.value!!.indent!! + borderHolst.value!!.width!! + (borderHolst.value!!.indent!!*0.5).toFloat()
            } else {
                (bitmapLocationText.height * 0.5).toFloat()
            }
        )

        Log.d("MyLog", "Done getBottomMarginLocationText")
        return margin.toInt()
    }
    private fun getAbsoluteHeightMap(): Float {
        Log.d("MyLog", "Start getAbsoluteHeightMap")

        // Определяем высоту карты (с рамкой и без) с ее верхним отступом от края холста
        var margin = 0F

        margin += if(hasBorderMap.value!!) {
            holst.value!!.width!!/2 + bitmapMapBorder.height / 2
        } else {
            holst.value!!.width!!/2 + bitmapMap.height / 2
        }

        Log.d("MyLog", "Done getAbsoluteHeightMap")
        return margin
    }
    private fun getAutoAlignMargin(): Float {
        Log.d("MyLog", "Start getAutoAlignMargin")

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

        Log.d("MyLog", "Done getAutoAlignMargin")
        return (holst.value!!.height!! - heightAllObjects) / countObjectsForSetMargin
    }
    private fun getLoadingBitmap(): Bitmap {
        Log.d("MyLog", "Start getLoadingBitmap")

        val map = Paint(ANTI_ALIAS_FLAG).apply {
            style = Style.FILL
            color = Color.parseColor(starMap.value!!.color!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val loadingCanvas = Canvas(tempBitmap)

        loadingCanvas.drawCircle(starMap.value!!.radius!!, starMap.value!!.radius!!, starMap.value!!.radius!!, map)

        val widthLoadingIcon = starMap.value!!.radius!! * 0.5
        val heightLoadingIcon = starMap.value!!.radius!! * 0.5
        val drawableLoadingIcon = ContextCompat.getDrawable(activity, R.drawable.ic_loading_map)!!
        val wrappedDrawable = DrawableCompat.wrap(drawableLoadingIcon)
        DrawableCompat.setTint(wrappedDrawable, if (starMap.value!!.color!! == "#FFFFFF") Color.BLACK else Color.WHITE)
        val bitmapLoadingIcon = wrappedDrawable.toBitmap(widthLoadingIcon.toInt(), heightLoadingIcon.toInt())

        loadingCanvas.drawBitmap(bitmapLoadingIcon, (starMap.value!!.radius!! - widthLoadingIcon / 2).toFloat(), (starMap.value!!.radius!! - heightLoadingIcon / 2).toFloat(), null)

        Log.d("MyLog", "Done getLoadingBitmap")
        return Bitmap.createBitmap(tempBitmap, 0, 0, (starMap.value!!.radius!! * 2).toInt(), (starMap.value!!.radius!! * 2).toInt())
    }
    override fun drawCanvas() {
        Log.d("MyLog", "Start drawCanvas")

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

        doneRedraw.postValue(true)

        Log.d("MyLog", "Done drawCanvas")
    }
    override fun getControllerList(): ArrayList<Controller> {
        Log.d("MyLog", "Start getControllerList")
        return controllerList
    }
}