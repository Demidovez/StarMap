package com.nikolaydemidovez.starmap.controllers.map_v1

import adapters.ColorAdapter
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
import com.nikolaydemidovez.starmap.databinding.FragmentMapV1ControllerBinding
import com.nikolaydemidovez.starmap.interfaces.HasShapeInterface
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder.Companion.CIRCLE
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder.Companion.COMPASS
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder.Companion.NONE
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper

class MapV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: MapV1ControllerViewModel
    private lateinit var binding: FragmentMapV1ControllerBinding
    private lateinit var backgroundColorMapAdapter: ColorAdapter
    private lateinit var colorMapBorderAdapter: ColorAdapter
    private val disablerColorRecycler = Helper.Companion.RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(MapV1ControllerViewModel::class.java)
        binding = FragmentMapV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.sizeMap.text = templateCanvas.starMapRadius.value!!.toInt().toString()
        binding.sliderSizeMap.progress = templateCanvas.starMapRadius.value!!.toInt()
        binding.sliderSizeMap.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.starMapRadius.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.sizeMap.text = progress.toString()
                }
            }
        )

        binding.widthMapBorder.text = templateCanvas.starMapBorder.value!!.width!!.toInt().toString()
        binding.sliderWidthMapBorder.progress = templateCanvas.starMapBorder.value!!.width!!.toInt()
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
            binding.widthUnit.alpha = Helper.shadowAlpha(isEnabled)
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

        templateCanvas.starMapBorder.observe(requireActivity(), {
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