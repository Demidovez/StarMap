package com.nikolaydemidovez.starmap.pages.template

import android.content.Context
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

class TemplateFragment : Fragment() {

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplateBinding
    private lateinit var adapter: ControllerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val templateName = arguments?.getString("templateName")
        val templateTitle = arguments?.getString("templateTitle")

        templateViewModel = ViewModelProviders
            .of(this, TemplateViewModelFactory(templateName!!))
            .get(TemplateViewModel::class.java)

        binding = FragmentTemplateBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val templateView = getTemplateView(templateName, this.context)
        
        adapter = ControllerAdapter(childFragmentManager, templateView)

        if (activity != null) {
            (activity as MainActivity).supportActionBar?.title = templateTitle
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

    private fun getTemplateView(templateName: String, context: Context?): TemplateView {
        val templateView = when(templateName) {
            "classic" -> ClassicV1TemplateView(context, null)
            "half" -> HalfV1TemplateView(context, null)

            else -> ClassicV1TemplateView(context, null)
        }

        templateView.id = View.generateViewId()
        val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, 0)
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

        return  templateView
    }
}