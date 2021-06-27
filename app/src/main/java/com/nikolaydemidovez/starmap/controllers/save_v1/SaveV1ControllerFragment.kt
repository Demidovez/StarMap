package com.nikolaydemidovez.starmap.controllers.save_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.databinding.FragmentSaveV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateView

class SaveV1ControllerFragment(private val templateView: TemplateView) : Fragment() {

    private lateinit var viewModel: SaveV1ControllerViewModel
    private lateinit var binding: FragmentSaveV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(SaveV1ControllerViewModel::class.java)
        binding = FragmentSaveV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val textView: TextView = binding.textView

        viewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })

        return root
    }

}