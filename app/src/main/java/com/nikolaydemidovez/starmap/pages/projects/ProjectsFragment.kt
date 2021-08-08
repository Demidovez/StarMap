package com.nikolaydemidovez.starmap.pages.projects

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.ProjectAdapter
import com.nikolaydemidovez.starmap.adapters.TemplateAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentProjectsBinding
import com.nikolaydemidovez.starmap.pages.templates.TemplatesViewModel
import com.nikolaydemidovez.starmap.pages.templates.TemplatesViewModelFactory

class ProjectsFragment : Fragment() {
    private lateinit var projectsViewModel: ProjectsViewModel
    private lateinit var binding: FragmentProjectsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()

        projectsViewModel = ViewModelProviders.of(this, ProjectsViewModelFactory(requireContext())).get(
            ProjectsViewModel::class.java)

        binding = FragmentProjectsBinding.inflate(inflater, container, false)

        val root: View = binding.root

        recyclerInit()

        return root
    }

    private fun editActionAndStatusBar() {
        if (activity != null) {
            (activity as MainActivity).supportActionBar?.show()
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window: Window = requireActivity().window
            window.statusBarColor = ContextCompat.getColor(requireActivity(), R.color.white)
        }
    }

    private fun recyclerInit() {
        val adapter = ProjectAdapter()

        val recyclerTemplates: RecyclerView = binding.recyclerTemplates

        recyclerTemplates.layoutManager = GridLayoutManager(this.context, 1)
        recyclerTemplates.adapter = adapter

        projectsViewModel.allTemplates.observe(viewLifecycleOwner, {
            binding.notFoundProjects.visibility = if(it.isNotEmpty()) GONE else VISIBLE

            adapter.addAllTemplateList(it)
        })
    }
}