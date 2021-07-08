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

    // Начало списка основных свойства холста
    val canvasWidth                     = MutableLiveData<Float>()      // Ширина холста
    val canvasHeight                    = MutableLiveData<Float>()      // Высота холста
    val backgroundColorCanvas           = MutableLiveData<Int>()        // Цвет фона холста
    val canvasBorderColor               = MutableLiveData<Int>()        // Цвет рамки холста
    val hasBorderCanvas                 = MutableLiveData<Boolean>()    // Добавлена ли рамка холста
    val indentBorderCanvas              = MutableLiveData<Float>()      // Отступ рамки от края холста
    val widthBorderCanvas               = MutableLiveData<Float>()      // Ширина рамки холста
    val backgroundColorMap              = MutableLiveData<Int>()        // Цвет фона карты
    val radiusMap                       = MutableLiveData<Float>()      // Радиус карты
    val hasBorderMap                    = MutableLiveData<Boolean>()    // Добавлена ли рамка карты
    val widthBorderMap                  = MutableLiveData<Float>()      // Ширина рамки карты
    val mapBorderColor                  = MutableLiveData<Int>()        // Цвет рамки карты
    val descTextSize                    = MutableLiveData<Float>()      // Размер основного текста
    val eventLocationSize               = MutableLiveData<Float>()      // Размер текста локации
    val descText                        = MutableLiveData<String>()     // Основной текст
    val hasEventDateInLocation          = MutableLiveData<Boolean>()    // Добавить ли дату в текст локации
    val eventDate                       = MutableLiveData<Date>()       // Дата события
    val hasEventTimeInLocation          = MutableLiveData<Boolean>()    // Добавить ли время в текст локации
    val eventTime                       = MutableLiveData<Long>()       // Время события
    val hasEventCityInLocation          = MutableLiveData<Boolean>()    // Добавить ли город в текст локации
    val eventCity                       = MutableLiveData<String>()     // Город события
    val hasEventLatitudeInLocation      = MutableLiveData<Boolean>()    // Добавить ли широту в текст локации
    val eventLatitude                   = MutableLiveData<Float>()      // Широта места события
    val hasEventLongitudeInLocation     = MutableLiveData<Boolean>()    // Добавить ли долготу в текст локации
    val eventLongitude                  = MutableLiveData<Float>()      // Долгота места события
    val hasSeparator                    = MutableLiveData<Boolean>()    // Добавить ли разделитель
    val separatorColor                  = MutableLiveData<Int>()        // Цвет разделителя
    val separatorWidth                  = MutableLiveData<Float>()      // Длина разделителя
    val separatorHeight                 = MutableLiveData<Float>()      // Высота разделителя
    // Конец списка свойств

    protected var listener: OnDrawListener? = null

    var bitmap: Bitmap
        protected set

    init {
        bitmap = Bitmap.createBitmap(canvasWidth.value!!.toInt(), canvasHeight.value!!.toInt(),Bitmap.Config.ARGB_8888)
    }

    interface OnDrawListener {
        fun onDraw()
    }

    abstract fun draw()

    fun getShortBitmap(): Bitmap {
        val maxSize = (canvasWidth.value!!).coerceAtLeast(canvasHeight.value!!)
        val scaleFactor: Float = if(maxSize > 3000) maxSize / 3000 else 1F

        return Bitmap.createScaledBitmap(bitmap, (canvasWidth.value!! / scaleFactor).toInt(), (canvasHeight.value!! / scaleFactor).toInt(), false)
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

        if(hasEventDateInLocation.value!!)      locationText = "$locationText $eventDate"
        if(hasEventTimeInLocation.value!!)      locationText = "$locationText $eventTime"
        if(hasEventCityInLocation.value!!)      locationText = "$locationText $eventCity"
        if(hasEventLatitudeInLocation.value!!)  locationText = "$locationText $eventLatitude"
        if(hasEventLongitudeInLocation.value!!) locationText = "$locationText $eventLongitude"

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

