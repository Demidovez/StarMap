package com.nikolaydemidovez.starmap.controllers.canvas_v1

import android.R.attr
import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentCanvasV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import android.R.attr.checked




class CanvasV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var viewModel: CanvasV1ControllerViewModel
    private lateinit var binding: FragmentCanvasV1ControllerBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(CanvasV1ControllerViewModel::class.java)
        binding = FragmentCanvasV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val radioGroupIndentBorder = binding.radioGroupIndentBorder
        val radioGroupWidthBorder = binding.radioGroupWidthBorder
        val radioGroupColorBorder = binding.radioGroupColorBorder

        // Изменяем размеры холста
        binding.radioGroupSize.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = root.findViewById<RadioButton>(checkedId)

            var width = 2480F
            var height = 3508F

            when(radioBtn.text) {
                "A4" -> { width = 2480F; height = 3508F }
                "A3" -> { width = 3508F; height = 4961F }
                "A2" -> { width = 4961F; height = 7016F }
                "A1" -> { width = 7016F; height = 9933F }
            }

            templateCanvas.updateCanvasSize(width, height)
        }

        // Изменяем цвет фона холста
        binding.radioGroupBackground.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = root.findViewById<RadioButton>(checkedId)

            var color = "#FFFFFF"

            when(radioBtn.text) {
                "Белый" -> color = "#FFFFFF"
                "Черный" -> color = "#000000"
                "Зеленый" -> color = "#10ac84"
                "Синий" -> color = "#0a3d62"
            }

            templateCanvas.updateBackgroundColorCanvas(color)
        }

        // Добавлеям/убираем рамку холста
        binding.checkboxEnableBorder.isChecked = templateCanvas.hasBorderCanvas
        binding.checkboxEnableBorder.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.updateHasBorderCanvas(isChecked)

            binding.labelIndentBorder.alpha = if (isChecked) 1F else 0.6F
            for (i in 0 until radioGroupIndentBorder.childCount) {
                (radioGroupIndentBorder.getChildAt(i) as RadioButton).isEnabled = isChecked
            }

            binding.labelWidthBorder.alpha = if (isChecked) 1F else 0.6F
            for (i in 0 until radioGroupWidthBorder.childCount) {
                (radioGroupWidthBorder.getChildAt(i) as RadioButton).isEnabled = isChecked
            }

            binding.labelColorBorder.alpha = if (isChecked) 1F else 0.6F
            for (i in 0 until radioGroupColorBorder.childCount) {
                (radioGroupColorBorder.getChildAt(i) as RadioButton).isEnabled = isChecked
            }
        }

        // Изменяем цвет рамки холста
        radioGroupColorBorder.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = root.findViewById<RadioButton>(checkedId)

            var color = "#FFFFFF"

            when(radioBtn.text) {
                "Белый" -> color = "#FFFFFF"
                "Черный" -> color = "#000000"
                "Зеленый" -> color = "#10ac84"
                "Синий" -> color = "#0a3d62"
            }

            templateCanvas.updateCanvasBorderColor(color)
        }

        // Изменяем отступ рамки от краев холста
        radioGroupIndentBorder.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = root.findViewById<RadioButton>(checkedId)

            var indent = 10F

            when(radioBtn.text) {
                "10 мм" -> indent = 10F
                "15 мм" -> indent = 15F
                "20 мм" -> indent = 20F
                "25 мм" -> indent = 25F
            }

            templateCanvas.updateIndentBorderCanvas(indent)
        }

        // Изменяем ширину рамки
        radioGroupWidthBorder.setOnCheckedChangeListener { _, checkedId ->
            val radioBtn = root.findViewById<RadioButton>(checkedId)

            var width = 10F

            when(radioBtn.text) {
                "3 мм" -> width = 3F
                "4 мм" -> width = 4F
                "5 мм" -> width = 5F
                "6 мм" -> width = 6F
            }

            templateCanvas.updateWidthBorderCanvas(width)
        }

        return root
    }

}