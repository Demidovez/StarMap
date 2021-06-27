package com.nikolaydemidovez.starmap.controllers.stars_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.databinding.FragmentStarsV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView

class StarsV1ControllerFragment(private val templateView: TemplateView) : Fragment() {

    private lateinit var viewModel: StarsV1ControllerViewModel
    private lateinit var binding: FragmentStarsV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(StarsV1ControllerViewModel::class.java)
        binding = FragmentStarsV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textView

        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}