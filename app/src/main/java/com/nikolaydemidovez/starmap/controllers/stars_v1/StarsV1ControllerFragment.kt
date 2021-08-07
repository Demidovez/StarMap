package com.nikolaydemidovez.starmap.controllers.stars_v1

import adapters.ColorAdapter
import android.app.Dialog
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.SeekBar
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.LangAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentStarsV1ControllerBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.templates.TemplateCanvas.Companion.DASHED_GRATICULE
import com.nikolaydemidovez.starmap.templates.TemplateCanvas.Companion.LINE_GRATICULE
import com.nikolaydemidovez.starmap.utils.helpers.Helper

class StarsV1ControllerFragment(private val templateCanvas: TemplateCanvas) : Fragment() {

    private lateinit var viewModel: StarsV1ControllerViewModel
    private lateinit var binding: FragmentStarsV1ControllerBinding
    private val disablerColorGraticuleRecycler = Helper.Companion.RecyclerViewDisabler(true)
    private val disablerColorConstellationsRecycler = Helper.Companion.RecyclerViewDisabler(true)
    private val disablerColorNamesRecycler = Helper.Companion.RecyclerViewDisabler(true)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewModel = ViewModelProvider(this).get(StarsV1ControllerViewModel::class.java)
        binding = FragmentStarsV1ControllerBinding.inflate(inflater, container, false)

        val root: View = binding.root

