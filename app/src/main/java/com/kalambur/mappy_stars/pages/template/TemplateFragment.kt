package com.kalambur.mappy_stars.pages.template

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
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
import com.kalambur.mappy_stars.MainActivity
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.adapters.ControllerTabAdapter
import com.kalambur.mappy_stars.databinding.FragmentTemplateBinding
import com.kalambur.mappy_stars.interfaces.IOnBackPressed
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.templates.TemplateCanvas
import com.kalambur.mappy_stars.templates.classic_v1.ClassicV1TemplateCanvas
import androidx.navigation.fragment.findNavController
import com.kalambur.mappy_stars.templates.full_v1.FullV1TemplateCanvas
import com.kalambur.mappy_stars.templates.half_v1.HalfV1TemplateCanvas
import com.kalambur.mappy_stars.templates.polaroid_v1.PolaroidV1TemplateCanvas
import com.kalambur.mappy_stars.templates.understars_v1.UnderStarsV1TemplateCanvas
import com.kalambur.mappy_stars.utils.admob.AdmobUtil
import com.kalambur.mappy_stars.utils.extensions.dismissWithAds


class TemplateFragment : Fragment(), IOnBackPressed {
    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var templateCanvas: TemplateCanvas

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()
        val templateId = arguments?.getInt("templateId")

        templateViewModel = ViewModelProviders.of(this, TemplateViewModelFactory(requireContext(), templateId!!)).get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        templateViewModel.template.observe(viewLifecycleOwner, { it ->
            templateCanvas = getTemplateCanvas(it)

            binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())

            templateCanvas.doneRedraw.observe(requireActivity()) {
                if(it) {
                    binding.canvasImage.setImageBitmap(templateCanvas.getShortBitmap())
                }
            }

            binding.canvasImage.setOnClickListener {
                showFullScreenCanvasDialog(templateCanvas)
            }

            initTabControllers()
        })

        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        return root
    }

    override fun onBackPressed(): Boolean {
        showAskSaveDialog()

        return true
    }

    private fun showAskSaveDialog() {
        val layoutInflater = LayoutInflater.from(requireContext())
        val layout: View = layoutInflater.inflate(R.layout.dialog_ask_layout, null)

        val title = "Вы уверены, что хотите покинуть проект?"
        layout.findViewById<TextView>(R.id.title).text = title

        val builder = AlertDialog.Builder(requireContext(), R.style.dialog_corners)
        builder.setPositiveButton("Да", null)
        builder.setNegativeButton("Нет", null)
        builder.setView(layout)

        val dialog = builder.create()

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))

            val okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            okButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.dark))
            okButton.setOnClickListener {
               findNavController().popBackStack()

               dialog.dismissWithAds(requireActivity())
            }
        }

        dialog.show()

        val width = (resources.displayMetrics.widthPixels * 0.8).toInt()
        dialog.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
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

    private fun getTemplateCanvas(template: Template): TemplateCanvas = when(template.name) {
        "classic_v1" -> ClassicV1TemplateCanvas(activity as MainActivity, template)
        "half_v1" -> HalfV1TemplateCanvas(activity as MainActivity, template)
        "polaroid_v1" -> PolaroidV1TemplateCanvas(activity as MainActivity, template)
        "full_v1" -> FullV1TemplateCanvas(activity as MainActivity, template)
        "understars_v1" -> UnderStarsV1TemplateCanvas(activity as MainActivity, template)

        else -> ClassicV1TemplateCanvas(activity as MainActivity, template)
    }
}