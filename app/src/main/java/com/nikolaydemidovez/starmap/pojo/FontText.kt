package com.nikolaydemidovez.starmap.pojo

import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class FontText (var name: String?, var resId: Int?, override var color: String?, var size: Float?): PropertiesHasInterface {
    constructor() : this(null,null,null, null)


}