package com.nikolaydemidovez.starmap.pojo

class ShapeMapBorder (var title: String?, var iconId: Int?, var type: Int?) {
    constructor() : this(null,null, null)

    companion object {
        const val NONE = 1
        const val CIRCLE = 2
        const val COMPASS = 3
    }
}