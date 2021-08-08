package com.nikolaydemidovez.starmap.pages.templates

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.TemplateAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentTemplatesBinding

class TemplatesFragment : Fragment() {
    private lateinit var templatesViewModel: TemplatesViewModel
    private lateinit var binding: FragmentTemplatesBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()

        templatesViewModel = ViewModelProviders.of(this, TemplatesViewModelFactory(requireContext())).get(TemplatesViewModel::class.java)

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
        val adapter = TemplateAdapter()

        val recyclerTemplates: RecyclerView = binding.recyclerTemplates

        recyclerTemplates.layoutManager = GridLayoutManager(this.context, 2)
        recyclerTemplates.adapter = adapter

        templatesViewModel.allTemplates.observe(viewLifecycleOwner, {
            adapter.addAllTemplateList(it)
        })
    }
}