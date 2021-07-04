package com.nikolaydemidovez.starmap.controllers.desc_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.nikolaydemidovez.starmap.databinding.FragmentDescV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class DescV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: DescV1ControllerViewModel
    private lateinit var binding: FragmentDescV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(DescV1ControllerViewModel::class.java)
        binding = FragmentDescV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.editDesc.setText(templateCanvas.descText)

        // Изменяем текст под картой
        binding.editDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("MyLog", s.toString())
                templateCanvas.updateDescText(s.toString())
            }
        })

        return root
    }

}