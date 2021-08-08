package com.nikolaydemidovez.starmap.adapters

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.updatePadding
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.databinding.ProjectItemBinding
import com.nikolaydemidovez.starmap.databinding.TemplateItemBinding
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.lang.Exception

class ProjectAdapter: RecyclerView.Adapter<ProjectAdapter.ProjectHolder>() {
    private var projectList = listOf<Template?>()

    class ProjectHolder(item: View): RecyclerView.ViewHolder(item) {
        private val binding = ProjectItemBinding.bind(item)

        fun bind(template: Template?) = with(binding) {
            if(adapterPosition == 0) {
                rootItem.updatePadding(top = Helper.dpToPx(24F, itemView.context))
            } else {
                rootItem.updatePadding(top = 0)
            }

            projectName.text = if(template!!.title!!.isNotEmpty()) template.title else template.category
            projectDesc.text = template.descText
            projectLocation.text = template.resultLocationText

            cardView.setOnClickListener { view ->
                val bundle = bundleOf(
                    "templateId" to template.id
                )

                view.findNavController().navigate(R.id.action_navigation_projects_to_templateFragment, bundle)
            }

            Picasso.get().load(template.image).into(object : com.squareup.picasso.Target {
                override fun onBitmapLoaded(bitmap: Bitmap?, from: LoadedFrom?) {
                    imageProject.setImageBitmap(bitmap)
                }

                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.project_item, parent, false)
        return ProjectHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectHolder, position: Int) {
        holder.bind(projectList[position])
    }

    override fun getItemCount(): Int {
        return projectList.size
    }

    fun addAllTemplateList(list: List<Template?>) {
        projectList = list
        notifyDataSetChanged()
    }

}