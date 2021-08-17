package com.kalambur.mappy_stars.templates.full_v1

import android.graphics.*
import android.net.Uri
import android.os.*
import android.text.TextPaint
import android.util.Base64
import android.util.Log
import android.webkit.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.MutableLiveData
import androidx.webkit.WebViewAssetLoader
import com.kalambur.mappy_stars.MainActivity
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.pojo.*
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.extensions.drawMultilineText
import com.kalambur.mappy_stars.utils.helpers.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Float.max
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.sqrt

class FullV1TemplateCanvas(private val activity: MainActivity, private val properties: Template) : TemplateCanvas(activity) {
    private val controllerList = arrayListOf(
        Controller("event_v1","Событие", ContextCompat.getDrawable(activity, R.drawable.ic_event_v1)),
        Controller("canvas_v1", "Холст", ContextCompat.getDrawable(activity, R.drawable.ic_canvas_v1)),
        Controller("map_v4", "Карта", ContextCompat.getDrawable(activity, R.drawable.ic_map_v1)),
        Controller("stars_v1", "Звезды", ContextCompat.getDrawable(activity, R.drawable.ic_stars_v1)),
        Controller("desc_v1", "Текст", ContextCompat.getDrawable(activity, R.drawable.ic_desc_v1)),
        Controller("separator_v1", "Разделитель", ContextCompat.getDrawable(activity, R.drawable.ic_separator_v1)),
        Controller("location_v1", "Локация", ContextCompat.getDrawable(activity, R.drawable.ic_location_v1)),
        Controller("save_v1", "Сохранение", ContextCompat.getDrawable(activity, R.drawable.ic_save_v1)),
    )

    private var isDataInitialized = false

    private lateinit var bitmapHolst: Bitmap
    private lateinit var bitmapHolstBorder: Bitmap
    private lateinit var bitmapMap: Bitmap
    private lateinit var bitmapTextBlock: Bitmap
    private lateinit var bitmapDesc: Bitmap
    private lateinit var bitmapSeparator: Bitmap
    private lateinit var bitmapLocationText: Bitmap
    private lateinit var bitmapStarMap: Bitmap
    private val webViewStarMap = WebView(activity)
    private  var isLoadedStarMap = MutableLiveData(false)    // Загрузилась ли звездная карта с сервера

    init {
        title =                                 properties.title
        category =                              properties.category
        name =                                  properties.name
        type =                                  if(properties.type == "default") DEFAULT else CUSTOM
        templateId =                            properties.id
        image =                                 properties.image

        holst.value =                           Holst("A4", "297 x 210 мм", properties.holstWidth, properties.holstHeight)
        holstColor.value =                      properties.holstColor // #16A085
        hasBorderHolst.value =                  properties.hasBorderHolst
        borderHolst.value =                     HolstBorder(properties.borderHolstIndent, properties.borderHolstWidth)
        borderHolstColor.value =                properties.borderHolstColor
        starMapColor.value =                    properties.starMapColor
        descFont.value =                        FontText(properties.descFontName, properties.descFontResId, properties.descFontSize)
        descFontColor.value =                   properties.descFontColor
        descText.value =                        properties.descText
        hasEventDateInLocation.value =          properties.hasEventDateInLocation
        eventDate.value =                       if(properties.eventDate == null) Date() else Date(properties.eventDate)
        hasEventTimeInLocation.value =          properties.hasEventTimeInLocation
        eventTime.value =                       properties.eventTime ?: Helper.getTimeString()
        hasEventCityInLocation.value =          properties.hasEventCityInLocation
        eventLocation.value =                   properties.eventLocation
        eventCountry.value =                    properties.eventCountry
        hasEditResultLocationText.value =       properties.hasEditResultLocationText
        resultLocationText.value =              properties.resultLocationText ?: ""
        locationFont.value =                    FontText(properties.locationFontName, properties.locationFontResId, properties.locationFontSize)
        locationFontColor.value =               properties.locationFontColor
        hasEventCoordinatesInLocation.value =   properties.hasEventCoordinatesInLocation
        coordinates.value =                     arrayListOf(properties.coordinatesLatitude!!, properties.coordinatesLongitude!!)
        separator.value =                       Separator(properties.separatorWidth!!, properties.separatorType!!)
        separatorColor.value =                  properties.separatorColor
        hasGraticule.value =                    properties.hasGraticule
        graticuleWidth.value =                  properties.graticuleWidth
        graticuleColor.value =                  properties.graticuleColor
        graticuleOpacity.value =                properties.graticuleOpacity
        graticuleType.value =                   properties.graticuleType
        hasMilkyWay.value =                     properties.hasMilkyWay
        hasNames.value =                        properties.hasNames
        namesStarsSize.value =                  properties.namesStarsSize
        namesStarsColor.value =                 properties.namesStarsColor
        namesStarsLang.value =                  Lang(properties.namesStarsLangLabel, properties.namesStarsLangName)
        hasConstellations.value =               properties.hasConstellations
        constellationsWidth.value =             properties.constellationsWidth
        constellationsColor.value =             properties.constellationsColor
        constellationsOpacity.value =           properties.constellationsOpacity
        starsSize.value =                       properties.starsSize
        starsColor.value =                      properties.starsColor
        starsOpacity.value =                    properties.starsOpacity
        textBlock.value =                       TextBlock(properties.textBlockWidth, properties.textBlockHeight, properties.textBlockIndent)
        textBlockColor.value =                  properties.textBlockColor
    }

