package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class HolstBorder (var indent: Float?, var width: Float?, override var color: String?): PropertiesHasInterface {
    constructor() : this( null, null, null)
}