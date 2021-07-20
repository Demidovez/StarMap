package com.nikolaydemidovez.starmap.controllers.location_v1

import adapters.ColorAdapter
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentLocationV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.util.*

class LocationV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: LocationV1ControllerViewModel
    private lateinit var binding: FragmentLocationV1ControllerBinding
    private val colorAdapter = ColorAdapter(templateCanvas)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(LocationV1ControllerViewModel::class.java)
        binding = FragmentLocationV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        templateCanvas.locationFont.observe(requireActivity(), {
            binding.editFont.setText(it.name)
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
            if(!binding.resultLocationText.isFocused && !templateCanvas.hasEditResultLocationText.value!!) {
                binding.resultLocationText.setText(it)
            }
        })

        binding.resultLocationText.doOnTextChanged { _, _, _, _ ->
            if(templateCanvas.hasEditResultLocationText.value!!) {
                templateCanvas.resultLocationText.value = binding.resultLocationText.text.toString()
            }
        }

        return root
    }

    private fun recyclerColorsInit() {
        val recyclerColors: RecyclerView = binding.colorRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = colorAdapter

        colorAdapter.addAllColorList(arrayListOf(
            "#000000",
            "#FFFFFF",
            "#1ABC9C",
            "#16A085",
            "#2ECC71",
            "#27AE60",
            "#3498DB",
            "#2980B9",
            "#9B59B6",
            "#8E44AD",
            "#34495E",
            "#2C3E50",
            "#F1C40F",
            "#F39C12",
            "#E67E22",
            "#D35400",
            "#E74C3C",
            "#C0392B",
            "#BDC3C7",
            "#95A5A6",
            "#7F8C8D"
        ))
    }

    private fun showFontDialog() {
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.font_picker_layout)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        val listView = dialog.findViewById<ListView>(R.id.listView)

        val allFonts: ArrayList<FontText> = getAllFonts()

        val adapter = FontAdapter(activity, templateCanvas, dialog, allFonts)
        listView.adapter = adapter

        dialog.show()
    }

    private fun getAllFonts(): ArrayList<FontText> {
        val allFonts: ArrayList<FontText> = ArrayList()
        val fontList = requireActivity().resources.getStringArray(R.array.font_in_locations)

        for (font in fontList) {
            val fontResID   = requireActivity().resources.getIdentifier(font, "font", activity?.packageName)
            val stringResID = requireActivity().resources.getIdentifier(font, "string", activity?.packageName)
            val fontName    = requireActivity().resources.getString(stringResID)

            try {
                allFonts.add(FontText(fontName, fontResID, null, null))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        return allFonts
    }

}