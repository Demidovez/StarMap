package com.nikolaydemidovez.starmap.pages.template

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Slide
import androidx.transition.TransitionManager
import androidx.transition.Visibility
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.controllers.canvas_v1.CanvasV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.desc_v1.DescV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.event_v1.EventV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.location_v1.LocationV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.map_v1.MapV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.save_v1.SaveV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.separator_v1.SeparatorV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.stars_v1.StarsV1ControllerFragment
import com.nikolaydemidovez.starmap.databinding.FragmentTemplateBinding
import com.nikolaydemidovez.starmap.templates.TemplateView
import com.nikolaydemidovez.starmap.templates.classic_v1.ClassicV1TemplateView
import com.nikolaydemidovez.starmap.templates.half_v1.HalfV1TemplateView
import android.app.Activity
import android.content.Context.WINDOW_SERVICE
import androidx.core.view.ViewCompat.getDisplay


class TemplateFragment : Fragment() {

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var adapter: ControllerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val templateName = arguments?.getString("templateName")
        val templateTitle = arguments?.getString("templateTitle")

        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = templateTitle
        }

        templateViewModel = ViewModelProviders
            .of(this, TemplateViewModelFactory(templateName!!))
            .get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val templateView = getTemplateView(templateName, this.context)

        addTemplateViewToPreview(templateView)
        
        adapter = ControllerAdapter(childFragmentManager, templateView)

        binding.fullScreen.setOnClickListener {
            showFullScreenCanvasDialog(templateView)
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

    private fun showFullScreenCanvasDialog(templateView: TemplateView) {
        val dialog = Dialog(requireContext(), R.style.full_screen_dialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setContentView(R.layout.full_screen_layout)
        dialog.setCancelable(true)

        val root = dialog.findViewById<LinearLayout>(R.id.linear_layout)

        val layoutParams = LinearLayout.LayoutParams(templateView.canvasOriginalWidth.toInt(), templateView.canvasOriginalHeight.toInt())
        binding.rootLayout.removeView(templateView)

        // TODO - А что если форма нестандартная?
        root.post {
            val factor = root.width / templateView.canvasOriginalWidth

            templateView.scaleX = factor - factor * 0.1F
            templateView.scaleY = factor - factor * 0.1F
        }

        layoutParams.gravity = Gravity.CENTER_HORIZONTAL
        root.addView(templateView, 0, layoutParams)

        root.setOnClickListener {
            root.removeView(templateView)
            addTemplateViewToPreview(templateView)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun getTemplateView(templateName: String, context: Context?): TemplateView = when(templateName) {
        "classic" -> ClassicV1TemplateView(context, null)
        "half" -> HalfV1TemplateView(context, null)

        else -> ClassicV1TemplateView(context, null)
    }

    private fun addTemplateViewToPreview(templateView: TemplateView) {
        templateView.scaleX = 1F
        templateView.scaleY = 1F

        templateView.id = View.generateViewId()
        val layoutParams = ConstraintLayout.LayoutParams(0, 0)
        binding.rootLayout.addView(templateView, 0, layoutParams)

        val set = ConstraintSet()
        set.clone(binding.rootLayout)
        set.connect(templateView.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
        set.connect(templateView.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
        set.connect(templateView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP,
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                8F,
                resources.displayMetrics
            ).toInt()
        )
        set.connect(templateView.id, ConstraintSet.BOTTOM, binding.recyclerControllers.id, ConstraintSet.TOP)
        set.applyTo(binding.rootLayout)
    }
}