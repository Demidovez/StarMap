package com.kalambur.mappy_stars.controllers.map_v2

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
import com.kalambur.mappy_stars.interfaces.HasShapeInterface
import com.kalambur.mappy_stars.pojo.ShapeMapBorder
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.CIRCLE
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.COMPASS
import com.kalambur.mappy_stars.pojo.ShapeMapBorder.Companion.NONE
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.utils.helpers.Helper

class MapV2ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var binding: FragmentMapV2ControllerBinding
    private lateinit var backgroundColorMapAdapter: ColorAdapter
    private lateinit var colorMapBorderAdapter: ColorAdapter
    private val disablerColorRecycler = Helper.Companion.RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMapV2ControllerBinding.inflate(inflater, container, false)

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

        binding.widthMapBorder.text = templateCanvas.starMapBorder.value!!.width.toInt().toString()
        binding.sliderWidthMapBorder.progress = templateCanvas.starMapBorder.value!!.width.toInt()
        binding.sliderWidthMapBorder.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newStarMapBorder = templateCanvas.starMapBorder.value
                    newStarMapBorder?.width = seekBar.progress.toFloat()

                    templateCanvas.starMapBorder.value = newStarMapBorder
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.widthMapBorder.text = progress.toString()
                }
            }
        )

        recyclerBackgroundMapInit()

        templateCanvas.starMapBorder.observe(requireActivity(),  {
            val isEnabled = it.shapeType != NONE

            binding.labelWidthBorder.alpha = Helper.shadowAlpha(isEnabled)
            binding.widthMapBorder.alpha = Helper.shadowAlpha(isEnabled)
            binding.sliderWidthMapBorder.isEnabled = isEnabled
            binding.labelColorBorder.alpha = Helper.shadowAlpha(isEnabled)
            disablerColorRecycler.isEnable = isEnabled
            binding.colorBorderRecycler.alpha = Helper.shadowAlpha(isEnabled)

        })

        recyclerShapeMapBorderInit()

        recyclerColorMapBorderInit()

        return root
    }

    private fun recyclerBackgroundMapInit() {
        backgroundColorMapAdapter = ColorAdapter(templateCanvas.starMapColor) {
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

    private fun recyclerColorMapBorderInit() {
        colorMapBorderAdapter = ColorAdapter(templateCanvas.starMapBorderColor) {
            templateCanvas.starMapBorderColor.value = it
        }

        templateCanvas.starMapBorderColor.observe(requireActivity(), {
            colorMapBorderAdapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorBorderRecycler

        recyclerColors.addOnItemTouchListener(disablerColorRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = colorMapBorderAdapter

        colorMapBorderAdapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerShapeMapBorderInit() {
        val shapeBorderAdapter = ShapeAdapter(templateCanvas.starMapBorder as MutableLiveData<HasShapeInterface>) {
            val newStarMapBorder = templateCanvas.starMapBorder.value
            newStarMapBorder?.shapeType = it.type

            templateCanvas.starMapBorder.value = newStarMapBorder
        }

        templateCanvas.starMapBorder.observe(requireActivity(), {
            shapeBorderAdapter.notifyDataSetChanged()
        })

        val recyclerSize: RecyclerView = binding.shapeRecycler

        recyclerSize.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerSize.adapter = shapeBorderAdapter

        shapeBorderAdapter.addAllSizeList(arrayListOf(
            ShapeMapBorder(R.drawable.ic_none_border_map, NONE ),
            ShapeMapBorder(R.drawable.ic_circle_border_map, CIRCLE),
            ShapeMapBorder(R.drawable.ic_compass_border_map, COMPASS )
        ))
    }

}