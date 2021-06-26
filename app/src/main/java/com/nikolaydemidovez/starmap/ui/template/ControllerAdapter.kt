package com.nikolaydemidovez.starmap.ui.template

import android.content.Context
import android.content.res.Resources
import android.hardware.display.DisplayManager
import android.util.DisplayMetrics
import android.view.Display
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat.getDisplay
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.findFragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.ControllerItemBinding
import com.nikolaydemidovez.starmap.databinding.TemplateItemBinding
import com.nikolaydemidovez.starmap.ui.template.controllers.canvas.CanvasV1ControllerFragment
import com.nikolaydemidovez.starmap.ui.template.controllers.event.EventV1ControllerFragment
import com.nikolaydemidovez.starmap.ui.templates.Template
import com.nikolaydemidovez.starmap.ui.templates.TemplateAdapter
import com.squareup.picasso.Picasso

class ControllerAdapter(private val childFragmentManager: FragmentManager): RecyclerView.Adapter<ControllerAdapter.ControllerHolder>() {
    private var controllerList = listOf<Controller>()
    private var screenWidth = 0

    class ControllerHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ControllerItemBinding.bind(item)

        fun bind(controller: Controller, childFragmentManager: FragmentManager, screenWidth: Int) = with(binding) {
            labelController.text = controller.title

            val itemWidth = (screenWidth / 5.5).toInt()

            val lp = parentView.layoutParams
            lp.height = lp.height
            lp.width = itemWidth
            parentView.layoutParams = lp

            Picasso.get().load(controller.image).into(imageController)

            cardView.setOnClickListener {
                val fragment = getControllerFragment(controller.name!!)

                val ft = childFragmentManager.beginTransaction()
                ft.replace(R.id.controller_wrapper, fragment, fragment.javaClass.name)
                ft.commitAllowingStateLoss()
            }
        }

        private fun getControllerFragment(name: String): Fragment = when(name) {
            "event_v1" -> EventV1ControllerFragment()
            "canvas_v1" -> CanvasV1ControllerFragment()

            else -> EventV1ControllerFragment()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_item, parent, false)

        screenWidth = Resources.getSystem().displayMetrics.widthPixels

        return ControllerHolder(view)
    }

    override fun onBindViewHolder(holder: ControllerHolder, position: Int) {
        holder.bind(controllerList[position], childFragmentManager, screenWidth)
    }

    override fun getItemCount(): Int {
        return controllerList.size
    }

    fun addAllControllerList(list: List<Controller>) {
        controllerList = list
        notifyDataSetChanged()
    }

}