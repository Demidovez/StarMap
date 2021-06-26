package com.nikolaydemidovez.starmap.ui.template.controllers.event

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import java.sql.Time
import java.text.DateFormat
import java.text.DateFormat.*
import java.text.SimpleDateFormat
import java.util.*

class EventV1ControllerFragment : Fragment() {
    private lateinit var viewModel: EventV1ControllerViewModel
    private lateinit var binding: FragmentEventV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(EventV1ControllerViewModel::class.java)
        binding = FragmentEventV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.editDate.setText(getDateInstance(SHORT).format(Calendar.getInstance().time))
        binding.editTime.setText(getTimeInstance(SHORT).format(Calendar.getInstance().time))
        binding.editLocation.setText("Москва")
        binding.editLatitude.setText("55.7522200")
        binding.editLongitude.setText("37.6155600")

//        val textView: TextView = binding.textView
//
//        viewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        return root
    }
}