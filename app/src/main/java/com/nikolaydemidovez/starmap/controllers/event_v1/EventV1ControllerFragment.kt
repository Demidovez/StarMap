package com.nikolaydemidovez.starmap.controllers.event_v1

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.text.DateFormat.*
import java.util.*

import android.widget.ListView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.LocationAdapter
import com.nikolaydemidovez.starmap.pojo.Location


class EventV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var viewModel: EventV1ControllerViewModel
    private lateinit var binding: FragmentEventV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(EventV1ControllerViewModel::class.java)
        binding = FragmentEventV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.editDate.setOnClickListener {
            showDatePickerDialog()
        }

        binding.editTime.setOnClickListener {
            showTimePickerDialog()
        }

        binding.editLocation.setOnClickListener {
            showLocationPicker()
        }

        binding.editDate.setText(getDateInstance(LONG, Locale("ru")).format(templateCanvas.eventDate.value!!))
        binding.editTime.setText(templateCanvas.eventTime.value!!)
        binding.editLocation.setText(templateCanvas.eventLocation.value)

        templateCanvas.eventLocation.observe(requireActivity(), {
            val text = "$it, ${templateCanvas.eventCountry.value!!}"
            binding.editLocation.setText(text)
        })

        templateCanvas.eventCountry.observe(requireActivity(), {
            var text = ""

            if(it.isNotEmpty()) {
                text = "${templateCanvas.eventLocation.value!!}, $it"

            } else {
                text = templateCanvas.eventLocation.value!!
            }

            binding.editLocation.setText(text)
        })

        templateCanvas.eventLatitude.observe(requireActivity(), {
            if(!binding.editLatitude.isFocused) {
                binding.editLatitude.setText(it.toString())
            }
        })

        templateCanvas.eventLongitude.observe(requireActivity(), {
            if(!binding.editLongitude.isFocused) {
                binding.editLongitude.setText(it.toString())
            }
        })

        binding.editLatitude.doOnTextChanged { text, _, _, _ ->
            if(binding.editLatitude.isFocused) {
                templateCanvas.eventLatitude.value = text.toString().toDouble()
            }
        }

        binding.editLongitude.doOnTextChanged { text, _, _, _ ->
            if(binding.editLongitude.isFocused) {
                templateCanvas.eventLongitude.value = text.toString().toDouble()
            }
        }

        return root
    }

    private fun showLocationPicker() {
        val placesClient = Places.createClient(requireActivity())
        val token = AutocompleteSessionToken.newInstance()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.location_picker_layout)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
//        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        val editText = dialog.findViewById<EditText>(R.id.edit_text_location)
        val listView = dialog.findViewById<ListView>(R.id.listView)

        val searchLocations: ArrayList<Location> = ArrayList()

        val adapter = LocationAdapter(activity, placesClient, templateCanvas, dialog, searchLocations)
        listView.adapter = adapter

        editText.doOnTextChanged { text, _, _, _ ->
            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(text.toString())
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    adapter.clear()

                    for (prediction in response.autocompletePredictions) {
                        val searchLocation = Location(prediction.getPrimaryText(null).toString(), prediction.getSecondaryText(null).toString(), prediction.placeId)

                        searchLocations.add(searchLocation)
                        adapter.notifyDataSetChanged()
                    }
                }
        }
        editText.setText(templateCanvas.eventLocation.value)
        editText.requestFocus()

        dialog.show()
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        c.time = templateCanvas.eventDate.value!!
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val format = getDateInstance(LONG, Locale("ru"))

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, year, monthOfYear, dayOfMonth ->
                c.set(year, monthOfYear, dayOfMonth)

                templateCanvas.eventDate.value = c.time

                binding.editDate.setText(format.format(c.time).toString())
            },
            mYear, mMonth, mDay
        )

        datePickerDialog.setOnShowListener {
            datePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            datePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
        }

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val is24HView = true
        val hour = templateCanvas.eventTime.value!!.split(":")[0].toInt()
        val minute = templateCanvas.eventTime.value!!.split(":")[1].toInt()

        val timePickerDialog = TimePickerDialog (
            requireContext(),  { _, hourOfDay, minute ->
                val newTime = "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

                templateCanvas.eventTime.value = newTime

                binding.editTime.setText(newTime)
            }, hour, minute, is24HView
        )

        timePickerDialog.setOnShowListener {
            timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            timePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.dark))
        }

        timePickerDialog.show()
    }
}