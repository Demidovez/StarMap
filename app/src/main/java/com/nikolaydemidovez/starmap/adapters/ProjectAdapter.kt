package com.nikolaydemidovez.starmap.adapters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.nikolaydemidovez.starmap.databinding.ProjectItemBinding
import com.nikolaydemidovez.starmap.databinding.TemplateItemBinding
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.utils.extensions.resIdByName
import com.nikolaydemidovez.starmap.utils.helpers.Helper
import com.squareup.picasso.Picasso
import com.squareup.picasso.Picasso.LoadedFrom
import java.lang.Exception
import android.R
import android.R.attr

import android.R.attr.path
import android.content.Context
import android.widget.ImageView
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


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

                view.findNavController().navigate(com.nikolaydemidovez.starmap.R.id.action_navigation_projects_to_templateFragment, bundle)
            }

            try {
                val f = File(itemView.context.applicationContext.filesDir.path, template.image!!)
                val b = BitmapFactory.decodeStream(FileInputStream(f))
                imageProject.setImageBitmap(b)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.nikolaydemidovez.starmap.R.layout.project_item, parent, false)
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