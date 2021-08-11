package com.nikolaydemidovez.starmap.controllers.separator_v1

import com.nikolaydemidovez.starmap.adapters.ColorAdapter
import adapters.ShapeAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentSeparatorV1ControllerBinding
import com.nikolaydemidovez.starmap.interfaces.HasShapeInterface
import com.nikolaydemidovez.starmap.pojo.ShapeSeparator
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.shadowAlpha

import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.RecyclerViewDisabler

class SeparatorV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var binding: FragmentSeparatorV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter
    private val disablerColorRecycler = RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentSeparatorV1ControllerBinding.inflate(inflater, container, false)

        colorAdapter = ColorAdapter(templateCanvas.separatorColor) {
            templateCanvas.separatorColor.value = it
        }

        templateCanvas.separatorColor.observe(requireActivity(), {
            colorAdapter.notifyDataSetChanged()
        })

        val root: View = binding.root

        recyclerShapeInit()

        templateCanvas.separator.observe(requireActivity(), {
            val isEnabled = it.shapeType != ShapeSeparator.NONE

            binding.labelColorRecycler.alpha = shadowAlpha(isEnabled)
            disablerColorRecycler.isEnable = isEnabled
            binding.sliderWidth.isEnabled = isEnabled
            binding.sliderWidth.alpha = shadowAlpha(isEnabled)
            binding.colorRecycler.alpha = shadowAlpha(isEnabled)
            binding.width.alpha = shadowAlpha(isEnabled)
            binding.labelWidthSeparator.alpha = shadowAlpha(isEnabled)
        })

        recyclerColorsInit()

        binding.sliderWidth.progress = templateCanvas.separator.value!!.width.toInt()
        binding.width.text           = templateCanvas.separator.value!!.width.toInt().toString()

        binding.sliderWidth.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newSeparator = templateCanvas.separator.value
                    newSeparator!!.width = seekBar.progress.coerceAtLeast(1).toFloat()

                    templateCanvas.separator.value = newSeparator
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.width.text = progress.coerceAtLeast(1).toString()
                }
            }
        )

        return root
    }

    private fun recyclerColorsInit() {
        val recyclerColors: RecyclerView = binding.colorRecycler

        recyclerColors.addOnItemTouchListener(disablerColorRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = colorAdapter

        colorAdapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerShapeInit() {
        val shapeAdapter = ShapeAdapter(templateCanvas.separator as MutableLiveData<HasShapeInterface>) {
            val newSeparator = templateCanvas.separator.value
            newSeparator?.shapeType = it.type

            templateCanvas.separator.value = newSeparator
        }

        templateCanvas.separator.observe(requireActivity(), {
            shapeAdapter.notifyDataSetChanged()
        })

        val recyclerSize: RecyclerView = binding.shapeRecycler

        recyclerSize.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerSize.adapter = shapeAdapter

        shapeAdapter.addAllSizeList(arrayListOf(
            ShapeSeparator(R.drawable.ic_none_border_map, ShapeSeparator.NONE ),
            ShapeSeparator(R.drawable.ic_separator_v1, ShapeSeparator.LINE),
            ShapeSeparator(R.drawable.ic_shape_curved_icon, ShapeSeparator.CURVED),
            ShapeSeparator(R.drawable.ic_shape_star, ShapeSeparator.STAR),
            ShapeSeparator(R.drawable.ic_shape_stars, ShapeSeparator.STARS),
            ShapeSeparator(R.drawable.ic_shape_heart, ShapeSeparator.HEART),
            ShapeSeparator(R.drawable.ic_shape_hearts, ShapeSeparator.HEARTS)
        ))
    }

}