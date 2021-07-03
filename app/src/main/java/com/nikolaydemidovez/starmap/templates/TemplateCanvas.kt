package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
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


abstract class TemplateCanvas(private val context: Context) {
    protected var canvasWidth: Float = 2480F
    protected var canvasHeight: Float = 3508F
    protected var listener: OnDrawListener? = null

    var bitmap: Bitmap
        protected set

    protected var backgroundColorCanvas: Int = 0
    protected var canvasBorderColor: Int = 0

    init {
        bitmap = Bitmap.createBitmap(canvasWidth.toInt(), canvasHeight.toInt(),Bitmap.Config.ARGB_8888)
    }

    interface OnDrawListener {
        fun onDraw()
    }

    abstract fun draw()


    fun updateBackgroundColorCanvas(color: Int) {
        backgroundColorCanvas = ResourcesCompat.getColor(context.resources, color, null)

        draw()
    }

    fun updateCanvasBorderColor(color: Int) {
        canvasBorderColor = ResourcesCompat.getColor(context.resources, color, null)

        draw()
    }

    fun updateCanvasSize(width: Float, height: Float) {
        canvasWidth = width
        canvasHeight = height

        draw()
    }

    fun setOnDrawListener(listener: OnDrawListener) {
        this.listener = listener

        this.listener!!.onDraw()
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