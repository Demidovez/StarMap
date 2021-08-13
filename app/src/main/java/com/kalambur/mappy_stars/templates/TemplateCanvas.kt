package com.kalambur.mappy_stars.templates

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.text.TextPaint
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import com.kalambur.mappy_stars.MainActivity
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.pojo.*
import java.io.*
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat
import com.kalambur.mappy_stars.pojo.ShapeSeparator.Companion.CURVED
import com.kalambur.mappy_stars.pojo.ShapeSeparator.Companion.HEART
import com.kalambur.mappy_stars.pojo.ShapeSeparator.Companion.HEARTS
import com.kalambur.mappy_stars.pojo.ShapeSeparator.Companion.STAR
import com.kalambur.mappy_stars.pojo.ShapeSeparator.Companion.STARS
import com.kalambur.mappy_stars.room.AppDatabase
import com.kalambur.mappy_stars.room.TemplateRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import com.kalambur.mappy_stars.utils.helpers.Helper.Companion.dpToPx
import java.lang.Exception

abstract class TemplateCanvas(private val activity: MainActivity) {
    var title: String? = null
    var category: String? = null
    var name: String? = null
    var type = DEFAULT
    var templateId: Int? = null
    var image: String? = null

    // Начало списка основных свойства холста
    val holst =                         MutableLiveData<Holst>()                // Холст
    val holstColor =                    MutableLiveData<String>()               // Цвет холста
    val hasBorderHolst =                MutableLiveData<Boolean>()              // Добавлена ли рамка холста
    val borderHolst =                   MutableLiveData<HolstBorder>()          // Рамка холста
    val borderHolstColor =              MutableLiveData<String>()               // Цвет рамки холста
    val starMapRadius =                 MutableLiveData<Float>()                // Радиус звездной карта
    val starMapPosition =               MutableLiveData<Float>()                // Позиция звездной карта
    val starMapColor =                  MutableLiveData<String>()               // Цвет фона звездной карта
    val starMapBorder =                 MutableLiveData<StarMapBorder>()        // Рамка звездной карты
    val starMapBorderColor =            MutableLiveData<String>()               // Цвет рамки звездной карты
    val descFont =                      MutableLiveData<FontText>()             // Шрифт текста события
    val descFontColor =                 MutableLiveData<String>()               // Цвет шрифта текста события
    val descText =                      MutableLiveData<String>()               // Основной текст
    val hasEventDateInLocation =        MutableLiveData<Boolean>()              // Добавить ли дату в текст локации
    val eventDate =                     MutableLiveData<Date>()                 // Дата события
    val hasEventTimeInLocation =        MutableLiveData<Boolean>()              // Добавить ли время в текст локации
    val eventTime =                     MutableLiveData<String>()               // Время события
    val hasEventCityInLocation =        MutableLiveData<Boolean>()              // Добавить ли город в текст локации
    val eventLocation =                 MutableLiveData<String>()               // Место события
    val locationFont =                  MutableLiveData<FontText>()             // Шрифт текста локации
    val locationFontColor =             MutableLiveData<String>()               // Цвет шрифта текста локации
    val eventCountry =                  MutableLiveData<String>()               // Страна события
    val hasEditResultLocationText =     MutableLiveData<Boolean>()              // Изменить ли результирующий текст в локации
    val resultLocationText =            MutableLiveData<String>()               // Результирующий текст локации
    val hasEventCoordinatesInLocation = MutableLiveData<Boolean>()              // Добавить ли широту и долготу в текст локации
    val coordinates =                   MutableLiveData<ArrayList<Float>>()     // Координаты места события
    val separator =                     MutableLiveData<Separator>()            // Разделителя
    val separatorColor =                MutableLiveData<String>()               // Цвет разделителя
    val hasGraticule =                  MutableLiveData<Boolean>()              // Добавить ли сеть координат
    val graticuleWidth =                MutableLiveData<Float>()                // Ширина сети координат
    val graticuleColor =                MutableLiveData<String>()               // Цвет сети координат
    val graticuleOpacity =              MutableLiveData<Int>()                  // Прозрачность сети координат
    val graticuleType =                 MutableLiveData<Int>()                  // Тип сети координат
    val hasConstellations =             MutableLiveData<Boolean>()              // Добавить ли созвездия
    val constellationsWidth =           MutableLiveData<Float>()                // Ширина линий созвездий
    val constellationsColor =           MutableLiveData<String>()               // Цвет созвездий
    val constellationsOpacity =         MutableLiveData<Int>()                  // Прозрачность созвездий
    val hasMilkyWay =                   MutableLiveData<Boolean>()              // Добавить ли млечный путь
    val starsSize =                     MutableLiveData<Float>()                // Размер звезд
    val starsColor =                    MutableLiveData<String>()               // Цвет звезд
    val starsOpacity =                  MutableLiveData<Int>()                  // Прозрачность звезд
    val hasNames =                      MutableLiveData<Boolean>()              // Добавить ли названия на звездной карте
    val namesStarsSize =                MutableLiveData<Int>()                  // Размер текста названий звезд и созвездий
    val namesStarsColor =               MutableLiveData<String>()               // Цвет текста названий звезд и созвездий
    val namesStarsLang =                MutableLiveData<Lang>()                 // Язык текста названий звезд и созвездий
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
    val fontList = arrayListOf(
        FontText("Amatic SC", R.font.amaticsc_regular, null),
        FontText("Bad Script Regular", R.font.bad_script, null),
        FontText("Caveat", R.font.caveat, null),
        FontText("Comfortaa Regular", R.font.comfortaa, null),
        FontText("Cormorant Infant Regular", R.font.cormorant_infant, null),
        FontText("Days One", R.font.days_one, null),
        FontText("Marck Script", R.font.marck_script, null),
        FontText("Nunito Regular", R.font.nunito, null),
        FontText("Lobster Regular", R.font.lobster, null),
        FontText("Kelly Slab", R.font.kelly_slab, null),
        FontText("Pacifico", R.font.pacifico_regular, null),
        FontText("Poiret One Regular", R.font.poiret_one, null),
        FontText("Press Start 2P", R.font.press_start_2p, null),
        FontText("Russo One Regular", R.font.russo_one, null),
        FontText("Seymour One", R.font.seymour_one, null),
        FontText("Under Dog Regular", R.font.underdog, null)
    )

