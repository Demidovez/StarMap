package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class StarMap (var radius: Float?, override var color: String?): PropertiesHasInterface {
    constructor() : this(null, null)
}