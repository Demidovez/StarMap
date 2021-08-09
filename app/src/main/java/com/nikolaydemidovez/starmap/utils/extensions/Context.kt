package com.nikolaydemidovez.starmap.utils.extensions

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.os.Build
import android.text.*
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.collection.lruCache
import androidx.core.graphics.withTranslation

fun Context.resIdByName(resIdName: String?, resType: String): Int {
    resIdName?.let {
        return resources.getIdentifier(it, resType, packageName)
    }
    throw Resources.NotFoundException()
}