    abstract fun drawCanvas()
    abstract fun getControllerList(): ArrayList<Controller>

    fun getShortBitmap(): Bitmap {
        val maxSize = (holst.value!!.width!!).coerceAtLeast(holst.value!!.height!!)
        val scaleFactor: Float = if(maxSize > 2000) maxSize / 2000 else 1F

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

    fun deleteProject() {
        val templateDao = AppDatabase.getDatabase(activity, CoroutineScope(Dispatchers.IO)).templateDao()
        val repository = TemplateRepository(templateDao)

        CoroutineScope(Dispatchers.IO).launch  {
            launch { deleteThumbnailProject() }
            launch { repository.deleteById(templateId!!) }
        }
    }

    fun saveToProjects(title: String) {
        val templateDao = AppDatabase.getDatabase(activity, CoroutineScope(Dispatchers.IO)).templateDao()
        val repository = TemplateRepository(templateDao)

        val imageName = if(type == DEFAULT) {
            "${name}_${Calendar.getInstance().time.time}"
        } else {
            image
        }

        val template = Template(
            id = if(type == DEFAULT) null else templateId,
            name = name,
            category = category,
            title = title,
            image = imageName,
            type = "custom",
            status = "active",
            holstWidth = holst.value!!.width,
            holstHeight = holst.value!!.height,
            holstColor = holstColor.value,
            hasBorderHolst = hasBorderHolst.value,
            borderHolstIndent = borderHolst.value!!.indent,
            borderHolstWidth = borderHolst.value!!.width,
            borderHolstColor = borderHolstColor.value,
            starMapRadius = starMapRadius.value,
            starMapPosition = starMapPosition.value,
            starMapColor = starMapColor.value,
            starMapBorderWidth = starMapBorder.value?.width,
            starMapBorderType = starMapBorder.value?.shapeType,
            starMapBorderColor = starMapBorderColor.value,
            descFontName = descFont.value!!.name,
            descFontResId = descFont.value!!.resId,
            descFontSize = descFont.value!!.size,
            descFontColor = descFontColor.value,
            descText = descText.value,
            hasEventDateInLocation = hasEventDateInLocation.value,
            eventDate = eventDate.value!!.time,
            hasEventTimeInLocation = hasEventTimeInLocation.value,
            eventTime = eventTime.value,
            hasEventCityInLocation = hasEventCityInLocation.value,
            eventLocation = eventLocation.value,
            eventCountry = eventCountry.value,
            hasEditResultLocationText = hasEditResultLocationText.value,
            resultLocationText = resultLocationText.value,
            locationFontName = locationFont.value!!.name,
            locationFontResId = locationFont.value!!.resId,
            locationFontSize = locationFont.value!!.size,
            locationFontColor = locationFontColor.value,
            hasEventCoordinatesInLocation = hasEventCoordinatesInLocation.value,
            coordinatesLatitude = coordinates.value!![0],
            coordinatesLongitude = coordinates.value!![1],
            separatorWidth = separator.value!!.width,
            separatorType = separator.value!!.shapeType,
            separatorColor = separatorColor.value,
            hasGraticule = hasGraticule.value,
            graticuleWidth = graticuleWidth.value,
            graticuleColor = graticuleColor.value,
            graticuleOpacity = graticuleOpacity.value,
            graticuleType = graticuleType.value,
            hasMilkyWay = hasMilkyWay.value,
            hasNames = hasNames.value,
            namesStarsSize = namesStarsSize.value,
            namesStarsColor = namesStarsColor.value,
            namesStarsLangLabel = namesStarsLang.value!!.label,
            namesStarsLangName = namesStarsLang.value!!.name,
            hasConstellations = hasConstellations.value,
            constellationsWidth = constellationsWidth.value,
            constellationsColor = constellationsColor.value,
            constellationsOpacity = constellationsOpacity.value,
            starsSize = starsSize.value,
            starsColor = starsColor.value,
            starsOpacity = starsOpacity.value
        )

        CoroutineScope(Dispatchers.IO).launch  {
            launch { saveThumbnailProject(imageName!!) }
            launch {
                if(type == DEFAULT)
                    repository.insert(template)
                else
                    repository.update(template)
            }
        }
    }

    private fun saveThumbnailProject(name: String) {
        val width = dpToPx(300F, activity)
        val height = dpToPx(bitmap.height / (bitmap.width / 300F), activity)

        val thumb = Bitmap.createScaledBitmap(bitmap, width, height, false)

        val pathFile = File(activity.applicationContext.filesDir.path, name)

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(pathFile)
            thumb.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                fos!!.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun deleteThumbnailProject() {
        val file = File(activity.applicationContext.filesDir.path, image!!)
        if (file.exists()) {
            file.delete()
        }
    }

    protected open fun drawCircleBorder(): Bitmap {
        val radiusMap = getRadiusMap()
        val borderWidth = (radiusMap / 4F) * starMapBorder.value!!.width / 100

        val tempSize = (radiusMap + borderWidth) * 2

        val tempBitmap = Bitmap.createBitmap(tempSize.toInt(), tempSize.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(tempBitmap)

        val mapBorder = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor(starMapBorderColor.value!!)
            isDither = true
            isAntiAlias = true
        }

        canvas.drawCircle(radiusMap + borderWidth, radiusMap + borderWidth, radiusMap + borderWidth, mapBorder)

        return tempBitmap
    }
    protected open fun drawCompassBorder(): Bitmap {
        // Радиус карты
        val radiusMap = getRadiusMap()

        // Ширина рамки
        val borderWidth = (radiusMap / 10F) * starMapBorder.value!!.width / 100

        // Ширина компаса
        val compassWidth = borderWidth * 12F

        // Длина (и высота) холста под компас
        val tempSize = (radiusMap + compassWidth) * 2F

        // Изображаение под компас
        val tempBitmap = Bitmap.createBitmap(tempSize.toInt(), tempSize.toInt(), Bitmap.Config.ARGB_8888)

        // Холст под компас
        val canvas = Canvas(tempBitmap)

        // Ширина линий
        val lineWidth = holst.value!!.width!! * 0.002F

        // Стиль линий
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = lineWidth
            color = Color.parseColor(starMapBorderColor.value!!)
            isDither = true
            isAntiAlias = true
        }

        // Координаты центра окружностей линий
        val cx = radiusMap + compassWidth
        val cy = radiusMap + compassWidth

        // Радиусы кругов
        val radiusSmall = radiusMap + lineWidth / 2F
        val radiusMiddle = radiusSmall + borderWidth * 1.9F
        val radiusBig = radiusMiddle + borderWidth * 3.3F

        // Рисуем круги
        canvas.drawCircle(cx, cy, radiusSmall, borderPaint)
        canvas.drawCircle(cx, cy, radiusMiddle, borderPaint)
        canvas.drawCircle(cx, cy, radiusBig, borderPaint)

        // Стиль букв N, E, S, W
        val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            textAlign = Paint.Align.CENTER
            textSize = compassWidth * 0.4F
            color = Color.parseColor(starMapBorderColor.value)
            typeface = ResourcesCompat.getFont(activity, R.font.cormorant_infant)
            isDither = true
            isAntiAlias = true
        }

        // Определяем размеры букв для центровки по периметру компаса
        val boundsN = Rect()
        val boundsE = Rect()
        val boundsW = Rect()
        val boundsS = Rect()
        textPaint.getTextBounds("N", 0, 1, boundsN)
        textPaint.getTextBounds("E", 0, 1, boundsE)
        textPaint.getTextBounds("W", 0, 1, boundsW)
        textPaint.getTextBounds("S", 0, 1, boundsS)

        // Стиль штрихов
        val ticksPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor(starMapBorderColor.value!!)
            isDither = true
            isAntiAlias = true
        }

