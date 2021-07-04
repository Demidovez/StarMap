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
import java.io.*
import java.sql.Time
import java.util.*


abstract class TemplateCanvas(private val context: Context) {
    private val PIXELS_IN_ONE_MM = 3.779527559055F

    // Начало списка основных свойства холста
    protected var canvasWidth: Float = 2480F
    protected var canvasHeight: Float = 3508F
    protected var backgroundColorCanvas: Int = 0
    protected var canvasBorderColor: Int = 0
    protected var hasBorderCanvas: Boolean = true
    protected var indentBorderCanvas: Float = 10F * PIXELS_IN_ONE_MM
    protected var widthBorderCanvas: Float = 3F * PIXELS_IN_ONE_MM
    protected var radiusMap: Float = 600F
    protected var descTextSize: Float = 100F
    protected var eventLocationSize: Float = 100F
    var descText: String = "День, когда сошлись все звезды вселенной..."
        private set
    protected var hasEventDateInLocation: Boolean = true
    var eventDate: Date = Date()
        private set
    protected var hasEventTimeInLocation: Boolean = true
    var eventTime: Long = Date().time
        private set
    protected var hasEventCityInLocation: Boolean = true
    var eventCity: String = "Москва"
        private set
    protected var hasEventLatitudeInLocation: Boolean = true
    var eventLatitude: Float = 55.7522200F
        private set
    protected var hasEventLongitudeInLocation: Boolean = true
    var eventLongitude: Float = 37.6155600F
        private set

    // Конец списка свойств

    protected var listener: OnDrawListener? = null

    var bitmap: Bitmap
        protected set

    init {
        bitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(),Bitmap.Config.ARGB_8888)
        radiusMap = canvasWidth / 2.5F
    }

    interface OnDrawListener {
        fun onDraw()
    }

    abstract fun draw()

    fun updateBackgroundColorCanvas(color: String) {
        backgroundColorCanvas = Color.parseColor(color)

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

    fun updateHasBorderCanvas(hasBorder: Boolean) {
        hasBorderCanvas = hasBorder

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

    fun saveToPNG(uri: Uri) {
        context.contentResolver.openOutputStream(uri, "w").use {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, it)

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