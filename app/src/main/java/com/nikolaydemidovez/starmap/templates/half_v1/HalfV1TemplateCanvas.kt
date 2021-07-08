package com.nikolaydemidovez.starmap.templates.half_v1

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
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

        val canvas = Canvas(bitmap!!)

        //holst.color = backgroundColorCanvas
        border.color = canvasBorderColor.value!!

        // Рисуем холст
        canvas.drawRect(0F, 0F, canvasWidth.value!!, canvasHeight.value!!, holst)

        // Рисуем рамку
        canvas.drawRect(40F, 40F, canvasWidth.value!! - 40F, canvasHeight.value!! - 40F, border)

        listener?.onDraw()
    }
}