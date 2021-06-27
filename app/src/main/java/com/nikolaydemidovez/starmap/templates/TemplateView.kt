package com.nikolaydemidovez.starmap.templates

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat

open class TemplateView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    protected var backgroundColorCanvas: Int = 0
    protected var canvasBorderColor: Int = 0

    fun setBackgroundCanvasColor(color: Int) {
        backgroundColorCanvas = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }

    fun setCanvasColorBorder(color: Int) {
        canvasBorderColor = ResourcesCompat.getColor(resources, color, null)
        invalidate()
    }
}