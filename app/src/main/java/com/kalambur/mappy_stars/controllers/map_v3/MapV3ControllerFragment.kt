package com.kalambur.mappy_stars.controllers.map_v3

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
import com.kalambur.mappy_stars.interfaces.HasShapeInterface
import com.kalambur.mappy_stars.pojo.ShapeMapBorder
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.CIRCLE
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.COMPASS
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.NONE
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.helpers.Helper

class MapV3ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var binding: FragmentMapV3ControllerBinding
    private lateinit var backgroundColorMapAdapter: ColorAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapV3ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.positionMap.text = templateCanvas.starMapPosition.value!!.toInt().toString()
        binding.sliderPositionMap.progress = templateCanvas.starMapPosition.value!!.toInt()
        binding.sliderPositionMap.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.starMapPosition.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.positionMap.text = progress.toString()
                }
            }
        )

        binding.indentMap.text = templateCanvas.starMapRadius.value!!.toInt().toString()
        binding.sliderIndentMap.progress = templateCanvas.starMapRadius.value!!.toInt()
        binding.sliderIndentMap.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.starMapRadius.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.indentMap.text = progress.toString()
                }
            }
        )

        recyclerBackgroundMapInit()

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

}