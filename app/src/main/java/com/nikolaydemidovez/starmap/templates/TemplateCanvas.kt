package com.nikolaydemidovez.starmap.templates

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.pojo.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


abstract class TemplateCanvas(private val activity: MainActivity) {

    // Начало списка основных свойства холста
    val holst                           = MutableLiveData<Holst>()      // Холст
    val hasBorderHolst                  = MutableLiveData<Boolean>()    // Добавлена ли рамка холста
    val borderHolst                     = MutableLiveData<HolstBorder>()// Рамка холста
    val backgroundColorMap              = MutableLiveData<String>()     // Цвет фона карты
    val radiusMap                       = MutableLiveData<Float>()      // Радиус карты
    val hasBorderMap                    = MutableLiveData<Boolean>()    // Добавлена ли рамка карты
    val widthBorderMap                  = MutableLiveData<Float>()      // Ширина рамки карты
    val mapBorderColor                  = MutableLiveData<Int>()        // Цвет рамки карты
    val descFont                        = MutableLiveData<FontText>()   // Шрифт текста события
    val descText                        = MutableLiveData<String>()     // Основной текст
    val hasEventDateInLocation          = MutableLiveData<Boolean>()    // Добавить ли дату в текст локации
    val eventDate                       = MutableLiveData<Date>()       // Дата события
    val hasEventTimeInLocation          = MutableLiveData<Boolean>()    // Добавить ли время в текст локации
    val eventTime                       = MutableLiveData<String>()     // Время события
    val hasEventCityInLocation          = MutableLiveData<Boolean>()    // Добавить ли город в текст локации
    val eventLocation                   = MutableLiveData<String>()     // Место события
    val locationFont                    = MutableLiveData<FontText>()   // Шрифт текста локации
    val eventCountry                    = MutableLiveData<String>()     // Страна события
    val hasEditResultLocationText       = MutableLiveData<Boolean>()    // Изменить ли результирующий текст в локации
    val resultLocationText              = MutableLiveData<String>()     // Результирующий текст локации
    val hasEventCoordinatesInLocation   = MutableLiveData<Boolean>()    // Добавить ли широту и долготу в текст локации
    val eventLatitude                   = MutableLiveData<Double>()     // Широта места события
    val eventLongitude                  = MutableLiveData<Double>()     // Долгота места события
    val hasSeparator                    = MutableLiveData<Boolean>()    // Добавить ли разделитель
    val separator                       = MutableLiveData<Separator>()  // Разделителя
    // Конец списка свойств

    var bitmap: Bitmap = Bitmap.createBitmap(2480, 3508,Bitmap.Config.ARGB_8888)
        protected set

    protected var listener: OnDrawListener? = null

    fun setOnDrawListener(listener: OnDrawListener) {
        this.listener = listener
    }

    interface OnDrawListener {
        fun onDraw()
    }

    abstract fun draw()
    abstract fun getControllerList(): ArrayList<Controller>

    fun getShortBitmap(): Bitmap {
        val maxSize = (holst.value!!.width!!).coerceAtLeast(holst.value!!.height!!)
        val scaleFactor: Float = if(maxSize > 3000) maxSize / 3000 else 1F

        return Bitmap.createScaledBitmap(bitmap, (holst.value!!.width!! / scaleFactor).toInt(), (holst.value!!.height!! / scaleFactor).toInt(), false)
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

