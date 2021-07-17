package com.nikolaydemidovez.starmap.pojo

data class Template(val id: String?, val name: String?, val title: String?, val image: String?) {
    constructor() : this(null,null,null, null)
}