        // Количество штрихов и их размеры (3 вида)
        val ticks = 360
        val littleBarWidth = lineWidth * 0.6F
        val littleBarHeight = (radiusBig - radiusMiddle) / 2
        val middleBarWidth = lineWidth * 0.8F
        val middleBarHeight = radiusBig - radiusMiddle
        val bigBarWidth = lineWidth * 1F
        val bigBarHeight = (radiusBig - radiusMiddle) * 1.4F

        // Рисуем буквы отталкиваясь от размеров больших штрихов
        canvas.drawText("N", tempSize / 2, boundsN.height().toFloat(), textPaint)
        canvas.drawText("E", tempSize / 2 - radiusMiddle - bigBarHeight - boundsE.width() - boundsN.width() / 2, radiusSmall + compassWidth + boundsE.height() / 2, textPaint)
        canvas.drawText("W", tempSize / 2 + radiusMiddle + bigBarHeight + boundsN.width(), radiusSmall + compassWidth + boundsW.height() / 2, textPaint)
        canvas.drawText("S", tempSize / 2, tempSize / 2 + radiusMiddle + bigBarHeight + boundsN.width() / 2 + boundsS.height(), textPaint)

        // Перемещаем рисование к центру компаса
        canvas.translate(tempSize / 2, tempSize / 2)

