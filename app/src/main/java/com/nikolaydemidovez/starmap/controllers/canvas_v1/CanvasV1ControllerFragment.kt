package com.nikolaydemidovez.starmap.controllers.canvas_v1

import adapters.ColorAdapter
import adapters.HolstSizeAdapter
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
import android.widget.SeekBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.pojo.Holst
import com.nikolaydemidovez.starmap.utils.helpers.Helper

class CanvasV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {
    private lateinit var viewModel: CanvasV1ControllerViewModel
    private lateinit var binding: FragmentCanvasV1ControllerBinding
    private lateinit var holstSizeAdapter: HolstSizeAdapter
    private lateinit var backgroundColorAdapter: ColorAdapter
    private lateinit var borderColorAdapter: ColorAdapter
    private val disablerColorRecycler = Helper.Companion.RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(CanvasV1ControllerViewModel::class.java)
        binding = FragmentCanvasV1ControllerBinding.inflate(inflater, container, false)

        holstSizeAdapter = HolstSizeAdapter(templateCanvas.holst) {
            val newHolst = templateCanvas.holst.value
            newHolst?.width = it.width
            newHolst?.height = it.height
            newHolst?.title = it.title
            newHolst?.subTitle = it.subTitle

            templateCanvas.holst.value = newHolst
        }

        backgroundColorAdapter = ColorAdapter(templateCanvas.holst) {
            val newHolst = templateCanvas.holst.value
            newHolst?.color = it

            templateCanvas.holst.value = newHolst
        }

        borderColorAdapter = ColorAdapter(templateCanvas.borderHolst) {
            val newBolderHolst = templateCanvas.borderHolst.value
            newBolderHolst?.color = it

            templateCanvas.borderHolst.value = newBolderHolst
        }

        templateCanvas.holst.observe(requireActivity(), {
            holstSizeAdapter.notifyDataSetChanged()
            backgroundColorAdapter.notifyDataSetChanged()
        })

        templateCanvas.borderHolst.observe(requireActivity(), {
            borderColorAdapter.notifyDataSetChanged()
        })

        val root: View = binding.root


        recyclerHolstSizeInit()

        recyclerBackgroundColorsInit()

        binding.checkboxEnableBorder.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasBorderHolst.value = isChecked
        }

        templateCanvas.hasBorderHolst.observe(requireActivity(), {
            binding.labelIndentBorder.alpha = Helper.shadowAlpha(it)
            binding.indentSize.alpha = Helper.shadowAlpha(it)
            binding.sizeIndentUnit.alpha = Helper.shadowAlpha(it)
            binding.sliderIndentBorder.isEnabled = it
            binding.labelWidthBorder.alpha = Helper.shadowAlpha(it)
            binding.sizeUnit.alpha = Helper.shadowAlpha(it)
            binding.sliderWeightBorder.isEnabled = it
            binding.labelColorBorder.alpha = Helper.shadowAlpha(it)
            disablerColorRecycler.isEnable = it
            binding.colorRecyclerBorder.alpha = Helper.shadowAlpha(it)
        })

        binding.weightBorder.text = templateCanvas.borderHolst.value!!.width!!.toInt().toString()
        binding.indentSize.text   = templateCanvas.borderHolst.value!!.indent!!.toInt().toString()

        binding.sliderWeightBorder.progress = templateCanvas.borderHolst.value!!.width!!.toInt()
        binding.sliderWeightBorder.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newBorderHolst = templateCanvas.borderHolst.value
                    newBorderHolst?.width = seekBar.progress.toFloat()

                    templateCanvas.borderHolst.value = newBorderHolst
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.weightBorder.text = progress.toString()
                }
            }
        )

        binding.sliderIndentBorder.progress = templateCanvas.borderHolst.value!!.indent!!.toInt()
        binding.sliderIndentBorder.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newBorderHolst = templateCanvas.borderHolst.value
                    newBorderHolst?.indent = seekBar.progress.toFloat()

                    templateCanvas.borderHolst.value = newBorderHolst
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.indentSize.text = progress.toString()
                }
            }
        )

        recyclerBorderColorsInit()

        return root
    }

    private fun recyclerHolstSizeInit() {
        val recyclerSize: RecyclerView = binding.colorSizeRecycler

        recyclerSize.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerSize.adapter = holstSizeAdapter

        holstSizeAdapter.addAllSizeList(arrayListOf(
            Holst("A4", "210 × 297 мм", 2480F, 3508F, null ),
            Holst("A3", "297 × 420 мм", 3508F, 4961F, null ),
            Holst("A2", "420 × 594 мм", 4961F, 7016F, null ),
            Holst("A1", "594 × 841 мм", 7016F, 9933F, null ),
        ))
    }

    private fun recyclerBackgroundColorsInit() {
        val recyclerColors: RecyclerView = binding.colorBackgroundRecycler

        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = backgroundColorAdapter

        backgroundColorAdapter.addAllColorList(arrayListOf(
            "#000000",
            "#FFFFFF",
            "#1ABC9C",
            "#16A085",
            "#2ECC71",
            "#27AE60",
            "#3498DB",
            "#2980B9",
            "#9B59B6",
            "#8E44AD",
            "#34495E",
            "#2C3E50",
            "#F1C40F",
            "#F39C12",
            "#E67E22",
            "#D35400",
            "#E74C3C",
            "#C0392B",
            "#BDC3C7",
            "#95A5A6",
            "#7F8C8D"
        ))
    }

    private fun recyclerBorderColorsInit() {
        val recyclerColors: RecyclerView = binding.colorRecyclerBorder

        recyclerColors.addOnItemTouchListener(disablerColorRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = borderColorAdapter

        borderColorAdapter.addAllColorList(arrayListOf(
            "#000000",
            "#FFFFFF",
            "#1ABC9C",
            "#16A085",
            "#2ECC71",
            "#27AE60",
            "#3498DB",
            "#2980B9",
            "#9B59B6",
            "#8E44AD",
            "#34495E",
            "#2C3E50",
            "#F1C40F",
            "#F39C12",
            "#E67E22",
            "#D35400",
            "#E74C3C",
            "#C0392B",
            "#BDC3C7",
            "#95A5A6",
            "#7F8C8D"
        ))
    }
}