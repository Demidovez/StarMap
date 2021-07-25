package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class NamesStars (var size: Int?, override var color: String?, var lang: Lang?): PropertiesHasInterface {
    constructor() : this(null,null, null)
}