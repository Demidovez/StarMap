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
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder
import com.nikolaydemidovez.starmap.pojo.ShapeSeparator
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateCanvas
import java.util.*

class TemplateFragment : Fragment() {
    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var templateCanvas: TemplateCanvas

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()
        val templateName = arguments?.getString("templateName")
        val templateProperties = getTemplateProperties(templateName)

        templateViewModel = ViewModelProviders.of(this, TemplateViewModelFactory(templateName!!)).get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        templateCanvas = getTemplateCanvas(templateName, templateProperties)

        binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())

        templateCanvas.doneRedraw.observe(requireActivity()) {
            if(it) {
                binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())
            }
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.canvasImage.setOnClickListener {
            showFullScreenCanvasDialog(templateCanvas)
        }

        initTabControllers()

        return root
    }

    private fun initTabControllers() {
        val controllerList = templateCanvas.getControllerList()

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

        binding.tabsViewpager.adapter = ControllerTabAdapter(requireActivity().supportFragmentManager, lifecycle, controllerList.size, templateCanvas)
        binding.tabsViewpager.isUserInputEnabled = false

        TabLayoutMediator(binding.tabLayout, binding.tabsViewpager, false, false) { tab, position ->
            tab.text = controllerList[position].title
            tab.icon = controllerList[position].drawable
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

    private fun getTemplateCanvas(templateName: String, template: Template): TemplateCanvas = when(templateName) {
        "classic_v1" -> ClassicV1TemplateCanvas(activity as MainActivity, template)
        //"half_v1" -> HalfV1TemplateCanvas(activity as MainActivity)

        else -> ClassicV1TemplateCanvas(activity as MainActivity, template)
    }

    private fun getTemplateProperties(templateName: String?): Template {
        return Template(
            holstWidth = 2480F,
            holstHeight = 3508F,
            holstColor = "#FFFFFF",
            hasBorderHolst = true,
            borderHolstIndent = 100F,
            borderHolstWidth = 10F,
            borderHolstColor = "#000000",
            starMapRadius = 900F,
            starMapColor = "#000000",
            starMapBorderWidth = 15F,
            starMapBorderType = ShapeMapBorder.NONE,
            starMapBorderColor = "#000000",
            descFontName = "Comfortaa Regular",
            descFontResId = R.font.comfortaa_regular,
            descFontSize = 120F,
            descFontColor = "#000000",
            descText = "День, когда сошлись\nвсе звезды вселенной...",
            hasEventDateInLocation = true,
            eventDate = Calendar.getInstance().time.time,
            hasEventTimeInLocation = true,
            eventTime = "",
            hasEventCityInLocation = true,
            eventLocation = "Москва",
            eventCountry = "Россия",
            hasEditResultLocationText = false,
            resultLocationText = "",
            locationFontName = "Comfortaa Regular",
            locationFontResId = R.font.comfortaa_regular,
            locationFontSize = 60F,
            locationFontColor = "#000000",
            hasEventCoordinatesInLocation = true,
            coordinatesLatitude = 55.755826F,
            coordinatesLongitude = 37.6173F,
            separatorWidth = 1000F,
            separatorType = ShapeSeparator.CURVED,
            separatorColor = "#000000",
            hasGraticule = true,
            graticuleWidth = 2F,
            graticuleColor = "#FFFFFF",
            graticuleOpacity = 70,
            graticuleType = TemplateCanvas.LINE_GRATICULE,
            hasMilkyWay = true,
            hasNames = true,
            namesStarsSize = 12,
            namesStarsColor = "#FFFFFF",
            namesStarsLangLabel = "Русский",
            namesStarsLangName = "ru",
            hasConstellations = true,
            constellationsWidth = 3F,
            constellationsColor = "#FFFFFF",
            constellationsOpacity = 100,
            starsSize = 17F,
            starsColor = "#FFFFFF",
            starsOpacity = 100
        )
    }
}