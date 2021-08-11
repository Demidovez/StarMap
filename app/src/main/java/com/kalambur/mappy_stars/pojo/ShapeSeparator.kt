package com.kalambur.mappy_stars.pojo

import com.kalambur.mappy_stars.interfaces.ShapeInterface

class ShapeSeparator (override var iconId: Int, override var type: Int): ShapeInterface {
    companion object {
        const val NONE = 1
        const val LINE = 2
        const val CURVED = 3
        const val STAR = 4
        const val STARS = 5
        const val HEART = 6
        const val HEARTS = 7
    }
}