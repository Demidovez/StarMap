package com.nikolaydemidovez.starmap.controllers.canvas_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentCanvasV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView

class CanvasV1ControllerFragment(private val templateView: TemplateView) : Fragment() {
    private lateinit var viewModel: CanvasV1ControllerViewModel
    private lateinit var binding: FragmentCanvasV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(CanvasV1ControllerViewModel::class.java)
        binding = FragmentCanvasV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.frameLayout2.setOnClickListener {
            templateView.updateCanvasOriginalSize(2480F, 3508F)
            templateView.updateBackgroundColorCanvas(R.color.black)
            templateView.updateCanvasBorderColor(R.color.white)
        }

        return root
    }

}