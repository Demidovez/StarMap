package com.kalambur.mappy_stars.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.FieldPosition

@Entity
data class Template(
    @PrimaryKey(autoGenerate=true)
    val id:Int? = null,
    val name: String? = null,
    val title: String? = null,
    val image: String? = null,
    val type: String? = null,
    val category: String? = null,
    val status: String? = null,
    val holstWidth: Float? = null,
    val holstHeight: Float? = null,
    val holstColor: String? = null,
    val hasBorderHolst: Boolean? = null,
    val borderHolstIndent: Float? = null,
    val borderHolstWidth: Float? = null,
    val borderHolstColor: String? = null,
    val starMapRadius: Float? = null,
    val starMapPosition: Float? = null,
    val starMapColor: String? = null,
    val starMapBorderWidth: Float? = null,
    val starMapBorderType: Int? = null,
    val starMapBorderColor: String? = null,
    val descFontName: String? = null,
    val descFontResId: Int? = null,
    val descFontSize: Float? = null,
    val descFontColor: String? = null,
    val descText: String? = null,
    val hasEventDateInLocation: Boolean? = null,
    val eventDate: Long? = null,
    val hasEventTimeInLocation: Boolean? = null,
    val eventTime: String? = null,
    val hasEventCityInLocation: Boolean? = null,
    val eventLocation: String? = null,
    val eventCountry: String? = null,
    val hasEditResultLocationText: Boolean? = null,
    val resultLocationText: String? = null,
    val locationFontName: String? = null,
    val locationFontResId: Int? = null,
    val locationFontSize: Float? = null,
    val locationFontColor: String? = null,
    val hasEventCoordinatesInLocation: Boolean? = null,
    val coordinatesLatitude: Float? = null,
    val coordinatesLongitude: Float? = null,
    val separatorWidth: Float? = null,
    val separatorType: Int? = null,
    val separatorColor: String? = null,
    val hasGraticule: Boolean? = null,
    val graticuleWidth: Float? = null,
    val graticuleColor: String? = null,
    val graticuleOpacity: Int? = null,
    val graticuleType: Int? = null,
    val hasMilkyWay: Boolean? = null,
    val hasNames: Boolean? = null,
    val namesStarsSize: Int? = null,
    val namesStarsColor: String? = null,
    val namesStarsLangLabel: String? = null,
    val namesStarsLangName: String? = null,
    val hasConstellations: Boolean? = null,
    val constellationsWidth: Float? = null,
    val constellationsColor: String? = null,
    val constellationsOpacity: Int? = null,
    val starsSize: Float? = null,
    val starsColor: String? = null,
    val starsOpacity: Int? = null
)