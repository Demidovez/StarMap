package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

abstract class TemplateView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    protected var backgroundColorCanvas: Int = 0
    protected var canvasBorderColor: Int = 0

    protected var factorIncrease: Float = 1F

    var canvasOriginalWidth: Float = 0F
        private set
    var canvasOriginalHeight: Float = 0F
        private set

    protected var canvasWidth: Float = 0F
    protected var canvasHeight: Float = 0F

    protected var containerWidth: Float = 0F
    protected var containerHeight: Float = 0F

    protected var startX: Float = 0F
    protected var startY: Float = 0F

    fun updateBackgroundColorCanvas(color: Int) {
        backgroundColorCanvas = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }

    fun updateCanvasBorderColor(color: Int) {
        canvasBorderColor = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }

    fun updateCanvasOriginalSize(width: Float, height: Float) {
        canvasOriginalWidth = width
        canvasOriginalHeight = height

        factorIncrease = canvasOriginalHeight / containerHeight

        canvasWidth = canvasOriginalWidth / factorIncrease
        canvasHeight = canvasOriginalHeight / factorIncrease

        startX = (containerWidth - canvasWidth) / 2

        invalidate()
    }

    fun updateContainerSize(width: Float, height: Float) {
        containerWidth = width
        containerHeight = height

        invalidate()
    }
}