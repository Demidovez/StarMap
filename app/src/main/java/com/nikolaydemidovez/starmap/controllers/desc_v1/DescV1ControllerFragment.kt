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
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentDescV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.util.ArrayList

class DescV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: DescV1ControllerViewModel
    private lateinit var binding: FragmentDescV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(DescV1ControllerViewModel::class.java)
        binding = FragmentDescV1ControllerBinding.inflate(inflater, container, false)

        colorAdapter = ColorAdapter(templateCanvas.descFont) {
            val newFont = templateCanvas.descFont.value
            newFont?.color = it

            templateCanvas.descFont.value = newFont
        }

        val root: View = binding.root

        binding.editDesc.setText(templateCanvas.descText.value)

        // Изменяем текст под картой
        binding.editDesc.doOnTextChanged { _, _, _, _ ->
            templateCanvas.descText.value = binding.editDesc.text.toString()
        }

        templateCanvas.descFont.observe(requireActivity(), {
            binding.editFont.setText(it.name )
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
        dialog.setContentView(R.layout.simple_picker_layout)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        val listView = dialog.findViewById<ListView>(R.id.listView)

        val allFonts: ArrayList<FontText> = getAllFonts()

        val adapter = FontAdapter(activity, templateCanvas, dialog, allFonts) {
            val newFont = templateCanvas.descFont.value
            newFont?.name = it.name
            newFont?.resId = it.resId

            templateCanvas.descFont.value = newFont
        }

        listView.adapter = adapter

        dialog.show()
    }

    private fun getAllFonts(): ArrayList<FontText> {
        val allFonts: ArrayList<FontText> = ArrayList()
        val fontList = requireActivity().resources.getStringArray(R.array.font_in_desc)

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