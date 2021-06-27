package com.nikolaydemidovez.starmap.controllers.map_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nikolaydemidovez.starmap.databinding.FragmentMapV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView

class MapV1ControllerFragment(private val templateView: TemplateView) : Fragment() {

    private lateinit var viewModel: MapV1ControllerViewModel
    private lateinit var binding: FragmentMapV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(MapV1ControllerViewModel::class.java)
        binding = FragmentMapV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root


        return root
    }

}