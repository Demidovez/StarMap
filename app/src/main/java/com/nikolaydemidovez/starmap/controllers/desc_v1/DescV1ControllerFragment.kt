package com.nikolaydemidovez.starmap.controllers.desc_v1

import adapters.ColorAdapter
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentDescV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.getAllFonts
import java.util.ArrayList

class DescV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: DescV1ControllerViewModel
    private lateinit var binding: FragmentDescV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(DescV1ControllerViewModel::class.java)
        binding = FragmentDescV1ControllerBinding.inflate(inflater, container, false)

        colorAdapter = ColorAdapter(templateCanvas.descFontColor) {
            templateCanvas.descFontColor.value = it
        }

        val root: View = binding.root

        binding.editDesc.setText(templateCanvas.descText.value)

        // Изменяем текст под картой
        binding.editDesc.doOnTextChanged { _, _, _, _ ->
            templateCanvas.descText.value = binding.editDesc.text.toString()
        }

        templateCanvas.descFont.observe(requireActivity(), {
            binding.editFont.setText(it.name )
        })

        templateCanvas.descFontColor.observe(requireActivity(), {
            colorAdapter.notifyDataSetChanged()
        })

        binding.editFont.setOnClickListener {
            showFontDialog()
        }

        recyclerColorsInit()

        binding.sliderFontSize.progress = templateCanvas.descFont.value!!.size!!.toInt()
        binding.fontSize.text           = templateCanvas.descFont.value!!.size!!.toInt().toString()

        binding.sliderFontSize.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newFont = templateCanvas.descFont.value
                    newFont?.size = seekBar.progress.toFloat()

                    templateCanvas.descFont.value = newFont
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.fontSize.text = progress.toString()
                }
            }
        )

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
            val newFont = templateCanvas.descFont.value
            newFont?.name = it.name
            newFont?.resId = it.resId

            templateCanvas.descFont.value = newFont
        }

        listView.adapter = adapter

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
}