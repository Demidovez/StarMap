package com.kalambur.mappy_stars.controllers.map_v4

import com.kalambur.mappy_stars.adapters.ColorAdapter
import adapters.ShapeAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.databinding.FragmentMapV1ControllerBinding
import com.kalambur.mappy_stars.databinding.FragmentMapV2ControllerBinding
import com.kalambur.mappy_stars.databinding.FragmentMapV3ControllerBinding
import com.kalambur.mappy_stars.databinding.FragmentMapV4ControllerBinding
import com.kalambur.mappy_stars.interfaces.HasShapeInterface
import com.kalambur.mappy_stars.pojo.ShapeMapBorder
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.CIRCLE
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.COMPASS
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.NONE
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.helpers.Helper

class MapV4ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var binding: FragmentMapV4ControllerBinding
    private lateinit var backgroundColorMapAdapter: ColorAdapter
    private lateinit var backgroundColorBlockAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapV4ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        recyclerBackgroundMapInit()

        binding.heightBlock.text = templateCanvas.textBlock.value!!.height!!.toInt().toString()
        binding.sliderHeightBlock.progress = templateCanvas.textBlock.value!!.height!!.toInt()
        binding.sliderHeightBlock.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newTextBlock = templateCanvas.textBlock.value!!
                    newTextBlock.height = seekBar.progress.toFloat()

                    templateCanvas.textBlock.value = newTextBlock
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.heightBlock.text = progress.toString()
                }
            }
        )

        binding.widthBlock.text = templateCanvas.textBlock.value!!.width!!.toInt().toString()
        binding.sliderWidthBlock.progress = templateCanvas.textBlock.value!!.width!!.toInt()
        binding.sliderWidthBlock.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newTextBlock = templateCanvas.textBlock.value!!
                    newTextBlock.width = seekBar.progress.toFloat()

                    templateCanvas.textBlock.value = newTextBlock
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.widthBlock.text = progress.toString()
                }
            }
        )

        binding.indentBlock.text = templateCanvas.textBlock.value!!.indent!!.toInt().toString()
        binding.sliderIndentBlock.progress = templateCanvas.textBlock.value!!.indent!!.toInt()
        binding.sliderIndentBlock.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newTextBlock = templateCanvas.textBlock.value!!
                    newTextBlock.indent = seekBar.progress.toFloat()

                    templateCanvas.textBlock.value = newTextBlock
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.indentBlock.text = progress.toString()
                }
            }
        )

        recyclerBackgroundBlockInit()

        return root
    }

    private fun recyclerBackgroundMapInit() {
        backgroundColorMapAdapter = ColorAdapter(requireActivity(), templateCanvas.starMapColor) {
            templateCanvas.starMapColor.value = it
        }

        templateCanvas.starMapColor.observe(requireActivity(), {
            backgroundColorMapAdapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorBackgroundRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = backgroundColorMapAdapter

        backgroundColorMapAdapter.addAllColorList(templateCanvas.colorList)
    }
    private fun recyclerBackgroundBlockInit() {
        backgroundColorBlockAdapter = ColorAdapter(requireActivity(), templateCanvas.textBlockColor) {
            templateCanvas.textBlockColor.value = it
        }

        templateCanvas.textBlockColor.observe(requireActivity(), {
            backgroundColorBlockAdapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorBackgroundBlockRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = backgroundColorBlockAdapter

        backgroundColorBlockAdapter.addAllColorList(templateCanvas.colorList)
    }

}