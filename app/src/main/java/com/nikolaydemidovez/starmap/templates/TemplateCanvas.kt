package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import android.provider.MediaStore
import android.util.DisplayMetrics
import java.io.*
import java.sql.Time
import java.util.*


abstract class TemplateCanvas(private val context: Context) {
    private val PIXELS_IN_ONE_MM = 3.779527559055F

    // Начало списка основных свойства холста
    protected var canvasWidth: Float = 2480F                                         // Ширина холста
    protected var canvasHeight: Float = 3508F                                        // Высота холста
    protected var backgroundColorCanvas: Int = Color.parseColor("#FFFFFF") // Цвет фона холста
    protected var canvasBorderColor: Int = Color.parseColor("#000000")     // Цвет рамки холста
    var hasBorderCanvas: Boolean = true                                              // Добавлена ли рамка холста
        private set
    protected var indentBorderCanvas: Float = 30F * PIXELS_IN_ONE_MM                 // Отступ рамки от края холста
    protected var widthBorderCanvas: Float = 3F * PIXELS_IN_ONE_MM                   // Ширина рамки холста
    protected var backgroundColorMap: Int = Color.parseColor("#000000")    // Цвет фона карты
    protected var radiusMap: Float = 1000F                                            // Радиус карты
    var hasBorderMap: Boolean = false                                                // Добавлена ли рамка карты
        private set
    protected var widthBorderMap: Float = 5F * PIXELS_IN_ONE_MM                      // Ширина рамки карты
    protected var mapBorderColor: Int = Color.parseColor("#FFFFFF")        // Цвет рамки карты
    protected var descTextSize: Float = 160F                                         // Размер основного текста
    protected var eventLocationSize: Float = 60F                                     // Размер текста локации
    var descText: String = "День, когда сошлись все звезды вселенной..."             // Основной текст
        private set
    var hasEventDateInLocation: Boolean = true
        private set
    var eventDate: Date = Date()
        private set
    var hasEventTimeInLocation: Boolean = true
        private set
    var eventTime: Long = Date().time
        private set
    var hasEventCityInLocation: Boolean = true
        private set
    var eventCity: String = "Москва"
        private set
    var hasEventLatitudeInLocation: Boolean = true
        private set
    var eventLatitude: Float = 55.7522200F
        private set
    var hasEventLongitudeInLocation: Boolean = true
        private set
    var eventLongitude: Float = 37.6155600F
        private set
    var hasSeparator: Boolean = true
        private set
    protected var separatorColor: Int = Color.parseColor("#000000")
    protected var separatorWidth: Float = 1200F
    protected var separatorHeight: Float = 7F

    // Конец списка свойств

    protected var listener: OnDrawListener? = null

    var bitmap: Bitmap
        protected set

    init {
        bitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(),Bitmap.Config.ARGB_8888)
    }

    interface OnDrawListener {
        fun onDraw()
    }

    abstract fun draw()

    fun updateBackgroundColorCanvas(color: String) {
        backgroundColorCanvas = Color.parseColor(color)

        draw()
    }

    fun updateBackgroundColorMap(color: String) {
        backgroundColorMap = Color.parseColor(color)

        draw()
    }

    fun updateRadiusMap(radius: Float) {
        radiusMap = radius

        draw()
    }

    fun updateHasBorderMap(has: Boolean) {
        hasBorderMap = has

        draw()
    }

    fun updateWidthBorderMap(width: Float) {
        widthBorderMap = width * PIXELS_IN_ONE_MM

        draw()
    }

    fun updateMapBorderColor(color: String) {
        mapBorderColor = Color.parseColor(color)

        draw()
    }

    fun updateCanvasBorderColor(color: String) {
        canvasBorderColor = Color.parseColor(color)

        draw()
    }

    fun updateCanvasSize(width: Float, height: Float) {
        canvasWidth = width
        canvasHeight = height

        draw()
    }

    fun updateHasBorderCanvas(has: Boolean) {
        hasBorderCanvas = has

        draw()
    }

    fun updateIndentBorderCanvas(indent: Float) {
        indentBorderCanvas = indent * PIXELS_IN_ONE_MM

        draw()
    }

    fun updateWidthBorderCanvas(width: Float) {
        widthBorderCanvas = width * PIXELS_IN_ONE_MM

        draw()
    }

    fun updateDescText(text: String) {
        descText = text

        draw()
    }

    fun updateDescTextSize(size: Float) {
        descTextSize = size

        draw()
    }

    fun updateEventLocationSize(size: Float) {
        eventLocationSize = size

        draw()
    }

    fun updateHasEventDateInLocation(has: Boolean) {
        hasEventDateInLocation = has

        draw()
    }

    fun updateHasEventTimeInLocation(has: Boolean) {
        hasEventTimeInLocation = has

        draw()
    }

    fun updateHasEventCityInLocation(has: Boolean) {
        hasEventCityInLocation = has

        draw()
    }

    fun updateHasEventLatitudeInLocation(has: Boolean) {
        hasEventLatitudeInLocation = has

        draw()
    }

    fun updateHasEventLongitudeInLocation(has: Boolean) {
        hasEventLongitudeInLocation = has

        draw()
    }

    fun updateHasSeparator(has: Boolean) {
        hasSeparator = has

        draw()
    }

    fun setOnDrawListener(listener: OnDrawListener) {
        this.listener = listener
    }

    fun convertToSharingFile(typeFile: String): File {
        val file = File(context.filesDir, "StarMap.${typeFile.lowercase()}")

        when(typeFile) {
            "PNG" -> {
                val bytes = ByteArrayOutputStream()

                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

                try {
                    file.createNewFile()
                    val fo = FileOutputStream(file)
                    fo.write(bytes.toByteArray())
                    fo.flush()
                    fo.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            "PDF" -> {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    val pdfDocument = PdfDocument()
                    val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
                    val page = pdfDocument.startPage(pageInfo)

                    page.canvas.drawBitmap(bitmap, 0F, 0f, null)
                    pdfDocument.finishPage(page)

                    file.createNewFile()
                    val fo = FileOutputStream(file)
                    pdfDocument.writeTo(fo)
                    fo.flush()
                    fo.close()

                    pdfDocument.close()
                }

            }
        }

        return file
    }

    fun saveToJPG(uri: Uri) {
        context.contentResolver.openOutputStream(uri, "w").use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

            Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
        }
    }

    protected fun getLocationText(): String {
        var locationText = ""

        if(hasEventDateInLocation)      locationText = "$locationText $eventDate"
        if(hasEventTimeInLocation)      locationText = "$locationText $eventTime"
        if(hasEventCityInLocation)      locationText = "$locationText $eventCity"
        if(hasEventLatitudeInLocation)  locationText = "$locationText $eventLatitude"
        if(hasEventLongitudeInLocation) locationText = "$locationText $eventLongitude"

        return locationText.trim()
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun saveToPDF(uri: Uri) {
        context.contentResolver.openOutputStream(uri, "w").use {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            page.canvas.drawBitmap(bitmap, 0F, 0f, null)
            pdfDocument.finishPage(page)
            pdfDocument.writeTo(it)
            pdfDocument.close()

            Toast.makeText(context, "Сохранено", Toast.LENGTH_SHORT).show()
        }
    }
}

