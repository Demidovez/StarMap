package com.kalambur.mappy_stars.pojo

import com.kalambur.mappy_stars.interfaces.ShapeInterface

class ShapeMapBorder (override var iconId: Int, override var type: Int): ShapeInterface {
    companion object {
        const val NONE = 1
        const val CIRCLE = 2
        const val COMPASS = 3
    }
}