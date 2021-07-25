package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class Constellations (var width: Float?, override var color: String?, var opacity: Int?): PropertiesHasInterface {
    constructor() : this(null,null, null)
}