package com.nikolaydemidovez.starmap.pages.template

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayoutMediator
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.ControllerTabAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateCanvas
import com.nikolaydemidovez.starmap.templates.half_v1.HalfV1TemplateCanvas

class TemplateFragment : Fragment() {
    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var templateCanvas: TemplateCanvas

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()
        val templateName = arguments?.getString("templateName")

        templateViewModel = ViewModelProviders.of(this, TemplateViewModelFactory(templateName!!)).get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        templateCanvas = getTemplateCanvas(templateName)

        binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())

        templateCanvas.setOnDrawListener(object: TemplateCanvas.OnDrawListener {
            override fun onDraw() {
                binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())
            }
        })

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.canvasImage.setOnClickListener {
            showFullScreenCanvasDialog(templateCanvas)
        }

        binding.fullScreen.setOnClickListener {
            showFullScreenCanvasDialog(templateCanvas)
        }

        initTabControllers()

        return root
    }

    private fun initTabControllers() {
        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabLayout.isInlineLabel = true
        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(requireContext(), R.color.dark), BlendModeCompat.SRC_ATOP)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(requireContext(), R.color.dark_gray), BlendModeCompat.SRC_ATOP)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.tabsViewpager.adapter = ControllerTabAdapter(requireActivity().supportFragmentManager, lifecycle, templateCanvas.getControllerList().size, templateCanvas)
        binding.tabsViewpager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.tabsViewpager, false, false) { tab, position ->
            tab.text = templateCanvas.getControllerList()[position].title
            tab.icon = templateCanvas.getControllerList()[position].drawable
            tab.icon?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(ContextCompat.getColor(requireContext(), R.color.dark_gray), BlendModeCompat.SRC_ATOP)
        }.attach()
    }

    private fun editActionAndStatusBar() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.hide()
        }

        val window: Window = requireActivity().window

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.gray)
        }
    }

    private fun showFullScreenCanvasDialog(templateCanvas: TemplateCanvas) {
        val dialog = Dialog(requireContext(), R.style.full_screen_dialog)
        dialog.setContentView(R.layout.full_screen_layout)

        dialog.show()

        val imageView = dialog.findViewById<SubsamplingScaleImageView>(R.id.full_canvas)
        imageView.setImage(ImageSource.bitmap(templateCanvas.bitmap))
    }

    private fun getTemplateCanvas(templateName: String): TemplateCanvas = when(templateName) {
        "classic_v1" -> ClassicV1TemplateCanvas(activity as MainActivity)
        "half_v1" -> HalfV1TemplateCanvas(activity as MainActivity)

        else -> ClassicV1TemplateCanvas(activity as MainActivity)
    }
}