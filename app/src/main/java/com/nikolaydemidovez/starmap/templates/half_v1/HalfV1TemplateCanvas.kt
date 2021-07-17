package com.nikolaydemidovez.starmap.templates.half_v1

import android.graphics.*
import androidx.core.content.ContextCompat
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.pojo.Controller
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class HalfV1TemplateCanvas(private val activity: MainActivity) : TemplateCanvas(activity) {
    private val STROKE_WIDTH = 12f
    private var holst: Paint
    private var border: Paint

    init {
        //backgroundColorCanvas = ResourcesCompat.getColor(activity.applicationContext.resources, R.color.white, null)

        holst = Paint().apply {
            style = Paint.Style.FILL
        }

        border = Paint().apply {
            style = Paint.Style.STROKE
            strokeWidth = STROKE_WIDTH
        }

        draw()
    }

    override fun draw() {
        bitmap = Bitmap.createBitmap(
            canvasWidth.value!!.toInt(),
            canvasHeight.value!!.toInt(),
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)

        //holst.color = backgroundColorCanvas
        border.color = canvasBorderColor.value!!

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth.value!!, canvasHeight.value!!, holst)

        // Рисуем рамку
        canvas.drawRect(40F, 40F, canvasWidth.value!! - 40F, canvasHeight.value!! - 40F, border)

        listener?.onDraw()
    }

    override fun getControllerList(): ArrayList<Controller> {
        return arrayListOf(
            Controller("event_v1",     "Событие",     ContextCompat.getDrawable(activity,R.drawable.ic_event_v1)),
            Controller("canvas_v1",    "Холст",       ContextCompat.getDrawable(activity,R.drawable.ic_canvas_v1)),
            Controller("map_v1",       "Карта",       ContextCompat.getDrawable(activity,R.drawable.ic_map_v1)),
            Controller("stars_v1",     "Звезды",      ContextCompat.getDrawable(activity,R.drawable.ic_stars_v1)),
            Controller("desc_v1",      "Текст",       ContextCompat.getDrawable(activity,R.drawable.ic_desc_v1)),
            Controller("separator_v1", "Разделитель", ContextCompat.getDrawable(activity,R.drawable.ic_separator_v1)),
            Controller("location_v1",  "Локация",     ContextCompat.getDrawable(activity,R.drawable.ic_location_v1)),
            Controller("save_v1",      "Сохранение",  ContextCompat.getDrawable(activity,R.drawable.ic_save_v1)),
        )
    }
}