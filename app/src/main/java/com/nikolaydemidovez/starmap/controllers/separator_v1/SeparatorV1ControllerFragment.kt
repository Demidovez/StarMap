package com.nikolaydemidovez.starmap.controllers.separator_v1

import adapters.ColorAdapter
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
import com.nikolaydemidovez.starmap.databinding.FragmentSeparatorV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.shadowAlpha
import java.lang.Compiler.enable

import com.nikolaydemidovez.starmap.utils.helpers.Helper.Companion.RecyclerViewDisabler




class SeparatorV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: SeparatorV1ControllerViewModel
    private lateinit var binding: FragmentSeparatorV1ControllerBinding
    private lateinit var colorAdapter: ColorAdapter
    private val disablerColorRecycler = RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(SeparatorV1ControllerViewModel::class.java)
        binding = FragmentSeparatorV1ControllerBinding.inflate(inflater, container, false)

        colorAdapter = ColorAdapter(templateCanvas.separatorColor) {
            templateCanvas.separatorColor.value = it
        }

        templateCanvas.separatorColor.observe(requireActivity(), {
            colorAdapter.notifyDataSetChanged()
        })

        val root: View = binding.root

        binding.checkboxEnableSeparator.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasSeparator.value = isChecked
        }

        templateCanvas.hasSeparator.observe(requireActivity(), {
            binding.labelColorRecycler.alpha = shadowAlpha(it)
            disablerColorRecycler.isEnable = it
            binding.sliderWidth.isEnabled = it
            binding.sliderWidth.alpha = shadowAlpha(it)
            binding.colorRecycler.alpha = shadowAlpha(it)
            binding.width.alpha = shadowAlpha(it)
            binding.labelWidthSeparator.alpha = shadowAlpha(it)
        })

        recyclerColorsInit()

        binding.sliderWidth.progress = templateCanvas.separatorWidth.value!!.toInt()
        binding.width.text           = templateCanvas.separatorWidth.value!!.toInt().toString()

        binding.sliderWidth.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.separatorWidth.value = seekBar.progress.coerceAtLeast(1).toFloat()
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

        colorAdapter.addAllColorList(arrayListOf(
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