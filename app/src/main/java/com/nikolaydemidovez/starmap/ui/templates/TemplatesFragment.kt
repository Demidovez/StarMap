package com.nikolaydemidovez.starmap.ui.templates

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.databinding.FragmentTemplatesBinding

class TemplatesFragment : Fragment() {

    private lateinit var templatesViewModel: TemplatesViewModel
    private lateinit var binding: FragmentTemplatesBinding
    private val adapter = TemplateAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        templatesViewModel = ViewModelProvider(this).get(TemplatesViewModel::class.java)

        binding = FragmentTemplatesBinding.inflate(inflater, container, false)

        val root: View = binding.root

        recyclerInit()

        return root
    }

    private fun recyclerInit() {
        val recyclerTemplates: RecyclerView = binding.recyclerTemplates

        recyclerTemplates.layoutManager = GridLayoutManager(this.context, 2)
        recyclerTemplates.adapter = adapter

        templatesViewModel.templateList.observe(viewLifecycleOwner, {
            adapter.addAllTemplateList(it)
        })
    }
}