    init {
        CoroutineScope(Dispatchers.IO).launch  {
            launch { initStarMapBitmap() }

            val drawObjects = launch {
                launch { drawHolst() }
                launch { drawHolstBorder() }
                launch { drawMap() }
                launch { drawDesc() }
                launch { drawSeparator() }
                launch { correctLocationText()  }

            }

            drawObjects.join()
            drawTextBlock()
            drawCanvas()

            isDataInitialized = true
        }

        holst.observe(activity)  {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    initRequestStarMap()

                    val drawObjects = launch {
                        launch { drawHolst() }
                        launch { drawHolstBorder() }
                        launch { drawMap() }
                        launch { drawDesc() }
                        launch { drawSeparator() }
                        launch { drawLocationText() }
                    }

                    drawObjects.join()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        holstColor.observe(activity)  {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawHolst() }
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
        borderHolstColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val drawObjects = launch {
                        launch { drawHolstBorder() } // TODO: Зачем ждать один метод?
                    }

                    drawObjects.join()
                    drawCanvas()
                }
            }
        }
        starMapColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
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
                    requestStarMap("showGraticule", it.toString())
                }
            }
        }
        graticuleWidth.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val width = starMapRadius.value!! * it / 2000
                    requestStarMap("widthGraticule", width.toString())
                }
            }
        }
        graticuleColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("colorGraticule", it)
                }
            }
        }
        graticuleOpacity.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("opacityGraticule", (it.toFloat() / 100).toString())
                }
            }
        }
        graticuleType.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("typeGraticule", it.toString())
                }
            }
        }
        hasMilkyWay.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("showMilkyWay", it.toString())
                }
            }
        }
        hasNames.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("showNames", it.toString())
                }
            }
        }
        namesStarsSize.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val size = starMapRadius.value!! * it / 1750
                    requestStarMap("sizeNames", size.toString())
                }
            }
        }
        namesStarsColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("colorNames", it)
                }
            }
        }
        namesStarsLang.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("langNames", it.name.toString())
                }
            }
        }
        hasConstellations.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("showConstellations", it.toString())
                }
            }
        }
        constellationsWidth.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val width = starMapRadius.value!! * it / 2000
                    requestStarMap("widthConstellations", width.toString())
                }
            }
        }
        constellationsColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("colorConstellations", it)
                }
            }
        }
        constellationsOpacity.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("opacityConstellations", (it.toFloat() / 100).toString())
                }
            }
        }
        starsSize.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    val size = starMapRadius.value!! * it / 1750
                    requestStarMap("sizeStars", size.toString())
                }
            }
        }
        starsColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("colorStars", it)
                }
            }
        }
        starsOpacity.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    requestStarMap("opacityStars", (it.toFloat() / 100).toString())
                }
            }
        }
        isLoadedStarMap.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawMap()

                    drawCanvas()
                }
            }
        }
        descFont.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawDesc()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        descFontColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawDesc()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        descText.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawDesc()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        hasEventDateInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    correctLocationText()
                }
            }
        }
        eventDate.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    launch { correctLocationText() }
                    launch { initRequestStarMap() }
                }
            }
        }
        hasEventTimeInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    correctLocationText()
                }
            }
        }
        eventTime.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    launch { correctLocationText() }
                    launch { initRequestStarMap() }
                }
            }
        }
        hasEventCityInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    correctLocationText()
                }
            }
        }
        eventLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    correctLocationText()
                }
            }
        }
        locationFont.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawLocationText()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        locationFontColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawLocationText()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        hasEventCoordinatesInLocation.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    correctLocationText()
                }
            }
        }
        coordinates.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    launch { correctLocationText() }
                    launch { initRequestStarMap() }
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
                    drawLocationText()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        separator.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawSeparator()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        separatorColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawSeparator()
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        textBlock.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
        textBlockColor.observe(activity) {
            if(isDataInitialized) {
                CoroutineScope(Dispatchers.IO).launch  {
                    drawTextBlock()
                    drawCanvas()
                }
            }
        }
    }

    private fun initStarMapBitmap() {
        Log.d("MyLog", "Start initStarMapBitmap")

        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(activity))
            .build()

        val webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                Log.d("Console", "${consoleMessage.message()} -- From line ${consoleMessage.lineNumber()} of ${consoleMessage.sourceId()}");
                return true
            }
        }

        val webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(request.url)
            }

            override fun shouldInterceptRequest(view: WebView?, url: String): WebResourceResponse? {
                return assetLoader.shouldInterceptRequest(Uri.parse(url))
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d("Console", "onPageFinished")
                view!!.loadUrl("""
                   javascript: initStarMap(`${initConfigStarMap()}`);  androidCallback = (png) => {Android.callBackStarMap(png)};
                """)
            }
        }

        val handler = Handler(Looper.getMainLooper()) {
            val decodedString: ByteArray = Base64.decode(it.data.getString("png"), Base64.DEFAULT)
            bitmapStarMap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)

            isLoadedStarMap.postValue(true)

            true
        }

        activity.runOnUiThread(kotlinx.coroutines.Runnable {
            webViewStarMap.loadUrl("https://appassets.androidplatform.net/assets/starmap/templates/classic_v1/index.html")
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
                allowFileAccess = false
                allowContentAccess = false
            }
        })

        Log.d("MyLog", "Done initStarMapBitmap")
    }
    private fun initRequestStarMap() {
        Log.d("MyLog", "Start initRequestStarMap")

        activity.runOnUiThread(kotlinx.coroutines.Runnable {
            webViewStarMap.loadUrl(
                """
                   javascript: initStarMap(`${initConfigStarMap()}`);
                """
            )
        })

        Log.d("MyLog", "Done initRequestStarMap")
    }
    private fun initConfigStarMap(): String {
        Log.d("MyLog", "Start initConfigStarMap")

        val date = Calendar.getInstance()
        date.time = eventDate.value!!

        val year    = date.get(Calendar.YEAR)
        val month   = date.get(Calendar.MONTH)
        val day     = date.get(Calendar.DAY_OF_MONTH)
        val hours   = eventTime.value!!.split(":")[0].toInt()
        val minutes = eventTime.value!!.split(":")[1].toInt()

        date.set(year, month, day, hours, minutes)

        val radius = getRadiusMap()

        val jsonString =
            """
                {
                    "date": ${date.time.time},
                    "latitude": ${coordinates.value!![0]}, 
                    "longtitude" :${coordinates.value!![1]},
                    "width": ${radius * 2},
                    "font": "Arial, Times, 'Times Roman', serif",
                    "stars": {
                        "size": ${radius * starsSize.value!! / 1750},
                        "exponent": -0.28,
                        "dataPath": "https://appassets.androidplatform.net/assets/starmap/data/stars.6.json",
                        "propername": ${hasNames.value},
                        "propernamePath": "https://appassets.androidplatform.net/assets/starmap/data/starnames.json",
                        "propernameLang": "${namesStarsLang.value!!.name}",
                        "propernameStyle": {
                            "fill": "${namesStarsColor.value!!}",
                            "font": "${radius * namesStarsSize.value!! / 1750}",
                            "align": "right",
                            "opacity": 1
                        },
                        "propernameLimit": 2.5,
                        "fill": "${starsColor.value!!}", 
                        "opacity": ${starsOpacity.value!!.toFloat() / 100} 
                    },
                    "dsos": {
                        "show": true,
                        "limit": 6,
                        "style": { "fill": "${starsColor.value!!}", "width": 2, "opacity": ${starsOpacity.value!!.toFloat() / 100}  }, 
                        "size": ${radius * starsSize.value!! / 1750},
                        "names": ${hasNames.value},
                        "nameLang": "${namesStarsLang.value!!.name}",
                        "namePath": "https://appassets.androidplatform.net/assets/starmap/data/dsonames.json",
                        "nameStyle": {
                            "fill": "${namesStarsColor.value!!}",
                            "font": "${radius * namesStarsSize.value!! / 1750}",
                            "align": "left",
                            "opacity": 1
                        },
                        "nameLimit": 4,
                        "exponent": -0.28,
                        "dataPath": "https://appassets.androidplatform.net/assets/starmap/data/dsos.bright.json"
                    },
                    "planets": {
                        "show": true,
                        "which": [ "sol", "mer", "ven", "ter", "lun", "mar", "jup", "sat", "ura", "nep", "cer", "plu"],
                        "style": {
                          "sol": { "fill": "#ffff00", "size": 12 },
                          "mer": { "fill": "#cccccc" },
                          "ven": { "fill": "#eeeecc" },
                          "ter": { "fill": "#00ccff" },
                          "lun": { "fill": "#ffffff" },
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
                        "dataPath": "https://appassets.androidplatform.net/assets/starmap/data/planets.json",
                        "names": ${hasNames.value},
                        "nameLang": "${namesStarsLang.value!!.name}",
                        "nameStyle": {
                            "fill": "${namesStarsColor.value!!}",
                            "font": "${radius * namesStarsSize.value!! / 1750}",
                            "align": "right",
                            "opacity": 1
                        }
                    },
                    "constellations": {
                        "show": ${hasConstellations.value},
                        "dataPath": "https://appassets.androidplatform.net/assets/starmap/data/constellations.lines.json",
                        "style": {
                          "stroke": "${constellationsColor.value!!}", 
                          "strokeWidth": ${radius * constellationsWidth.value!! / 2000},
                          "opacity": ${constellationsOpacity.value!!.toFloat() / 100} 
                        },
                        "names": ${hasNames.value},
                        "nameLang": "${namesStarsLang.value!!.name}",
                        "namePath": "https://appassets.androidplatform.net/assets/starmap/data/constellations.json",
                        "nameStyle": {
                            "fill": "${namesStarsColor.value!!}",
                            "align": "center",
                            "baseline": "middle",
                            "opacity": 1,
                            "font": "${radius * namesStarsSize.value!! / 1750}"
                        }
                    },
                    "mw": {
                        "show": ${hasMilkyWay.value},
                        "style": { "fill": "#ffffff", "opacity": "0.15" },
                        "dataPath": "https://appassets.androidplatform.net/assets/starmap/data/mw.json"
                    },
                    "graticule": {
                        "show": ${hasGraticule.value},
                        "style": {
                            "stroke": "${graticuleColor.value!!}",
                            "strokeWidth": ${radius * graticuleWidth.value!! / 2000},
                            "opacity": ${graticuleOpacity.value!!.toFloat() / 100}
                        }
                    }
                }       
            """.trimIndent()

        Log.d("MyLog", "Done initConfigStarMap")
        return jsonString
    }
    private fun requestStarMap(funcName: String, value: String) {
        Log.d("MyLog", "Start simpleRequestStarMap")

        activity.runOnUiThread(kotlinx.coroutines.Runnable {
            webViewStarMap.loadUrl("""javascript: $funcName("$value");""")
        })

        Log.d("MyLog", "Done simpleRequestStarMap")
    }
    private fun correctLocationText() {
        Log.d("MyLog", "Start correctLocationText")

        if(!hasEditResultLocationText.value!!) {
            var locationText = ""

            if(hasEventDateInLocation.value!!)
                locationText = "$locationText ${SimpleDateFormat("dd MMMM yyyy", Locale("ru")).format(eventDate.value!!)}"

            if(hasEventDateInLocation.value!! && hasEventTimeInLocation.value!!)
                locationText = "$locationText, "

            if(hasEventTimeInLocation.value!!) {
                val dateDefault = Calendar.getInstance()
                var hour = dateDefault.get(Calendar.HOUR_OF_DAY)
                var minute = dateDefault.get(Calendar.MINUTE)

                eventTime.value.let {
                    if(it!!.isNotEmpty()) {
                        hour = it.split(":")[0].toInt()
                        minute = it.split(":")[1].toInt()
                    }
                }

                locationText = "${locationText}${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}\n"
            } else if(hasEventDateInLocation.value!!) {
                locationText = "$locationText\n"
            }

            if(hasEventCityInLocation.value!!) {
                locationText = if(eventCountry.value!!.isNotEmpty()) {
                    "$locationText${eventLocation.value}, ${eventCountry.value}\n"
                } else {
                    "$locationText${eventLocation.value}\n"
                }
            }

            if(hasEventCoordinatesInLocation.value!!)
                locationText = "${locationText}${Helper.convert(coordinates.value!![0], coordinates.value!![1])}"

            resultLocationText.postValue(locationText.trim())

            if(!isDataInitialized) {
                drawLocationText(locationText.trim())
            }
        }

        Log.d("MyLog", "Done correctLocationText")
    }
    private fun drawHolst() {
        Log.d("MyLog", "Start drawHolst")

        val holstPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            isDither = true
            isAntiAlias = true
        }

        bitmapHolst = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(bitmapHolst).drawRect(0F, 0F, holst.value!!.width!!, holst.value!!.height!!, holstPaint)

        Log.d("MyLog", "Done drawHolst")
    }
    private fun drawHolstBorder() {
        Log.d("MyLog", "Start drawHolstBorder")

        val indent = (holst.value!!.width!! / 5F) * borderHolst.value!!.indent!! / 100
        val widthBorder = (holst.value!!.width!! / 15F) * borderHolst.value!!.width!! / 100

        val border = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            color = Color.parseColor(borderHolstColor.value!!)
            strokeWidth = widthBorder  //TODO: Ширина границы не изменяет общую длину отступа от края?
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawRect(indent, indent, holst.value!!.width!!.toInt() - indent, holst.value!!.height!!.toInt() - indent, border)

        bitmapHolstBorder = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt())

        Log.d("MyLog", "Done drawHolstBorder")
    }
    private fun drawMap() {
        Log.d("MyLog", "Start drawMap")

        val tempBitmap: Bitmap

        val widthMap = holst.value!!.width!!
        val heightMap = holst.value!!.height!!

        // TODO: При изменении размера холста, приложение падает
        tempBitmap = if(isLoadedStarMap.value!!) {
            val x = (bitmapStarMap.width / 2F) - widthMap / 2F
            val y = (bitmapStarMap.height / 2F) - heightMap / 2F

            Bitmap.createBitmap(
                bitmapStarMap,
                x.toInt(),
                y.toInt(),
                widthMap.toInt(),
                heightMap.toInt()
            )
        } else {
            getLoadingBitmap()
        }

        val newBitmap = Bitmap.createBitmap(tempBitmap.width, tempBitmap.height, tempBitmap.config)

        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.parseColor(starMapColor.value!!))
        canvas.drawBitmap(tempBitmap, 0f, 0f, null)

        bitmapMap = newBitmap

        Log.d("MyLog", "Done drawMap")
    }
    private fun drawTextBlock() {
        Log.d("MyLog", "Start drawTextBlock")

        val width =  holst.value!!.width!! * textBlock.value!!.width!! / 100
        val height = holst.value!!.height!! * textBlock.value!!.height!! / 100

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor(textBlockColor.value!!)
            isDither = true
            isAntiAlias = true
        }

        bitmapTextBlock = Bitmap.createBitmap(width.coerceAtLeast(1F).toInt(), height.coerceAtLeast(1F).toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmapTextBlock)

        // Рисуем блок
        canvas.drawRect(0F, 0F, width, height, paint)

        var separatorHeight = 0F
        var countElements = 2

        if(separator.value!!.shapeType != ShapeSeparator.NONE) {
            separatorHeight = bitmapSeparator.height.toFloat()
            countElements++
        }

        val margin = (height - bitmapDesc.height.toFloat() - separatorHeight - bitmapLocationText.height.toFloat()) / (countElements + 1)

        // Рисуем текст описание
        canvas.drawBitmap(bitmapDesc, width / 2 - bitmapDesc.width / 2, margin, null)

        // Рисуем разделитель
        if(separator.value!!.shapeType != ShapeSeparator.NONE) {
            canvas.drawBitmap(bitmapSeparator, width / 2 - bitmapSeparator.width / 2, margin + bitmapDesc.height + margin, null)
        }

        // Рисуем текст локации
        canvas.drawBitmap(bitmapLocationText, width / 2 - bitmapLocationText.width / 2, height - margin - bitmapLocationText.height.toFloat(), null)


        Log.d("MyLog", "Done drawTextBlock")
    }
    private fun drawDesc() {
        Log.d("MyLog", "Start drawDesc")

        val textSizeDesc = holst.value!!.width!! * descFont.value!!.size!! / 1000
        val lineHeightDesc = textSizeDesc * 0.5F

        val descTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = textSizeDesc
            typeface = ResourcesCompat.getFont(activity.applicationContext, descFont.value!!.resId!!)
            color = Color.parseColor(descFontColor.value!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = descText.value!!.split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, descTextPaint, (holst.value!!.width!!/1.3).toInt() , holst.value!!.width!!/2, totalHeightText, lineHeightDesc)
        }

        bitmapDesc = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), totalHeightText.toInt())

        Log.d("MyLog", "Done drawDesc")
    }
    private fun drawSeparator() {
        Log.d("MyLog", "Start drawSeparator")

        val shapeType = separator.value!!.shapeType
        val width = holst.value!!.width!! * textBlock.value!!.width!! / 100

        bitmapSeparator = when (shapeType) {
            ShapeSeparator.LINE -> drawLineSeparator(width)
            ShapeSeparator.CURVED -> drawDrawableSizedSeparator(shapeType, width,1280F, 112F, 1F)
            ShapeSeparator.STARS -> drawDrawableSizedSeparator(shapeType, width, 511.99142F, 165F, 0.3F)
            ShapeSeparator.HEARTS -> drawDrawableSizedSeparator(shapeType, width, 511.99142F, 165F, 0.3F)
            ShapeSeparator.STAR -> drawDrawableSizedSeparator(shapeType, width,1F, 1F, 0.1F)
            ShapeSeparator.HEART -> drawDrawableSizedSeparator(shapeType, width, 1F, 1F,0.1F)

            else -> drawDrawableSizedSeparator(shapeType, width,511.99142F, 165F, 0.1F)
        }

        Log.d("MyLog", "Done drawSeparator")
    }
    private fun drawLocationText(locationText: String = resultLocationText.value!!) {
        Log.d("MyLog", "Start drawLocationText")

        val textSizeLocation = (holst.value!!.width!! / 2) * locationFont.value!!.size!! / 1000

        val eventLocation = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = textSizeLocation
            color = Color.parseColor(locationFontColor.value!!)
            typeface = ResourcesCompat.getFont(activity, locationFont.value!!.resId!!)
            isDither = true
            isAntiAlias = true
        }

        val tempBitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val textLines = locationText.split("\n")
        var totalHeightText = 0F

        for(textLine in textLines) {
            totalHeightText += canvas.drawMultilineText(textLine, eventLocation, holst.value!!.width!!.toInt() , holst.value!!.width!!/2,  totalHeightText)
        }

        bitmapLocationText = Bitmap.createBitmap(tempBitmap, 0, 0, holst.value!!.width!!.toInt(), totalHeightText.toInt())

        Log.d("MyLog", "Done drawLocationText")
    }
    private fun getLoadingBitmap(): Bitmap {
        Log.d("MyLog", "Start getLoadingBitmap")

        val widthMap = holst.value!!.width!!
        val heightMap = holst.value!!.height!!

        val tempBitmap = Bitmap.createBitmap(widthMap.toInt(), heightMap.toInt(), Bitmap.Config.ARGB_8888)

        val loadingCanvas = Canvas(tempBitmap)

        val widthLoadingIcon = widthMap * 0.25
        val heightLoadingIcon = widthMap * 0.25
        val drawableLoadingIcon = ContextCompat.getDrawable(activity, R.drawable.ic_loading_map)!!
        val wrappedDrawable = DrawableCompat.wrap(drawableLoadingIcon)

        DrawableCompat.setTint(wrappedDrawable, if (starMapColor.value!! == "#FFFFFF") Color.BLACK else Color.WHITE)

        val bitmapLoadingIcon = wrappedDrawable.toBitmap(
            widthLoadingIcon.toInt(),
            heightLoadingIcon.toInt().coerceAtLeast(1)
        )

        loadingCanvas.drawBitmap(bitmapLoadingIcon, (widthMap / 2 - widthLoadingIcon / 2).toFloat(), (heightMap / 2 - heightLoadingIcon / 2).toFloat(), null)

        Log.d("MyLog", "Done getLoadingBitmap")

        return tempBitmap
    }
    override fun drawCanvas() {
        Log.d("MyLog", "Start drawCanvas")

        bitmap = Bitmap.createBitmap(holst.value!!.width!!.toInt(), holst.value!!.height!!.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        // Рисуем холст
        canvas.drawBitmap(bitmapHolst, 0F, 0F, null)

        // Рисуем карту
        canvas.drawBitmap(bitmapMap, 0F, 0F, null)

        // Рисуем рамку холста
        if(hasBorderHolst.value!!) {
            canvas.drawBitmap(bitmapHolstBorder, 0F, 0F, null)
        }

        // Рисуем блок с текстом
        val indent = holst.value!!.height!! * textBlock.value!!.indent!! / 100F
        canvas.drawBitmap(bitmapTextBlock, holst.value!!.width!! / 2 - bitmapTextBlock.width / 2, holst.value!!.height!! - indent - bitmapTextBlock.height, null)

        doneRedraw.postValue(true)

        Log.d("MyLog", "Done drawCanvas")
    }
    override fun getControllerList(): ArrayList<Controller> {
        Log.d("MyLog", "Start getControllerList")
        return controllerList
    }

    override fun getRadiusMap(): Float {
        val widthMap = holst.value!!.width!!
        val heightMap = holst.value!!.height!!

        return sqrt(widthMap * widthMap + heightMap * heightMap) / 2
    }
}