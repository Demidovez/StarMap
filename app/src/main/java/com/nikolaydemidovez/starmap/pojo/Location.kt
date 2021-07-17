package com.nikolaydemidovez.starmap.pojo

import com.google.android.gms.maps.model.LatLng

class Location (val location: String?, val country: String?, val placeId: String?) {
    constructor() : this(null,null, null)
}