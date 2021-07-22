package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng
import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class Holst (var title: String?, var subTitle: String?, var width: Float?, var height: Float?, override var color: String?):
    PropertiesHasInterface {
    constructor() : this(null,null, null, null, null)
}