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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.FontAdapter
import com.nikolaydemidovez.starmap.adapters.LangAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentStarsV1ControllerBinding
import com.nikolaydemidovez.starmap.pojo.FontText
import com.nikolaydemidovez.starmap.pojo.Graticule.Companion.DASHED
import com.nikolaydemidovez.starmap.pojo.Graticule.Companion.LINE
import com.nikolaydemidovez.starmap.pojo.Lang
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
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

        binding.checkboxEnableDashedGraticule.isChecked = templateCanvas.graticule.value!!.shape == DASHED
        binding.checkboxEnableDashedGraticule.setOnCheckedChangeListener { _, isChecked ->
            val newGraticule = templateCanvas.graticule.value
            newGraticule?.shape = if(isChecked) DASHED else LINE

            templateCanvas.graticule.value = newGraticule
        }

        binding.opacityGraticule.text = templateCanvas.graticule.value!!.opacity!!.toString()
        binding.sliderOpacityGraticule.progress = templateCanvas.graticule.value!!.opacity!!
        binding.sliderOpacityGraticule.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newGraticule = templateCanvas.graticule.value
                    newGraticule?.opacity = seekBar.progress

                    templateCanvas.graticule.value = newGraticule
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityGraticule.text = progress.toString()
                }
            }
        )

        binding.widthGraticule.text = templateCanvas.graticule.value!!.width!!.toInt().toString()
        binding.sliderWidthGraticule.progress = templateCanvas.graticule.value!!.width!!.toInt()
        binding.sliderWidthGraticule.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newGraticule = templateCanvas.graticule.value
                    newGraticule?.width = seekBar.progress.toFloat()

                    templateCanvas.graticule.value = newGraticule
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

        binding.opacityStars.text = templateCanvas.stars.value!!.opacity!!.toString()
        binding.sliderOpacityStars.progress = templateCanvas.stars.value!!.opacity!!
        binding.sliderOpacityStars.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newStars = templateCanvas.stars.value
                    newStars?.opacity = seekBar.progress

                    templateCanvas.stars.value = newStars
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityStars.text = progress.toString()
                }
            }
        )

        binding.sizeStars.text = templateCanvas.stars.value!!.size!!.toInt().toString()
        binding.sliderSizeStars.progress = templateCanvas.stars.value!!.size!!.toInt()
        binding.sliderSizeStars.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newStars = templateCanvas.stars.value
                    newStars?.size = seekBar.progress.toFloat()

                    templateCanvas.stars.value = newStars
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

        binding.opacityConstellations.text = templateCanvas.constellations.value!!.opacity!!.toString()
        binding.sliderOpacityConstellations.progress = templateCanvas.constellations.value!!.opacity!!
        binding.sliderOpacityConstellations.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newConstellations = templateCanvas.constellations.value
                    newConstellations?.opacity = seekBar.progress

                    templateCanvas.constellations.value = newConstellations
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.opacityConstellations.text = progress.toString()
                }
            }
        )

        binding.widthConstellations.text = templateCanvas.constellations.value!!.width!!.toInt().toString()
        binding.sliderWidthConstellations.progress = templateCanvas.constellations.value!!.width!!.toInt()
        binding.sliderWidthConstellations.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newConstellations = templateCanvas.constellations.value
                    newConstellations?.width = seekBar.progress.toFloat()

                    templateCanvas.constellations.value = newConstellations
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

        binding.sizeNames.text = templateCanvas.namesStars.value!!.size!!.toInt().toString()
        binding.sliderSizeNames.progress = templateCanvas.namesStars.value!!.size!!.toInt()
        binding.sliderSizeNames.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onStopTrackingTouch(seekBar: SeekBar) {
                    val newNamesStars = templateCanvas.namesStars.value
                    newNamesStars?.size = seekBar.progress

                    templateCanvas.namesStars.value = newNamesStars
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}

                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    binding.sizeNames.text = progress.toString()
                }
            }
        )

        binding.editLang.setText(templateCanvas.namesStars.value!!.lang!!.label!!)
        binding.editLang.setOnClickListener {
            showLangDialog()
        }

        return root
    }

    private fun recyclerColorGraticuleInit() {
        val adapter = ColorAdapter(templateCanvas.graticule) {
            val newGraticule = templateCanvas.graticule.value
            newGraticule?.color = it

            templateCanvas.graticule.value = newGraticule
        }

        templateCanvas.graticule.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorGraticuleRecycler

        recyclerColors.addOnItemTouchListener(disablerColorGraticuleRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorStarsInit() {
        val adapter = ColorAdapter(templateCanvas.stars) {
            val newStars = templateCanvas.stars.value
            newStars?.color = it

            templateCanvas.stars.value = newStars
        }

        templateCanvas.stars.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorStarsRecycler

        recyclerColors.addOnItemTouchListener(disablerColorGraticuleRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorConstellationsInit() {
        val adapter = ColorAdapter(templateCanvas.constellations) {
            val newConstellations = templateCanvas.constellations.value
            newConstellations?.color = it

            templateCanvas.constellations.value = newConstellations
        }

        templateCanvas.constellations.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorConstellationsRecycler

        recyclerColors.addOnItemTouchListener(disablerColorConstellationsRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun recyclerColorNamesInit() {
        val adapter = ColorAdapter(templateCanvas.namesStars) {
            val newNamesStars = templateCanvas.namesStars.value
            newNamesStars?.color = it

            templateCanvas.namesStars.value = newNamesStars
        }

        templateCanvas.namesStars.observe(requireActivity(), {
            adapter.notifyDataSetChanged()
        })

        val recyclerColors: RecyclerView = binding.colorNamesRecycler

        recyclerColors.addOnItemTouchListener(disablerColorNamesRecycler)
        recyclerColors.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerColors.adapter = adapter

        adapter.addAllColorList(templateCanvas.colorList)
    }

    private fun showLangDialog() {
        val width = (resources.displayMetrics.widthPixels * 0.9).toInt()

        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.simple_picker_layout)
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        val listView = dialog.findViewById<ListView>(R.id.listView)


        val adapter = LangAdapter(activity, templateCanvas, dialog) {
            val newNamesStars = templateCanvas.namesStars.value
            newNamesStars?.lang = it

            templateCanvas.namesStars.value = newNamesStars

            binding.editLang.setText(it.label)
        }

        listView.adapter = adapter

        dialog.show()
    }

}