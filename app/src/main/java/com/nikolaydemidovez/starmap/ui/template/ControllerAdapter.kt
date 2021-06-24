package com.nikolaydemidovez.starmap.ui.template

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.ControllerItemBinding
import com.nikolaydemidovez.starmap.databinding.TemplateItemBinding
import com.nikolaydemidovez.starmap.ui.templates.Template
import com.nikolaydemidovez.starmap.ui.templates.TemplateAdapter
import com.squareup.picasso.Picasso

class ControllerAdapter: RecyclerView.Adapter<ControllerAdapter.ControllerHolder>() {
    private var controllerList = listOf<Controller>()

    class ControllerHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ControllerItemBinding.bind(item)

        fun bind(controller: Controller) = with(binding) {
            labelController.text = controller.title

            Picasso.get().load(controller.image).into(imageController)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ControllerHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.controller_item, parent, false)
        return ControllerHolder(view)
    }

    override fun onBindViewHolder(holder: ControllerHolder, position: Int) {
        holder.bind(controllerList[position])
    }

    override fun getItemCount(): Int {
        return controllerList.size
    }

    fun addAllControllerList(list: List<Controller>) {
        controllerList = list
        notifyDataSetChanged()
    }

}