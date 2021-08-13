package com.kalambur.mappy_stars.adapters

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kalambur.mappy_stars.databinding.TemplateItemBinding
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.utils.extensions.resIdByName
import com.kalambur.mappy_stars.utils.helpers.Helper


class TemplateAdapter: RecyclerView.Adapter<TemplateAdapter.TemplateHolder>() {
    private var templateList = listOf<Template?>()

    class TemplateHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TemplateItemBinding.bind(item)

        fun bind(template: Template?) = with(binding) {
            if(adapterPosition in 0..1) {
                rootItem.updatePadding(top = Helper.dpToPx(24F, itemView.context))
            } else {
                rootItem.updatePadding(top = 0)
            }

            labelTemplate.text = template!!.category

            cardView.setOnClickListener { view ->
                val bundle = bundleOf(
                    "templateId" to template.id
                )

                view.findNavController().navigate(R.id.action_navigation_templates_to_templateFragment, bundle)
            }

            val imageStream = itemView.context.resources.openRawResource(itemView.context.resIdByName(template.image, "raw"))
            val bitmap = BitmapFactory.decodeStream(imageStream)

            imageTemplate.setImageBitmap(bitmap)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_item, parent, false)
        return TemplateHolder(view)
    }

    override fun onBindViewHolder(holder: TemplateHolder, position: Int) {
        holder.bind(templateList[position])
    }

    override fun getItemCount(): Int {
        return templateList.size
    }

    fun addAllTemplateList(list: List<Template?>) {
        templateList = list
        notifyDataSetChanged()
    }

}