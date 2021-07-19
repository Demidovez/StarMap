package com.nikolaydemidovez.starmap.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.TemplateItemBinding
import com.nikolaydemidovez.starmap.pojo.Template
import com.squareup.picasso.Picasso

class TemplateAdapter: RecyclerView.Adapter<TemplateAdapter.TemplateHolder>() {
    private var templateList = listOf<Template>()

    class TemplateHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = TemplateItemBinding.bind(item)

        fun bind(template: Template) = with(binding) {
            labelTemplate.text = template.title

            cardView.setOnClickListener { view ->
                val bundle = bundleOf(
                    "templateName" to template.name,
                    "templateTitle" to template.title
                )

                view.findNavController().navigate(R.id.action_navigation_templates_to_templateFragment, bundle)
            }

            Picasso.get().load(template.image).into(imageTemplate)
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

    fun addAllTemplateList(list: List<Template>) {
        templateList = list
        notifyDataSetChanged()
    }

}