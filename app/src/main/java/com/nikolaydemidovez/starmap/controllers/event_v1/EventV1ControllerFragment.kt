package com.nikolaydemidovez.starmap.controllers.event_v1

import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.LocationAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.Location
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidLat
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidLong
import java.text.DateFormat.LONG
import java.text.DateFormat.getDateInstance
import java.util.*

class EventV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var viewModel: EventV1ControllerViewModel
    private lateinit var binding: FragmentEventV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
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
            binding.editLatitude.clearFocus()
            binding.editLongitude.clearFocus()

            showLocationPicker()
        }

        binding.editLatitude.setOnClickListener {
            val value = "%.6f".format(templateCanvas.coordinates.value!![0]).replace(",", ".")
            showLatLongDialog("Широта места события", value, LATITUDE)
        }

        binding.editLongitude.setOnClickListener {
            val value = "%.6f".format(templateCanvas.coordinates.value!![1]).replace(",", ".")
            showLatLongDialog("Долгота места события", value, LONGITUDE)
        }

        binding.editDate.setText(getDateInstance(LONG, Locale("ru")).format(templateCanvas.eventDate.value!!))
        binding.editTime.setText(getTime())
        binding.editLocation.setText(templateCanvas.eventLocation.value)

        templateCanvas.eventLocation.observe(requireActivity(), {
            val text = "$it, ${templateCanvas.eventCountry.value!!}"
            binding.editLocation.setText(text)
        })

        templateCanvas.eventCountry.observe(requireActivity(), {
            var text = ""

            text = if(it.isNotEmpty()) {
                "${templateCanvas.eventLocation.value!!}, $it"
            } else {
                templateCanvas.eventLocation.value!!
            }

            binding.editLocation.setText(text)
        })

        templateCanvas.coordinates.observe(requireActivity(), {
            if(!binding.editLatitude.isFocused) {
                val value = "%.6f".format(it[0]).replace(",", ".")
                binding.editLatitude.setText(value)
            }

            if(!binding.editLongitude.isFocused) { // TODO нужна ли уже эта првоерка?
                val value = "%.6f".format(it[1]).replace(",", ".")
                binding.editLongitude.setText(value)
            }
        })

        return root
    }

    private fun getTime(): String {
        return if (templateCanvas.eventTime.value!!.isNotEmpty()) {
            templateCanvas.eventTime.value!!
        } else {
            val date = Calendar.getInstance()

            val hourOfDay = date.get(Calendar.HOUR_OF_DAY)
            val minute = date.get(Calendar.MINUTE)

            "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        }
    }

    private fun showLatLongDialog(title: String, value: String, flag: Int) {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_lat_long_layout, null)
        layout.findViewById<TextView>(R.id.title).text = title

        val editText      = layout.findViewById<EditText>(R.id.edit_text_lat_long)
        val imgClearText  = layout.findViewById<ImageView>(R.id.clear_text)
        val descText      = layout.findViewById<TextView>(R.id.desc_text)

        var descTextValue = ""
        descTextValue = if(flag == LATITUDE) "Диапазон значений от -90 до +90" else "Диапазон значений от -180 до +180"
        descText.text = descTextValue

        editText.setText(value)
        editText.doOnTextChanged { text, _, _, _ ->
            descText.text = descTextValue
            descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark_gray))
            imgClearText.visibility = if(text!!.isNotEmpty()) VISIBLE else GONE
        }
        imgClearText.setOnClickListener { editText .setText("") }


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
            imgClearText.visibility = if(editText.text.isNotEmpty()) VISIBLE else GONE
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                if(editText.text.isNotEmpty()) {
                    val editValue = editText.text.toString().toDouble()

                    if(flag == LATITUDE) {
                        if(isValidLat(editValue)) {
                            if(templateCanvas.coordinates.value!![0] != editValue) {
                                val newCoordinates = templateCanvas.coordinates.value
                                newCoordinates!![0] = editValue
                                templateCanvas.coordinates.value = newCoordinates

                                binding.editLocation.setText("Другое...")
                            }

                            dialog.dismiss()
                        } else {
                            descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_flat))
                            descText.text = "Неверная широта!"
                        }
                    }

                    if(flag == LONGITUDE) {
                        if(isValidLong(editValue)) {
                            if(templateCanvas.coordinates.value!![1] != editValue) {
                                val newCoordinates = templateCanvas.coordinates.value
                                newCoordinates!![1] = editValue
                                templateCanvas.coordinates.value = newCoordinates

                                binding.editLocation.setText("Другое...")
                            }

                            dialog.dismiss()
                        } else {
                            descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_flat))
                            descText.text = "Неверная долгота!"
                        }
                    }
                } else {
                    descText.setTextColor(ContextCompat.getColor(requireContext(), R.color.red_flat))
                    descText.text = "Пустое значение недопустимо!"
                }
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun showLocationPicker() {
        val placesClient = Places.createClient(requireActivity())
        val token = AutocompleteSessionToken.newInstance()

        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_location_layout, null)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setView(layout)

        val dialog = builder.create()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dialog.window?.setDecorFitsSystemWindows(false)
            dialog.window?.decorView!!.setOnApplyWindowInsetsListener { v, insets ->
                val imeInsets: Insets = insets.getInsets(WindowInsets.Type.ime())
                val paddingBottom = if(imeInsets.bottom == 0) 20 else imeInsets.bottom
                v.updatePadding(bottom = paddingBottom)
                insets
            }
        } else {
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }

        val editText = layout.findViewById<EditText>(R.id.edit_text_location)
        val imgClearText = layout.findViewById<ImageView>(R.id.clear_text)
        val imgLoading = layout.findViewById<LottieAnimationView>(R.id.loading)
        val listView = layout.findViewById<ListView>(R.id.listView)

        val searchLocations: ArrayList<Location> = ArrayList()

        val adapter = LocationAdapter(activity, placesClient, templateCanvas, dialog, searchLocations)
        listView.adapter = adapter

        editText.doOnTextChanged { text, _, _, count ->
            if(count > 0) {
                imgClearText.visibility = VISIBLE
            } else {
                imgClearText.visibility = GONE
            }

            val request = FindAutocompletePredictionsRequest.builder()
                .setSessionToken(token)
                .setQuery(text.toString())
                .build()

            placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                    adapter.clear()

                    imgLoading.visibility = GONE

                    for (prediction in response.autocompletePredictions) {
                        val searchLocation = Location(prediction.getPrimaryText(null).toString(), prediction.getSecondaryText(null).toString(), prediction.placeId)

                        searchLocations.add(searchLocation)
                        adapter.notifyDataSetChanged()
                    }
                }
        }

        imgClearText.setOnClickListener {
            editText.setText("")
        }

        val initialLocation = if(binding.editLocation.text.toString() != "Другое...") templateCanvas.eventLocation.value else ""
        editText.setText(initialLocation)

        dialog.setOnShowListener {
            editText.setSelection(editText.text.length)
            editText.requestFocus()
            imgClearText.visibility = if(editText.text.isNotEmpty()) VISIBLE else GONE
            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun showDatePickerDialog() {
        val c = Calendar.getInstance()
        c.time = templateCanvas.eventDate.value!!
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)

        val format = getDateInstance(LONG, Locale("ru"))

        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_date_layout, null)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.setView(layout)

        val dialog = builder.create()

        val datePicker = layout.findViewById<DatePicker>(R.id.datePicker)
        datePicker.updateDate(mYear, mMonth, mDay)


        dialog.setOnShowListener {

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)

            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                val year = datePicker.year
                val monthOfYear = datePicker.month
                val dayOfMonth = datePicker.dayOfMonth

                c.set(year, monthOfYear, dayOfMonth)

                templateCanvas.eventDate.value = c.time

                binding.editDate.setText(format.format(c.time).toString())

                dialog.dismiss()
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.7).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun showTimePickerDialog() {
        val dateDefault = Calendar.getInstance()
        var hour = dateDefault.get(Calendar.HOUR_OF_DAY)
        var minute = dateDefault.get(Calendar.MINUTE)

        templateCanvas.eventTime.value.let {
            if(it!!.isNotEmpty()) {
                hour = it.split(":")[0].toInt()
                minute = it.split(":")[1].toInt()
            }
        }

        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_time_layout, null)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton(android.R.string.ok, null)
        builder.setNegativeButton(android.R.string.cancel, null)
        builder.setView(layout)

        val dialog = builder.create()

        val timePicker = layout.findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour = hour
            timePicker.minute = minute
        } else {
            timePicker.currentHour = hour
            timePicker.currentMinute = minute
        }

        dialog.setOnShowListener {

            dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE)
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
                var hour = 0
                var minute = 0

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    hour = timePicker.hour
                    minute = timePicker.minute
                } else {
                    hour = timePicker.currentHour
                    minute = timePicker.currentMinute
                }

                val newTime = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

                templateCanvas.eventTime.value = newTime

                binding.editTime.setText(newTime)

                dialog.dismiss()
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.6).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    companion object {
        const val LATITUDE = 1
        const val LONGITUDE = 2
    }
}