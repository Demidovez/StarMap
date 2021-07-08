package com.nikolaydemidovez.starmap.controllers.map_v1

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.nikolaydemidovez.starmap.databinding.FragmentMapV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas

class MapV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: MapV1ControllerViewModel
    private lateinit var binding: FragmentMapV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(MapV1ControllerViewModel::class.java)
        binding = FragmentMapV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val radioGroupColorBorder = binding.radioGroupColorBorder
        val radioGroupWidthBorder = binding.radioGroupWidthBorder

        // Добавлеям/убираем рамку карты
        binding.checkboxEnableBorder.isChecked = templateCanvas.hasBorderMap.value!!
        binding.checkboxEnableBorder.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasBorderMap.value = isChecked

            binding.labelWidthBorder.alpha = if (isChecked) 1F else 0.6F
            for (i in 0 until radioGroupColorBorder.childCount) {
                (radioGroupColorBorder.getChildAt(i) as RadioButton).isEnabled = isChecked
            }

            binding.labelColorBorderMap.alpha = if (isChecked) 1F else 0.6F
            for (i in 0 until radioGroupWidthBorder.childCount) {
                (radioGroupWidthBorder.getChildAt(i) as RadioButton).isEnabled = isChecked
            }
        }


        return root
    }

}