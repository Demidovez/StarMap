package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

abstract class TemplateView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    protected var backgroundColorCanvas: Int = 0
    protected var canvasBorderColor: Int = 0

    var canvasWidth: Float = 0F
        private set
    var canvasHeight: Float = 0F
        private set


    fun updateBackgroundColorCanvas(color: Int) {
        backgroundColorCanvas = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }

    fun updateCanvasBorderColor(color: Int) {
        canvasBorderColor = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }

    fun updateCanvasSize(width: Float, height: Float) {
        canvasWidth = width
        canvasHeight = height

        invalidate()
    }
}