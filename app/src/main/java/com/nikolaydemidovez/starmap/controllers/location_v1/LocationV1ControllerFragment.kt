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
import androidx.appcompat.app.AlertDialog
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
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.getAllFonts
import java.util.*

class LocationV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: LocationV1ControllerViewModel
    private lateinit var binding: FragmentLocationV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(LocationV1ControllerViewModel::class.java)
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

        colorAdapter.addAllColorList(templateCanvas.colorList)
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