package com.nikolaydemidovez.starmap.templates

import android.graphics.Bitmap
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.view.ViewTreeObserver
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
    val holst =                         MutableLiveData<Holst>()         // Холст
    val hasBorderHolst =                MutableLiveData<Boolean>()       // Добавлена ли рамка холста
    val borderHolst =                   MutableLiveData<HolstBorder>()   // Рамка холста
    val starMap =                       MutableLiveData<StarMap>()       // Звездная карта
    val hasBorderMap =                  MutableLiveData<Boolean>()       // Добавлена ли рамка карты
    val starMapBorder =                 MutableLiveData<StarMapBorder>() // Рамка звездной карты
    val descFont =                      MutableLiveData<FontText>()      // Шрифт текста события
    val descText =                      MutableLiveData<String>()        // Основной текст
    val hasEventDateInLocation =        MutableLiveData<Boolean>()       // Добавить ли дату в текст локации
    val eventDate =                     MutableLiveData<Date>()          // Дата события
    val hasEventTimeInLocation =        MutableLiveData<Boolean>()       // Добавить ли время в текст локации
    val eventTime =                     MutableLiveData<String>()        // Время события
    val hasEventCityInLocation =        MutableLiveData<Boolean>()       // Добавить ли город в текст локации
    val eventLocation =                 MutableLiveData<String>()        // Место события
    val locationFont =                  MutableLiveData<FontText>()      // Шрифт текста локации
    val eventCountry =                  MutableLiveData<String>()        // Страна события
    val hasEditResultLocationText =     MutableLiveData<Boolean>()       // Изменить ли результирующий текст в локации
    val resultLocationText =            MutableLiveData<String>()        // Результирующий текст локации
    val hasEventCoordinatesInLocation = MutableLiveData<Boolean>()       // Добавить ли широту и долготу в текст локации
    val eventLatitude =                 MutableLiveData<Double>()        // Широта места события
    val eventLongitude =                MutableLiveData<Double>()        // Долгота места события
    val hasSeparator =                  MutableLiveData<Boolean>()       // Добавить ли разделитель
    val separator =                     MutableLiveData<Separator>()     // Разделителя
    val hasGraticule =                  MutableLiveData<Boolean>()       // Добавить ли сеть координат
    val graticule =                     MutableLiveData<Graticule>()     // Сеть координат
    val hasConstellations =             MutableLiveData<Boolean>()       // Добавить ли созвездия
    val constellations =                MutableLiveData<Constellations>()// Созвездия
    val hasMilkyWay =                   MutableLiveData<Boolean>()       // Добавить ли млечный путь
    val stars =                         MutableLiveData<Stars>()         // Добавить ли млечный путь
    val hasNames =                      MutableLiveData<Boolean>()       // Добавить ли названия на звездной карте
    val namesStars =                    MutableLiveData<NamesStars>()    // Названия звезд и созвездий
    // Конец списка свойств

    val doneRedraw = MutableLiveData<Boolean>(false)

    var bitmap: Bitmap = Bitmap.createBitmap(2480, 3508,Bitmap.Config.ARGB_8888)
        protected set

    val colorList = arrayListOf(
        "#000000",
        "#FFFFFF",
        "#1ABC9C",
        "#16A085",
        "#2ECC71",
        "#27AE60",
        "#3498DB",
        "#2980B9",
        "#9B59B6",
        "#8E44AD",
        "#34495E",
        "#2C3E50",
        "#F1C40F",
        "#F39C12",
        "#E67E22",
        "#D35400",
        "#E74C3C",
        "#C0392B",
        "#BDC3C7",
        "#95A5A6",
        "#7F8C8D"
    )
    val langList = arrayListOf(
        Lang("Русский", "ru"),
        Lang("English", "en"),
        Lang("Arabic", "ar"),
        Lang("Chinese", "zh"),
        Lang("Czech", "cz"),
        Lang("Estonian", "ee"),
        Lang("Finnish", "fi"),
        Lang("French", "fr"),
        Lang("German", "de"),
        Lang("Greek", "el"),
        Lang("Hebrew", "he"),
        Lang("Italian", "it"),
        Lang("Japanese", "ja"),
        Lang("Korean", "ko"),
        Lang("Hindi", "hi"),
        Lang("Persian", "fa"),
        Lang("Spanish", "es"),
        Lang("Turkish", "tr")
    )

    abstract fun drawCanvas()
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

