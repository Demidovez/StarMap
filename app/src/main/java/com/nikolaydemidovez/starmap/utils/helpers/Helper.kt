package com.nikolaydemidovez.starmap.utils.helpers

import android.location.Location

class Helper {
    companion object {
        fun convert(latitude: Double, longitude: Double): String {
            val builder = StringBuilder()

            if (latitude < 0) {
                builder.append("S ")
            } else {
                builder.append("N ")
            }

            val latitudeDegrees: String =
                Location.convert(Math.abs(latitude), Location.FORMAT_SECONDS)
            val latitudeSplit = latitudeDegrees.split(":").toTypedArray()

            builder.append(latitudeSplit[0])
            builder.append("°")
            builder.append(latitudeSplit[1])
            builder.append("'")
            builder.append(String.format("%.1f", latitudeSplit[2].toDouble()))
            builder.append("\"")

            builder.append(", ")

            if (longitude < 0) {
                builder.append("W ")
            } else {
                builder.append("E ")
            }

            val longitudeDegrees: String =
                Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS)
            val longitudeSplit = longitudeDegrees.split(":").toTypedArray()

            builder.append(longitudeSplit[0])
            builder.append("°")
            builder.append(longitudeSplit[1])
            builder.append("'")
            builder.append(String.format("%.1f", longitudeSplit[2].toDouble()))
            builder.append("\"")


            return builder.toString()
        }
    }
}