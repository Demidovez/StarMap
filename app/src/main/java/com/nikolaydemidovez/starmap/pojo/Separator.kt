package com.nikolaydemidovez.starmap.pojo

import com.nikolaydemidovez.starmap.interfaces.PropertiesHasInterface

class Separator (override var color: String?, var width: Float?): PropertiesHasInterface {
    constructor() : this(null, null)
}