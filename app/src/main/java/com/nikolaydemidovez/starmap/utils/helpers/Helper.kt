package com.nikolaydemidovez.starmap.utils.helpers

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.location.Location
import kotlin.math.abs
import android.util.TypedValue
import com.nikolaydemidovez.starmap.adapters.ColorAdapter
import java.util.regex.Matcher
import java.util.regex.Pattern


class Helper {
    companion object {
        // Конверт координат в виде N 12°12'32.3'
        fun convert(latitude: Double, longitude: Double): String {
            val builder = StringBuilder()

            if (latitude < 0) {
                builder.append("S ")
            } else {
                builder.append("N ")
            }

            val latitudeDegrees: String = Location.convert(abs(latitude), Location.FORMAT_SECONDS)
            val latitudeSplit = latitudeDegrees.replace(",", ".").split(":").toTypedArray()

            builder.append(latitudeSplit[0])
            builder.append("°")
            builder.append(latitudeSplit[1])
            builder.append("'")
            builder.append(String.format("%.1f", latitudeSplit[2].toDoubleOrNull()).replace(",", "."))
            builder.append("\"")

            builder.append(", ")

            if (longitude < 0) {
                builder.append("W ")
            } else {
                builder.append("E ")
            }

            val longitudeDegrees: String =
                Location.convert(abs(longitude), Location.FORMAT_SECONDS)
            val longitudeSplit = longitudeDegrees.replace(",", ".").split(":").toTypedArray()

            builder.append(longitudeSplit[0])
            builder.append("°")
            builder.append(longitudeSplit[1])
            builder.append("'")
            builder.append(String.format("%.1f", longitudeSplit[2].toDoubleOrNull()).replace(",", "."))
            builder.append("\"")


            return builder.toString()
        }

        // Bitmap из прямоугольной формы в круглую
        fun getBitmapClippedCircle(bitmap: Bitmap): Bitmap? {
            val width = bitmap.width
            val height = bitmap.height

            val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val path = Path()

            path.addCircle(
                (width / 2).toFloat(),
                (height / 2).toFloat(),
                width.coerceAtMost(height / 2).toFloat(),
                Path.Direction.CCW
            )

            val canvas = Canvas(outputBitmap)
            canvas.clipPath(path)
            canvas.drawBitmap(bitmap, 0F, 0F, null)

            return outputBitmap
        }

        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }

        // Проверка на валидность широты
        fun isValidLat(latitude: Double?): Boolean {
            return latitude?.toInt() in -90 until 90
        }

        // Проверка на валидность долготы
        fun isValidLong(longitude: Double?): Boolean {
            return longitude?.toInt() in -180 until 180
        }

        // Проверка на валидность строки как значение цвета
        fun isValidColor(color: String): Boolean {
            val colorPattern: Pattern = Pattern.compile("#([0-9a-f]{3}|[0-9a-f]{6}|[0-9a-f]{8})")
            val m: Matcher = colorPattern.matcher(color)

            return m.matches()
        }
    }
}