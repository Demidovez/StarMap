package com.nikolaydemidovez.starmap.adapters

import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.ColorItemBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidColor


class ColorAdapter(private val templateCanvas: TemplateCanvas): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var colorList = arrayListOf<String>()

    class ColorHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ColorItemBinding.bind(item)

        fun bind(color: String, templateCanvas: TemplateCanvas) = with(binding) {

            val borderColor = if(color == templateCanvas.locationFont.value!!.color) {
                ContextCompat.getColor(itemView.context, R.color.dark)
            } else {
                Color.parseColor("#FFFFFF")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                labelColor.background.colorFilter = BlendModeColorFilter(borderColor, BlendMode.SRC_ATOP)
            } else {
                labelColor.background.setColorFilter(borderColor, PorterDuff.Mode.SRC_ATOP)
            }

            val circleColor = if(color == "#FFFFFF") {
                Color.parseColor("#ECF0F1")
            } else {
                Color.parseColor(color)
            }

            labelColor.setColorFilter(circleColor, PorterDuff.Mode.SRC_ATOP)

            labelColor.setOnClickListener {
                val newFont = templateCanvas.locationFont.value
                newFont?.color = color

                templateCanvas.locationFont.value = newFont
            }
        }
    }

    class PickerHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ColorItemBinding.bind(item)

        fun bind(color: String, templateCanvas: TemplateCanvas) = with(binding) {

            labelColor.setOnClickListener {
                val newFont = templateCanvas.locationFont.value
                newFont?.color = "#FF0000"

                templateCanvas.locationFont.value = newFont
            }
        }
    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.color_item, parent, false)
//        return ColorHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ColorHolder, position: Int) {
//        holder.bind(colorList[position], templateCanvas)
//    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return if (viewType == COLOR) {
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.color_item, viewGroup, false)
            ColorHolder(view)
        } else {
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.color_item, viewGroup, false)
            PickerHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == COLOR) {
            (viewHolder as ColorHolder).bind(colorList[position], templateCanvas)
        } else {
            (viewHolder as PickerHolder).bind(colorList[position], templateCanvas)
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun getItemViewType(position: Int): Int {
        Log.d("MyLog", isValidColor(colorList[position]).toString())
        return if (isValidColor(colorList[position])) {
            COLOR
        } else {
            PICKER
        }
    }

    fun addAllColorList(list: ArrayList<String>) {
        colorList = list
        notifyDataSetChanged()
    }

    companion object {
        const val COLOR = 1
        const val PICKER = 2
    }

}