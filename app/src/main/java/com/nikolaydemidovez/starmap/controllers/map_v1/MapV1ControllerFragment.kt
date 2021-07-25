package com.nikolaydemidovez.starmap.controllers.map_v1

import adapters.ColorAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.databinding.FragmentMapV1ControllerBinding
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

        binding.sizeMap.text = templateCanvas.starMap.value!!.radius!!.toInt().toString()
        binding.sliderSizeMap.progress = templateCanvas.starMap.value!!.radius!!.toInt()
        binding.sliderSizeMap.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newStarMap = templateCanvas.starMap.value
                    newStarMap?.radius = seekBar.progress.toFloat()

                    templateCanvas.starMap.value = newStarMap
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

        binding.checkboxEnableBorder.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasBorderMap.value = isChecked
        }

        templateCanvas.hasBorderMap.observe(requireActivity(),  {
            binding.labelShape.alpha = Helper.shadowAlpha(it)
            binding.labelWidthBorder.alpha = Helper.shadowAlpha(it)
            binding.widthMapBorder.alpha = Helper.shadowAlpha(it)
            binding.widthUnit.alpha = Helper.shadowAlpha(it)
            binding.sliderWidthMapBorder.isEnabled = it
            binding.labelColorBorder.alpha = Helper.shadowAlpha(it)
            disablerColorRecycler.isEnable = it
            binding.colorBorderRecycler.alpha = Helper.shadowAlpha(it)
        })

        recyclerColorMapBorderInit()


        return root
    }

    private fun recyclerBackgroundMapInit() {
        backgroundColorMapAdapter = ColorAdapter(templateCanvas.starMap) {
            val newStarMap = templateCanvas.starMap.value
            newStarMap?.color = it

            templateCanvas.starMap.value = newStarMap
        }

        templateCanvas.starMap.observe(requireActivity(), {
            backgroundColorMapAdapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorBackgroundRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = backgroundColorMapAdapter

        backgroundColorMapAdapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorMapBorderInit() {
        colorMapBorderAdapter = ColorAdapter(templateCanvas.starMapBorder) {
            val newStarMapBorder = templateCanvas.starMapBorder.value
            newStarMapBorder?.color = it

            templateCanvas.starMapBorder.value = newStarMapBorder
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

}