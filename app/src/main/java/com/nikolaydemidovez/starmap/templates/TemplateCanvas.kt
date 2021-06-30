package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.res.ResourcesCompat

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

    interface OnDrawListener {
        fun onDraw()
    }

    fun setOnDrawListener(listener: OnDrawListener) {
        this.listener = listener

        this.listener!!.onDraw()
    }

    abstract fun draw()
}