        binding.checkboxEnableGraticule.isChecked = templateCanvas.hasGraticule.value!!
        binding.checkboxEnableGraticule.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasGraticule.value = isChecked
        }

        templateCanvas.hasGraticule.observe(requireActivity(), {
            binding.labelColorGraticuleRecycler.alpha = Helper.shadowAlpha(it)
            disablerColorGraticuleRecycler.isEnable = it
            binding.labelOpacityGraticule.alpha = Helper.shadowAlpha(it)
            binding.opacityGraticule.alpha = Helper.shadowAlpha(it)
            binding.sizeUnitOpacityGraticule.alpha = Helper.shadowAlpha(it)
            binding.sliderOpacityGraticule.alpha = Helper.shadowAlpha(it)
            binding.sliderOpacityGraticule.isEnabled = it
            binding.labelWidthGraticule.alpha = Helper.shadowAlpha(it)
            binding.widthGraticule.alpha = Helper.shadowAlpha(it)
            binding.sizeUnitWidth.alpha = Helper.shadowAlpha(it)
            binding.sliderWidthGraticule.alpha = Helper.shadowAlpha(it)
            binding.sliderWidthGraticule.isEnabled = it
            binding.colorGraticuleRecycler.alpha = Helper.shadowAlpha(it)
            binding.labelEnableDashedGraticule.alpha = Helper.shadowAlpha(it)
            binding.checkboxEnableDashedGraticule.isEnabled = it
            binding.checkboxEnableDashedGraticule.alpha = Helper.shadowAlpha(it)
        })

        recyclerColorGraticuleInit()

        binding.checkboxEnableDashedGraticule.isChecked = templateCanvas.graticuleType.value!! == DASHED_GRATICULE
        binding.checkboxEnableDashedGraticule.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.graticuleType.value = if(isChecked) DASHED_GRATICULE else LINE_GRATICULE
        }

        binding.opacityGraticule.text = templateCanvas.graticuleOpacity.value.toString()
        binding.sliderOpacityGraticule.progress = templateCanvas.graticuleOpacity.value!!
        binding.sliderOpacityGraticule.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.graticuleOpacity.value = seekBar.progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityGraticule.text = progress.toString()
                }
            }
        )

        binding.widthGraticule.text = templateCanvas.graticuleWidth.value!!.toInt().toString()
        binding.sliderWidthGraticule.progress = templateCanvas.graticuleWidth.value!!.toInt()
        binding.sliderWidthGraticule.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.graticuleWidth.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.widthGraticule.text = progress.toString()
                }
            }
        )

        binding.checkboxEnableMilkyWay.isChecked = templateCanvas.hasMilkyWay.value!!
        binding.checkboxEnableMilkyWay.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasMilkyWay.value = isChecked
        }

        recyclerColorStarsInit()

        binding.opacityStars.text = templateCanvas.starsOpacity.value!!.toString()
        binding.sliderOpacityStars.progress = templateCanvas.starsOpacity.value!!
        binding.sliderOpacityStars.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.starsOpacity.value = seekBar.progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityStars.text = progress.toString()
                }
            }
        )

        binding.sizeStars.text = templateCanvas.starsSize.value!!.toInt().toString()
        binding.sliderSizeStars.progress = templateCanvas.starsSize.value!!.toInt()
        binding.sliderSizeStars.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.starsSize.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.sizeStars.text = progress.toString()
                }
            }
        )

        binding.checkboxEnableConstellations.isChecked = templateCanvas.hasConstellations.value!!
        binding.checkboxEnableConstellations.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasConstellations.value = isChecked
        }

        templateCanvas.hasConstellations.observe(requireActivity(), {
            binding.labelColorConstellations.alpha = Helper.shadowAlpha(it)
            binding.colorConstellationsRecycler.alpha = Helper.shadowAlpha(it)
            binding.labelOpacityConstellations.alpha = Helper.shadowAlpha(it)
            binding.opacityConstellations.alpha = Helper.shadowAlpha(it)
            binding.sizeUnitOpacityConstellations.alpha = Helper.shadowAlpha(it)
            binding.sliderOpacityConstellations.alpha = Helper.shadowAlpha(it)
            binding.labelWidthConstellations.alpha = Helper.shadowAlpha(it)
            binding.widthConstellations.alpha = Helper.shadowAlpha(it)
            binding.sizeUnitWidthConstellations.alpha = Helper.shadowAlpha(it)
            binding.sliderWidthConstellations.alpha = Helper.shadowAlpha(it)
            binding.sliderOpacityConstellations.isEnabled = it
            binding.sliderWidthConstellations.isEnabled = it
            disablerColorConstellationsRecycler.isEnable = it
        })

        recyclerColorConstellationsInit()

        binding.opacityConstellations.text = templateCanvas.constellationsOpacity.value!!.toString()
        binding.sliderOpacityConstellations.progress = templateCanvas.constellationsOpacity.value!!
        binding.sliderOpacityConstellations.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.constellationsOpacity.value = seekBar.progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityConstellations.text = progress.toString()
                }
            }
        )

        binding.widthConstellations.text = templateCanvas.constellationsWidth.value!!.toInt().toString()
        binding.sliderWidthConstellations.progress = templateCanvas.constellationsWidth.value!!.toInt()
        binding.sliderWidthConstellations.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.constellationsWidth.value = seekBar.progress.toFloat()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.widthConstellations.text = progress.toString()
                }
            }
        )

        binding.checkboxEnableNames.isChecked = templateCanvas.hasNames.value!!
        binding.checkboxEnableNames.setOnCheckedChangeListener { _, isChecked ->
            templateCanvas.hasNames.value = isChecked
        }

        templateCanvas.hasNames.observe(requireActivity(), {
            binding.labelColorNames.alpha = Helper.shadowAlpha(it)
            binding.colorNamesRecycler.alpha = Helper.shadowAlpha(it)
            binding.labelSizeNames.alpha = Helper.shadowAlpha(it)
            binding.sizeNames.alpha = Helper.shadowAlpha(it)
            binding.sizeUnitNames.alpha = Helper.shadowAlpha(it)
            binding.sliderSizeNames.alpha = Helper.shadowAlpha(it)
            binding.labelLang.alpha = Helper.shadowAlpha(it)
            binding.editLang.alpha = Helper.shadowAlpha(it)
            binding.editLang.isEnabled = it
            disablerColorNamesRecycler.isEnable = it
            binding.sliderSizeNames.isEnabled = it
        })

        recyclerColorNamesInit()

        binding.sizeNames.text = templateCanvas.namesStarsSize.value!!.toInt().toString()
        binding.sliderSizeNames.progress = templateCanvas.namesStarsSize.value!!.toInt()
        binding.sliderSizeNames.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    templateCanvas.namesStarsSize.value = seekBar.progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.sizeNames.text = progress.toString()
                }
            }
        )

        binding.editLang.setText(templateCanvas.namesStarsLang.value!!.label!!)
        binding.editLang.setOnClickListener {
            showLangDialog()
        }

        return root
    }

    private fun recyclerColorGraticuleInit() {
        val adapter = ColorAdapter(templateCanvas.graticuleColor) {
            templateCanvas.graticuleColor.value = it
        }

        templateCanvas.graticuleColor.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorGraticuleRecycler

        recyclerColors.addOnItemTouchListener(disablerColorGraticuleRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorStarsInit() {
        val adapter = ColorAdapter(templateCanvas.starsColor) {
            templateCanvas.starsColor.value = it
        }

        templateCanvas.starsColor.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorStarsRecycler

        recyclerColors.addOnItemTouchListener(disablerColorGraticuleRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorConstellationsInit() {
        val adapter = ColorAdapter(templateCanvas.constellationsColor) {
            templateCanvas.constellationsColor.value = it
        }

        templateCanvas.constellationsColor.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorConstellationsRecycler

        recyclerColors.addOnItemTouchListener(disablerColorConstellationsRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorNamesInit() {
        val adapter = ColorAdapter(templateCanvas.namesStarsColor) {
            templateCanvas.namesStarsColor.value = it
        }

        templateCanvas.namesStarsColor.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorNamesRecycler

        recyclerColors.addOnItemTouchListener(disablerColorNamesRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun showLangDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.picker_simple_layout, null)

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setView(layout)

        val dialog = builder.create()

        val listView = layout.findViewById<ListView>(R.id.listView)

        val adapter = LangAdapter(activity, templateCanvas, dialog) {
            templateCanvas.namesStarsLang.value = it

            binding.editLang.setText(it.label)
        }

        listView.adapter = adapter

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.6).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }

}