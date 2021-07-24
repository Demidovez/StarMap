package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class StarMapBorder (var width: Float?, override var color: String?, var shape: Int?): PropertiesHasInterface {
    constructor() : this(null, null, null)

    companion object {
        val LINE = 1
        val KOMPAS = 2
    }
}