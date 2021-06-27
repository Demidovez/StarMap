package com.nikolaydemidovez.starmap.controllers.event_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolaydemidovez.starmap.databinding.FragmentEventV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView
import java.text.DateFormat.*
import java.util.*

class EventV1ControllerFragment(private val templateView: TemplateView) : Fragment() {
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

        return root
    }
}