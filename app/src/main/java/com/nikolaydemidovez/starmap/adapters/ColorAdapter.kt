package adapters

import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import android.graphics.*
import android.view.*
import android.widget.*
import com.nikolaydemidovez.starmap.R
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.MutableLiveData
import com.nikolaydemidovez.starmap.databinding.ColorItemBinding
import com.nikolaydemidovez.starmap.utils.extensions.hideKeyboard
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.dpToPx
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidColor
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener
import com.skydoves.colorpickerview.ColorPickerView
import com.skydoves.colorpickerview.sliders.BrightnessSlideBar

class ColorAdapter(
    private val mutableColor: MutableLiveData<String>,
    private val listener: (color: String) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var colorList = arrayListOf<String>()

    class ColorHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ColorItemBinding.bind(item)

        fun bind(color: String, mutableColor: MutableLiveData<String>, listener: (color: String) -> Unit) = with(binding) {

            val borderColor = if(color == mutableColor.value!!) {
                ContextCompat.getColor(itemView.context, R.color.dark)
            } else {
                Color.parseColor("#FFFFFF")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageColor.background.colorFilter = BlendModeColorFilter(borderColor, BlendMode.SRC_ATOP)
            } else {
                imageColor.background.setColorFilter(borderColor, PorterDuff.Mode.SRC_ATOP)
            }

            val circleColor = if(color == "#FFFFFF") {
                Color.parseColor("#ECF0F1")
            } else {
                Color.parseColor(color)
            }

            imageColor.setColorFilter(circleColor, PorterDuff.Mode.SRC_ATOP)

            rootItemColor.setOnClickListener {
                listener(color)
            }
        }
    }

    class PickerHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ColorItemBinding.bind(item)

        fun bind(colorList: ArrayList<String>, mutableColor: MutableLiveData<String>, listener: (color: String) -> Unit) = with(binding) {
            rootItemColor.updatePadding(left = dpToPx(16F, itemView.context))

            val borderColor = if(!colorList.contains(mutableColor.value!!)) {
                ContextCompat.getColor(itemView.context, R.color.dark)
            } else {
                Color.parseColor("#FFFFFF")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                imageColor.background.colorFilter = BlendModeColorFilter(borderColor, BlendMode.SRC_ATOP)
            } else {
                imageColor.background.setColorFilter(borderColor, PorterDuff.Mode.SRC_ATOP)
            }

            imageColor.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_color_picker))

            rootItemColor.setOnClickListener {
                showColorPicker(mutableColor, listener)
            }
        }

        private fun showColorPicker(mutableColor: MutableLiveData<String>, listener: (color: String) -> Unit) {
            val layoutInflater = LayoutInflater.from(itemView.context)
            val layout: View = layoutInflater.inflate(R.layout.picker_color_layout, null)
            layout.findViewById<TextView>(R.id.title).text = "Цвет текста"

            val colorPickerView    = layout.findViewById<ColorPickerView>(R.id.colorPickerView)
            val brightnessSlideBar = layout.findViewById<BrightnessSlideBar>(R.id.brightnessSlide)
            val colorPreview       = layout.findViewById<ImageView>(R.id.color_preview)
            val inputColor         = layout.findViewById<EditText>(R.id.input_color)
            val descText           = layout.findViewById<TextView>(R.id.desc_text)
            val imgClearText       = layout.findViewById<ImageView>(R.id.clear_text)

            colorPickerView.attachBrightnessSlider(brightnessSlideBar)
            colorPickerView.setInitialColor(Color.parseColor(mutableColor.value!!))

            colorPickerView.setColorListener(ColorEnvelopeListener { envelope, fromUser ->
                colorPreview.setColorFilter(envelope.color, PorterDuff.Mode.SRC_ATOP)

                if(!inputColor.isFocused) {
                    val colorText = "#" + envelope.hexCode.substring(2)
                    inputColor.setText(colorText)
                }

                if(fromUser) {
                    inputColor.clearFocus()
                    inputColor.hideKeyboard()
                }
            })

            inputColor.doOnTextChanged { text, _, _, count ->
                if(inputColor.text.isNotEmpty()) {
                    imgClearText.visibility = View.VISIBLE
                } else {
                    imgClearText.visibility = View.GONE
                }

                if(inputColor.isFocused && isValidColor(inputColor.text.toString())) {
                    colorPickerView.setInitialColor(Color.parseColor(inputColor.text.toString()))
                }

                descText.text = ""
            }


            imgClearText.setOnClickListener {
                inputColor.setText("")
            }

            val builder = AlertDialog.Builder(itemView.context, R.style.dialog_corners)
            builder.setPositiveButton(android.R.string.ok, null)
            builder.setNegativeButton(android.R.string.cancel, null)
            builder.setView(layout)

            val dialog = builder.create()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                dialog.window?.setDecorFitsSystemWindows(false)
                dialog.window?.decorView!!.setOnApplyWindowInsetsListener { v, insets ->
                    val imeInsets: Insets = insets.getInsets(WindowInsets.Type.ime())
                    val paddingBottom = if(imeInsets.bottom == 0) 40 else imeInsets.bottom
                    v.updatePadding(bottom = paddingBottom)
                    insets
                }
            } else {
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
            }

            dialog.setOnShowListener {
                dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(itemView.context, R.color.dark))

                val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                okButton.setTextColor(ContextCompat.getColor(itemView.context, R.color.dark))
                okButton.setOnClickListener {
                    val color = inputColor.text.toString()

                    if(color.isNotEmpty()) {
                        if(isValidColor(color)) {
                            listener(color)

                            dialog.dismiss()
                        } else {
                            descText.text = "Неверное значение!"
                        }
                    } else {
                        descText.text = "Введите текст!"
                    }
                }
            }

            dialog.show()

            val width = (itemView.resources.displayMetrics.widthPixels * 0.9).toInt()
            dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        }
    }

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
            (viewHolder as ColorHolder).bind(colorList[position], mutableColor, listener)
        } else {
            (viewHolder as PickerHolder).bind(colorList, mutableColor, listener)
        }
    }

    override fun getItemCount(): Int {
        return colorList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (colorList[position].isNotEmpty()) {
            COLOR
        } else {
            PICKER
        }
    }

    fun addAllColorList(list: ArrayList<String>) {
        colorList.add("")
        colorList.addAll(list)

        notifyDataSetChanged()
    }

    companion object {
        const val COLOR = 1
        const val PICKER = 2
    }
}