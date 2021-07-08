package com.nikolaydemidovez.starmap.pages.template

import android.app.Dialog
import android.graphics.PointF
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import android.view.View.OnLayoutChangeListener
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Toast
import androidx.core.graphics.scale
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davemorrissey.labs.subscaleview.ImageSource
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateCanvas
import com.nikolaydemidovez.starmap.templates.half_v1.HalfV1TemplateCanvas


class TemplateFragment : Fragment() {

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var adapter: ControllerAdapter
    private lateinit var templateCanvas: TemplateCanvas

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val templateName = arguments?.getString("templateName")
        val templateTitle = arguments?.getString("templateTitle")

        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = templateTitle
        }

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

        adapter = ControllerAdapter(childFragmentManager, templateCanvas)

        binding.fullScreen.setOnClickListener {
            showFullScreenCanvasDialog(templateCanvas)
        }

        recyclerInit()

        return root
    }

    private fun recyclerInit() {
        val recyclerTemplates: RecyclerView = binding.recyclerControllers

        recyclerTemplates.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerTemplates.adapter = adapter

        templateViewModel.controllerList.observe(viewLifecycleOwner, {
            adapter.addAllControllerList(it)
        })

    }

    private fun showFullScreenCanvasDialog(templateCanvas: TemplateCanvas) {
        val dialog = Dialog(requireContext(), R.style.full_screen_dialog)
        dialog.setContentView(R.layout.full_screen_layout)

        dialog.show()

        val imageView = dialog.findViewById<SubsamplingScaleImageView>(R.id.full_canvas)
        imageView.setImage(ImageSource.bitmap(templateCanvas.bitmap))
    }

    private fun getTemplateCanvas(templateName: String): TemplateCanvas = when(templateName) {
        "classic_v1" -> ClassicV1TemplateCanvas(requireActivity())
        "half_v1" -> HalfV1TemplateCanvas(requireActivity())

        else -> ClassicV1TemplateCanvas(requireActivity())
    }
}