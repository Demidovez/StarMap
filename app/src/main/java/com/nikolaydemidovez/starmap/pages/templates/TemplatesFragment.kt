package com.nikolaydemidovez.starmap.pages.templates

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nikolaydemidovez.starmap.MainActivity
import com.nikolaydemidovez.starmap.R
import com.nikolaydemidovez.starmap.adapters.TemplateAdapter
import com.nikolaydemidovez.starmap.databinding.FragmentTemplatesBinding
import com.nikolaydemidovez.starmap.pojo.ShapeMapBorder.Companion.NONE
import com.nikolaydemidovez.starmap.pojo.ShapeSeparator
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.pojo.TemplateProperties
import com.nikolaydemidovez.starmap.templates.TemplateCanvas
import java.util.*

class TemplatesFragment : Fragment() {

    private lateinit var templatesViewModel: TemplatesViewModel
    private lateinit var binding: FragmentTemplatesBinding
    private val adapter = TemplateAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        editActionAndStatusBar()
        templatesViewModel = ViewModelProvider(this).get(TemplatesViewModel::class.java)

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

        val templateList = arrayListOf(
            Template(null, "classic_v1", "Классика", "http://62.75.195.219:3000/images/templates/classic.jpeg"),
            Template(null, "half_v1", "Полусфера", "http://62.75.195.219:3000/images/templates/half.jpeg"),
            Template(null, "polaroid_v1", "Полароид", "http://62.75.195.219:3000/images/templates/polaroid.jpeg"),
            Template(null, "full_v1", "Полная", "http://62.75.195.219:3000/images/templates/full.jpeg"),
            Template(null, "starworld_v1", "Звездный мир", "http://62.75.195.219:3000/images/templates/starworld.jpeg"),
            Template(null, "moon_v1", "Луна", "http://62.75.195.219:3000/images/templates/moon.png"),
        )

        adapter.addAllTemplateList(templateList)
    }
}