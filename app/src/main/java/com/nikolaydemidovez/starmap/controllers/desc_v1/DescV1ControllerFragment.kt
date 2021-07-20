package com.nikolaydemidovez.starmap.controllers.desc_v1

import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentDescV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.util.ArrayList

class DescV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: DescV1ControllerViewModel
    private lateinit var binding: FragmentDescV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(DescV1ControllerViewModel::class.java)
        binding = FragmentDescV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.editDesc.setText(templateCanvas.descText.value)

        // Изменяем текст под картой
        binding.editDesc.doOnTextChanged { _, _, _, _ ->
            templateCanvas.descText.value = binding.editDesc.toString()
        }

        binding.editFont.setOnClickListener {
            showFontDialog()
        }

        return root
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