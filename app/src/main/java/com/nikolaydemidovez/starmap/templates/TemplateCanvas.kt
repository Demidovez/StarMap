package com.nikolaydemidovez.starmap.templates

import android.app.Activity
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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.nikolaydemidovez.starmap.controllers.canvas_v1.CanvasV1ControllerViewModel
import java.io.*
import java.sql.Time
import java.util.*


abstract class TemplateCanvas(private val activity: Activity) {
    protected val PIXELS_IN_ONE_MM = 3.779527559055F

    // Начало списка основных свойства холста
    val canvasWidth = MutableLiveData<Float>() //                                        // Ширина холста
    val canvasHeight = MutableLiveData<Float>() //                                      // Высота холста
    val backgroundColorCanvas = MutableLiveData<Int>()  // Цвет фона холста
    val canvasBorderColor = MutableLiveData<Int>()    // Цвет рамки холста
    val hasBorderCanvas = MutableLiveData<Boolean>()                                               // Добавлена ли рамка холста
    val indentBorderCanvas = MutableLiveData<Float>()                 // Отступ рамки от края холста
    val widthBorderCanvas = MutableLiveData<Float>()                    // Ширина рамки холста
    val backgroundColorMap = MutableLiveData<Int>()     // Цвет фона карты
    val radiusMap = MutableLiveData<Float>()                                         // Радиус карты
    val hasBorderMap = MutableLiveData<Boolean>()                                           // Добавлена ли рамка карты
    val widthBorderMap = MutableLiveData<Float>()                       // Ширина рамки карты
    val mapBorderColor = MutableLiveData<Int>()         // Цвет рамки карты
    val descTextSize                    = MutableLiveData<Float>()                                       // Размер основного текста
    val eventLocationSize               = MutableLiveData<Float>()                                      // Размер текста локации
    val descText                        = MutableLiveData<String>()              // Основной текст
    val hasEventDateInLocation          = MutableLiveData<Boolean>()
    val eventDate                       = MutableLiveData<Date>()
    val hasEventTimeInLocation          = MutableLiveData<Boolean>()
    val eventTime                       = MutableLiveData<Long>()
    val hasEventCityInLocation          = MutableLiveData<Boolean>()
    val eventCity                       = MutableLiveData<String>()
    val hasEventLatitudeInLocation      = MutableLiveData<Boolean>()
    val eventLatitude                   = MutableLiveData<Float>()
    val hasEventLongitudeInLocation     = MutableLiveData<Boolean>()
    val eventLongitude                  = MutableLiveData<Float>()
    val hasSeparator                    = MutableLiveData<Boolean>()
    val separatorColor                  = MutableLiveData<Int>()
    val separatorWidth                  = MutableLiveData<Float>()
    val separatorHeight                 = MutableLiveData<Float>()
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

    fun getShortBitmap(): Bitmap {
        val scaleFactor: Float = if(Math.max(canvasWidth, canvasHeight) > 3000) Math.max(canvasWidth, canvasHeight) / 3000 else 1F

        return Bitmap.createScaledBitmap(bitmap, (canvasWidth / scaleFactor).toInt(), (canvasHeight / scaleFactor).toInt(), false)
    }

    fun updateBackgroundColorCanvas(color: String) {
        backgroundColorCanvas.value = Color.parseColor(color)
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
        val file = File(activity.applicationContext.filesDir, "StarMap.${typeFile.lowercase()}")

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
        activity.applicationContext.contentResolver.openOutputStream(uri, "w").use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)

            Toast.makeText(activity.applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
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
        activity.applicationContext.contentResolver.openOutputStream(uri, "w").use {
            val pdfDocument = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
            val page = pdfDocument.startPage(pageInfo)

            page.canvas.drawBitmap(bitmap, 0F, 0f, null)
            pdfDocument.finishPage(page)
            pdfDocument.writeTo(it)
            pdfDocument.close()

            Toast.makeText(activity.applicationContext, "Сохранено", Toast.LENGTH_SHORT).show()
        }
    }
}