        // Рисуем штрихи (3 видов) путем вращения и рисваония вертикальных линий (штрихов)
        var i = 0F
        while (i < 360) {
            canvas.save()
            canvas.rotate(i)
            canvas.translate(0F, radiusMiddle)

            when(true) {
                i % 90F == 0F -> canvas.drawRect(- bigBarWidth / 2F, bigBarHeight, bigBarWidth / 2F, 0F, ticksPaint)
                i % 10F == 0F -> canvas.drawRect(- middleBarWidth / 2F, middleBarHeight, middleBarWidth / 2F, 0F, ticksPaint)

                else -> canvas.drawRect(- littleBarWidth / 2F, littleBarHeight, littleBarWidth / 2F, 0F, ticksPaint)
            }

            canvas.restore()

            i += 360 / ticks
        }

        // Возвращаем изображение компаса
        return tempBitmap
    }

    protected fun drawLineSeparator(): Bitmap {
        val separatorWidth = holst.value!!.width!! * separator.value!!.width / 100
        val separatorHeight = (separatorWidth * 0.01F).coerceAtLeast(1F)

        val separatorLine = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            isDither = true
            isAntiAlias = true
            strokeWidth = separatorHeight
            color = Color.parseColor(separatorColor.value!!)
        }

        val tempBitmap = Bitmap.createBitmap(separatorWidth.toInt(), separatorHeight.toInt(), Bitmap.Config.ARGB_8888)

        Canvas(tempBitmap).drawLine(0F, 0F, separatorWidth, 0F, separatorLine)

        return tempBitmap
    }
    protected fun drawDrawableSizedSeparator(shapeType: Int, viewportWidth: Float, viewportHeight: Float, scale: Float): Bitmap {
        val separatorWidth = holst.value!!.width!! * separator.value!!.width / 100 * scale
        val separatorHeight = separatorWidth * (viewportHeight/viewportWidth)

        val tempBitmap = Bitmap.createBitmap(separatorWidth.toInt(), separatorHeight.toInt(), Bitmap.Config.ARGB_8888)

        // TODO: Дублирвоание кода, drawable уже указывается при создании списка шейпов в адапторе
        val drawableId = when(shapeType) {
            STARS -> R.drawable.ic_shape_stars
            HEARTS -> R.drawable.ic_shape_hearts
            CURVED -> R.drawable.ic_shape_curved
            STAR -> R.drawable.ic_shape_star
            HEART -> R.drawable.ic_shape_heart

            else -> R.drawable.ic_shape_star
        }

        val canvas = Canvas(tempBitmap)
        val svg = ContextCompat.getDrawable(activity, drawableId)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            svg!!.colorFilter = BlendModeColorFilter(Color.parseColor(separatorColor.value), BlendMode.SRC_ATOP)
        } else {
            svg!!.setColorFilter(Color.parseColor(separatorColor.value), PorterDuff.Mode.SRC_ATOP)
        }

        svg.setBounds(0, 0, separatorWidth.toInt(), separatorHeight.toInt())
        svg.draw(canvas)

        return tempBitmap
    }

    protected open fun getRadiusMap(): Float {
        return ((holst.value!!.width!! / 1.5F) * starMapRadius.value!! / 100).coerceAtLeast(1F)
    }

    companion object {
        const val LINE_GRATICULE = 1
        const val DASHED_GRATICULE = 2
        const val DEFAULT = 3
        const val CUSTOM = 4
    }
}

