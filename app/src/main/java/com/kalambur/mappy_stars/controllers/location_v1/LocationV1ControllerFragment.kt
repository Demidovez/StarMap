package com.kalambur.mappy_stars.controllers.location_v1

import android.graphics.Insets
import android.os.Build
import com.kalambur.mappy_stars.adapters.ColorAdapter
import android.os.Bundle
import android.view.*
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.adapters.FontAdapter
import com.kalambur.mappy_stars.databinding.FragmentLocationV1ControllerBinding
import com.kalambur.mappy_stars.pojo.FontText
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.helpers.Helper.Companion.getAllFonts
import java.util.*

class LocationV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var binding: FragmentLocationV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLocationV1ControllerBinding.inflate(inflater, container, false)

        colorAdapter = ColorAdapter(templateCanvas.locationFontColor) {
            templateCanvas.locationFontColor.value = it
        }

        val root: View = binding.root

        templateCanvas.locationFont.observe(requireActivity(), {
            binding.editFont.setText(it.name)
        })

        templateCanvas.locationFontColor.observe(requireActivity(), {
            colorAdapter.notifyDataSetChanged()
        })

        binding.editFont.setOnClickListener {
            showFontDialog()
        }

        recyclerColorsInit()

        binding.sliderFontSize.progress = templateCanvas.locationFont.value!!.size!!.toInt()
        binding.fontSize.text           = templateCanvas.locationFont.value!!.size!!.toInt().toString()

        binding.sliderFontSize.setOnSeekBarChangeListener(
            object : OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newFont = templateCanvas.locationFont.value
                    newFont?.size = seekBar.progress.toFloat()

                    templateCanvas.locationFont.value = newFont
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.fontSize.text = progress.toString()
                }
            }
        )

        binding.checkboxEnableDateInLocation.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasEventDateInLocation.value = isChecked
        }

        binding.checkboxEnableTimeInLocation.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasEventTimeInLocation.value = isChecked
        }

        binding.checkboxEnableCityInLocation.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasEventCityInLocation.value = isChecked
        }

        binding.checkboxEnableCoordinatesInLocation.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasEventCoordinatesInLocation.value = isChecked
        }

        binding.checkboxEditText.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasEditResultLocationText.value = isChecked
        }

        templateCanvas.hasEditResultLocationText.observe(requireActivity(), {
            binding.resultLocationText.isEnabled = it
            binding.resultLocationText.alpha = if(it) 1F else 0.6F
        })

        templateCanvas.resultLocationText.observe(requireActivity(), {
            binding.resultLocationText.setText(it)
        })

        binding.resultLocationText.setOnClickListener {
            showDescDialog()
        }

        return root
    }

    private fun recyclerColorsInit() {
        val recyclerColors: RecyclerView = binding.colorRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = colorAdapter

        colorAdapter.addAllColorList(templateCanvas.colorList)
    }

    private fun showDescDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.dialog_desc_layout, null)
        layout.findViewById<TextView>(R.id.title).text = resources.getString(R.string.label_result)

        val editText = layout.findViewById<EditText>(R.id.desc)

        editText.setText(templateCanvas.resultLocationText.value)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
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
            editText.setSelection(editText.text.length)
            editText.requestFocus()
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                templateCanvas.resultLocationText.value = editText.text.toString().trim()

                dialog.dismiss()
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun showFontDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_simple_layout, null)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setView(layout)

        val dialog = builder.create()

        val listView = layout.findViewById<ListView>(R.id.listView)

        val allFonts: ArrayList<FontText> = getAllFonts(requireActivity())

        val adapter = FontAdapter(activity, templateCanvas, dialog, allFonts) {
            val newFont = templateCanvas.locationFont.value
            newFont?.name = it.name
            newFont?.resId = it.resId

            templateCanvas.locationFont.value = newFont
        }

        listView.adapter = adapter

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }



}