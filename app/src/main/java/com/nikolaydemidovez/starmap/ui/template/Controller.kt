package com.nikolaydemidovez.starmap.ui.template

data class Controller(val id: String?, val name: String?, val title: String?, val image: String?) {
    constructor() : this(null,null,null, null)
}