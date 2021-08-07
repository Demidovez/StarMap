package com.nikolaydemidovez.starmap.pages.templates

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.TemplateAdapter
import com.nikolaydemidovez.starmap.controllers.stars_v1.StarsV1ControllerViewModel
import com.nikolaydemidovez.starmap.databinding.FragmentTemplatesBinding
import com.nikolaydemidovez.starmap.room.TemplateViewModel

class TemplatesFragment : Fragment() {

    private lateinit var templateViewModel: TemplateViewModel
    private lateinit var binding: FragmentTemplatesBinding
    private val adapter = TemplateAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()

        templateViewModel = ViewModelProvider(this).get(TemplateViewModel::class.java)

        binding = FragmentTemplatesBinding.inflate(inflater, container, false)

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
        val recyclerTemplates: RecyclerView = binding.recyclerTemplates

        recyclerTemplates.layoutManager = GridLayoutManager(this.context, 2)
        recyclerTemplates.adapter = adapter

        templateViewModel.allTemplates.observe(viewLifecycleOwner, {
            adapter.addAllTemplateList(it)
        })
    }
}