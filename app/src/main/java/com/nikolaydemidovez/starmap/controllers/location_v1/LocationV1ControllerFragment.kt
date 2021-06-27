package com.nikolaydemidovez.starmap.controllers.location_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.databinding.FragmentLocationV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView

class LocationV1ControllerFragment(private val templateView: TemplateView) : Fragment() {

    private lateinit var viewModel: LocationV1ControllerViewModel
    private lateinit var binding: FragmentLocationV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(LocationV1ControllerViewModel::class.java)
        binding = FragmentLocationV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textView

        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}