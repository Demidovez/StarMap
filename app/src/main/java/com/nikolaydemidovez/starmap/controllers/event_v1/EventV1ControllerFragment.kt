package controllers.event_v1

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import java.text.DateFormat.*
import java.util.*
import android.view.WindowManager
import android.view.WindowInsets
import android.graphics.Insets
import android.os.Build
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import androidx.core.view.updatePadding
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.LocationAdapter
import com.nikolaydemidovez.starmap.controllers.event_v1.EventV1ControllerViewModel
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.Location
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidLat
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.isValidLong


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
            val value = "%.6f".format(templateCanvas.eventLatitude.value).replace(",", ".")
            showLatLongDialog("Широта места события", value, LATITUDE)
        }

        binding.editLongitude.setOnClickListener {
            val value = "%.6f".format(templateCanvas.eventLongitude.value).replace(",", ".")
            showLatLongDialog("Долгота места события", value, LONGITUDE)
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

            text = if(it.isNotEmpty()) {
                "${templateCanvas.eventLocation.value!!}, $it"
            } else {
                templateCanvas.eventLocation.value!!
            }

            binding.editLocation.setText(text)
        })

        templateCanvas.eventLatitude.observe(requireActivity(), {
            if(!binding.editLatitude.isFocused) {
                val value = "%.6f".format(it).replace(",", ".")
                binding.editLatitude.setText(value)
            }
        })

        templateCanvas.eventLongitude.observe(requireActivity(), {
            if(!binding.editLongitude.isFocused) { // TODO нужна ли уже эта првоерка?
                val value = "%.6f".format(it).replace(",", ".")
                binding.editLongitude.setText(value)
            }
        })

        return root
    }

    private fun showLatLongDialog(title: String, value: String, flag: Int) {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.lat_long_picker_layout, null)
        layout.findViewById<TextView>(R.id.title).text = title

        val editText      = layout.findViewById<EditText>(R.id.edit_text_lat_long)
        val imgClearText  = layout.findViewById<ImageView>(R.id.clear_text)
        val descText      = layout.findViewById<TextView>(R.id.desc_text)

        var descTextValue = ""
        descTextValue = if(flag == LATITUDE) "Допустимые значения от -90 до +90" else "Допустимые значения от -180 до +180"
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
                            if(templateCanvas.eventLatitude.value != editValue) {
                                templateCanvas.eventLatitude.value = editValue

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
                            if(templateCanvas.eventLongitude.value != editValue) {
                                templateCanvas.eventLongitude.value = editValue

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

        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

    private fun showLocationPicker() {
        val placesClient = Places.createClient(requireActivity())
        val token = AutocompleteSessionToken.newInstance()
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.location_picker_layout)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

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

        val editText = dialog.findViewById<EditText>(R.id.edit_text_location)
        val imgClearText = dialog.findViewById<ImageView>(R.id.clear_text)
        val listView = dialog.findViewById<ListView>(R.id.listView)

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
            requireContext(), { _, hourOfDay, minute ->
                val newTime = "${hourOfDay.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

                templateCanvas.eventTime.value = newTime

                binding.editTime.setText(newTime)
            }, hour, minute, is24HView
        )

        timePickerDialog.setOnShowListener {
            timePickerDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            timePickerDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(),
                R.color.dark))
        }

        timePickerDialog.show()
    }

    companion object {
        const val LATITUDE = 1
        const val LONGITUDE = 2
    }
}