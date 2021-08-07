package com.nikolaydemidovez.starmap.utils.helpers

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Path
import android.location.Location
import kotlin.math.abs
import android.util.TypedValue
import java.util.regex.Matcher
import java.util.regex.Pattern
import android.view.MotionEvent

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.pojo.FontText
import java.util.ArrayList


class Helper {
    companion object {
        // Конверт координат в виде N 12°12'32.3'
        fun convert(latitude: Float, longitude: Float): String {
            val builder = StringBuilder()

            if (latitude < 0) {
                builder.append("S ")
            } else {
                builder.append("N ")
            }

            val latitudeDegrees: String = Location.convert(abs(latitude.toDouble()), Location.FORMAT_SECONDS)
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
                Location.convert(abs(longitude.toDouble()), Location.FORMAT_SECONDS)
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

        // Конверт из dp в px
        fun dpToPx(dp: Float, context: Context): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()
        }

        // Проверка на валидность широты
        fun isValidLat(latitude: Float?): Boolean {
            return latitude?.toInt() in -90 until 90
        }

        // Проверка на валидность долготы
        fun isValidLong(longitude: Float?): Boolean {
            return longitude?.toInt() in -180 until 180
        }

        // Проверка на валидность цвета
        fun isValidColor(color: String): Boolean {
            val colorPattern: Pattern = Pattern.compile("#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})\\b")
            val m: Matcher = colorPattern.matcher(color)

            return m.find()
        }

        // Тусклый цвет, если элемент управления заблокирован
        fun shadowAlpha(isEnabled: Boolean): Float {
            return if(isEnabled) 1F else 0.6F
        }

        // Класс, для отключения recyclerview если элемент управления заблокирован
        class RecyclerViewDisabler(isEnable: Boolean) : OnItemTouchListener {
            var isEnable = true

            override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
                return !isEnable
            }

            override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {}
            override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

            init {
                this.isEnable = isEnable
            }
        }

        // Список шрифтов
        fun getAllFonts(activity: Activity): ArrayList<FontText> {
            val allFonts: ArrayList<FontText> = ArrayList()
            val fontList = activity.resources.getStringArray(R.array.font_in_locations)

            for (font in fontList) {
                val fontResID   = activity.resources.getIdentifier(font, "font", activity.packageName)
                val stringResID = activity.resources.getIdentifier(font, "string", activity.packageName)
                val fontName    = activity.resources.getString(stringResID)

                try {
                    allFonts.add(FontText(fontName, fontResID, null))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            return allFonts
        }
    }
}