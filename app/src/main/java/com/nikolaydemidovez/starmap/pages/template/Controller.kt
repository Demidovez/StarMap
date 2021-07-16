package com.nikolaydemidovez.starmap.pages.template

import android.graphics.drawable.Drawable

data class Controller(val name: String?, val title: String?, val drawable: Drawable?) {
    constructor() : this(null,null, null)
}