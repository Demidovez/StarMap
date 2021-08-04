package com.nikolaydemidovez.starmap.pojo

import com.nikolaydemidovez.starmap.interfaces.ShapeInterface

class ShapeMapBorder (override var iconId: Int, override var type: Int): ShapeInterface {
    companion object {
        const val NONE = 1
        const val CIRCLE = 2
        const val COMPASS = 3
    }
}