package com.nikolaydemidovez.starmap.controllers.separator_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.databinding.FragmentSeparatorV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class SeparatorV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: SeparatorV1ControllerViewModel
    private lateinit var binding: FragmentSeparatorV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(SeparatorV1ControllerViewModel::class.java)
        binding = FragmentSeparatorV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textView

        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}