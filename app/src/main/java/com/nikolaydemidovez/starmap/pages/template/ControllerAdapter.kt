package com.nikolaydemidovez.starmap.pages.template

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.ControllerItemBinding
import com.nikolaydemidovez.starmap.controllers.canvas_v1.CanvasV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.desc_v1.DescV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.event_v1.EventV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.location_v1.LocationV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.map_v1.MapV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.save_v1.SaveV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.separator_v1.SeparatorV1ControllerFragment
import com.nikolaydemidovez.starmap.controllers.stars_v1.StarsV1ControllerFragment
import com.nikolaydemidovez.starmap.templates.TemplateView
import com.squareup.picasso.Picasso

class ControllerAdapter(private val childFragmentManager: FragmentManager, private val templateView: TemplateView): RecyclerView.Adapter<ControllerAdapter.ControllerHolder>() {
    private var controllerList = listOf<Controller>()
    private var screenWidth = 0

    class ControllerHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ControllerItemBinding.bind(item)

        fun bind(controller: Controller, childFragmentManager: FragmentManager, screenWidth: Int, templateView: TemplateView) = with(binding) {
            labelController.text = controller.title

            val itemWidth = (screenWidth / 5.5).toInt()

            val lp = parentView.layoutParams
            lp.height = lp.height
            lp.width = itemWidth
            parentView.layoutParams = lp

            Picasso.get().load(controller.image).into(imageController)

            cardView.setOnClickListener {
                val fragment = getControllerFragment(controller.name!!, templateView)

                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.controller_wrapper, fragment, fragment.javaClass.name)
                ft.commitAllowingStateLoss()
            }
        }

        private fun getControllerFragment(name: String, templateView: TemplateView): Fragment = when(name) {
            "event_v1" -> EventV1ControllerFragment(templateView)
            "canvas_v1" -> CanvasV1ControllerFragment(templateView)
            "map_v1" -> MapV1ControllerFragment(templateView)
            "stars_v1" -> StarsV1ControllerFragment(templateView)
            "desc_v1" -> DescV1ControllerFragment(templateView)
            "separator_v1" -> SeparatorV1ControllerFragment(templateView)
            "location_v1" -> LocationV1ControllerFragment(templateView)
            "save_v1" -> SaveV1ControllerFragment(templateView)

            else -> EventV1ControllerFragment(templateView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_item, parent, false)

        screenWidth = Resources.getSystem().displayMetrics.widthPixels

        return ControllerHolder(view)
    }

    override fun onBindViewHolder(holder: ControllerHolder, position: Int) {
        holder.bind(controllerList[position], childFragmentManager, screenWidth, templateView)
    }

    override fun getItemCount(): Int {
        return controllerList.size
    }

    fun addAllControllerList(list: List<Controller>) {
        controllerList = list
        notifyDataSetChanged()
    }

}