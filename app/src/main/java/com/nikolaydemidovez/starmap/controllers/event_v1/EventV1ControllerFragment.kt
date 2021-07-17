package com.nikolaydemidovez.starmap.controllers.event_v1

import android.app.AlertDialog.THEME_HOLO_LIGHT
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*


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

        binding.editDate.setText(getDateInstance(LONG, Locale("ru")).format(templateCanvas.eventDate.value!!))
        binding.editTime.setText(templateCanvas.eventTime.value!!)
        binding.editLocation.setText(templateCanvas.eventCity.value)
        binding.editLatitude.setText(templateCanvas.eventLatitude.value.toString())
        binding.editLongitude.setText(templateCanvas.eventLongitude.value.toString())

        return root
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
            datePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),R.color.dark))
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