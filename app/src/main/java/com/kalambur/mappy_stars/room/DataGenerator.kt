package com.kalambur.mappy_stars.room

import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.pojo.ShapeMapBorder
import com.kalambur.mappy_stars.pojo.ShapeSeparator
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.templates.TemplateCanvas

class DataGenerator {
    companion object {
        fun getTemplates(): List<Template>{
            return listOf(
                Template(
                    name = "classic_v1",
                    category = "Классика",
                    title = null,
                    image = "classic",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = true,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 50F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 15F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 50F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись\nвсе звезды вселенной...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 35F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 35F,
                    separatorType = ShapeSeparator.CURVED,
                    separatorColor = "#000000",
                    hasGraticule = false,
                    graticuleWidth = 10F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 40,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 10F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 50F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                ),
                Template(
                    name = "half_v1",
                    category = "Полусфера",
                    title = null,
                    image = "half",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = false,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 3508F / 2,
                    starMapPosition = 80F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 30F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 40F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись звезды...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 35F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 35F,
                    separatorType = ShapeSeparator.NONE,
                    separatorColor = "#000000",
                    hasGraticule = false,
                    graticuleWidth = 10F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 40,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 10F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 50F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                ),
                Template(
                    name = "polaroid_v1",
                    category = "Полароид",
                    title = null,
                    image = "polaroid",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = false,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 35F,
                    starMapPosition = 85F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 30F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 40F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись звезды...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 35F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 35F,
                    separatorType = ShapeSeparator.NONE,
                    separatorColor = "#000000",
                    hasGraticule = false,
                    graticuleWidth = 10F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 40,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 10F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 50F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                ),
                Template(
                    name = "full_v1",
                    category = "Полная",
                    title = null,
                    image = "full",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = false,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 35F,
                    starMapPosition = 85F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 30F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 40F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись звезды...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 35F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 35F,
                    separatorType = ShapeSeparator.NONE,
                    separatorColor = "#000000",
                    hasGraticule = false,
                    graticuleWidth = 10F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 40,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 10F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 50F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                ),
                Template(
                    name = "starworld_v1",
                    category = "Звездный мир",
                    title = null,
                    image = "starworld",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = true,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 900F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 15F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 120F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись\nвсе звезды вселенной...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 60F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 1000F,
                    separatorType = ShapeSeparator.CURVED,
                    separatorColor = "#000000",
                    hasGraticule = true,
                    graticuleWidth = 2F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 12,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 3F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 17F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                ),
                Template(
                    name = "moon_v1",
                    category = "Луна",
                    title = null,
                    image = "moon",
                    type = "default",
                    status = "active",
                    holstWidth = 2480F,
                    holstHeight = 3508F,
                    holstColor = "#FFFFFF",
                    hasBorderHolst = true,
                    borderHolstIndent = 20F,
                    borderHolstWidth = 5F,
                    borderHolstColor = "#000000",
                    starMapRadius = 900F,
                    starMapColor = "#000000",
                    starMapBorderWidth = 15F,
                    starMapBorderType = ShapeMapBorder.NONE,
                    starMapBorderColor = "#000000",
                    descFontName = "Comfortaa Regular",
                    descFontResId = R.font.comfortaa,
                    descFontSize = 120F,
                    descFontColor = "#000000",
                    descText = "День, когда сошлись\nвсе звезды вселенной...",
                    hasEventDateInLocation = true,
                    eventDate = null,
                    hasEventTimeInLocation = true,
                    eventTime = null,
                    hasEventCityInLocation = true,
                    eventLocation = "Москва",
                    eventCountry = "Россия",
                    hasEditResultLocationText = false,
                    resultLocationText = null,
                    locationFontName = "Comfortaa Regular",
                    locationFontResId = R.font.comfortaa,
                    locationFontSize = 60F,
                    locationFontColor = "#000000",
                    hasEventCoordinatesInLocation = true,
                    coordinatesLatitude = 55.755826F,
                    coordinatesLongitude = 37.6173F,
                    separatorWidth = 1000F,
                    separatorType = ShapeSeparator.CURVED,
                    separatorColor = "#000000",
                    hasGraticule = true,
                    graticuleWidth = 2F,
                    graticuleColor = "#FFFFFF",
                    graticuleOpacity = 70,
                    graticuleType = TemplateCanvas.LINE_GRATICULE,
                    hasMilkyWay = true,
                    hasNames = true,
                    namesStarsSize = 12,
                    namesStarsColor = "#FFFFFF",
                    namesStarsLangLabel = "Русский",
                    namesStarsLangName = "ru",
                    hasConstellations = true,
                    constellationsWidth = 3F,
                    constellationsColor = "#FFFFFF",
                    constellationsOpacity = 100,
                    starsSize = 17F,
                    starsColor = "#FFFFFF",
                    starsOpacity = 100
                )
            )
        }
    }
}