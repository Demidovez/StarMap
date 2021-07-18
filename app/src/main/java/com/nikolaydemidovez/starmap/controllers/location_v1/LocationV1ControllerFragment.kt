package com.nikolaydemidovez.starmap.controllers.location_v1

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Insets
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.adapters.LocationAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentLocationV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.pojo.Location
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.text.DateFormat
import java.util.*

class LocationV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: LocationV1ControllerViewModel
    private lateinit var binding: FragmentLocationV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(LocationV1ControllerViewModel::class.java)
        binding = FragmentLocationV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        templateCanvas.locationFont.observe(requireActivity(), {
            binding.editFont.setText(templateCanvas.locationFont.value?.name)
        })

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