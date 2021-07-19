package com.nikolaydemidovez.starmap.controllers.location_v1

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.ColorAdapter
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
            binding.editFont.setText(templateCanvas.locationFont.value?.name)
            colorAdapter.notifyDataSetChanged()
        })

        binding.editFont.setOnClickListener {
            showFontDialog()
        }

        recyclerColorsInit()

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
            "#7F8C8D",
            "picker"
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