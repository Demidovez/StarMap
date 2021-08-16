package com.kalambur.mappy_stars.pages.templates

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kalambur.mappy_stars.MainActivity
import com.kalambur.mappy_stars.R
import com.kalambur.mappy_stars.adapters.TemplateAdapter
import com.kalambur.mappy_stars.databinding.FragmentTemplatesBinding
import com.kalambur.mappy_stars.utils.admob.AdmobUtil
import com.google.android.gms.ads.formats.UnifiedNativeAd

import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener
import com.google.android.gms.ads.nativead.NativeAdOptions
import com.kalambur.mappy_stars.pojo.Template
import java.util.ArrayList


class TemplatesFragment : Fragment() {
    private lateinit var templatesViewModel: TemplatesViewModel
    private lateinit var binding: FragmentTemplatesBinding
    private lateinit var recyclerTemplates: RecyclerView

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

        val adapter = TemplateAdapter(requireActivity())

        recyclerTemplates = binding.recyclerTemplates

        recyclerTemplates.layoutManager = GridLayoutManager(this.context, 1)
        recyclerTemplates.layoutManager?.onRestoreInstanceState(templatesViewModel.recyclerState)
        recyclerTemplates.adapter = adapter

        templatesViewModel.allTemplates.observe(viewLifecycleOwner, {
            val arrayList = ArrayList<Any?>()
            arrayList.addAll(it)
            adapter.addAllTemplateList(arrayList)
        })
    }

    override fun onStop() {
        super.onStop()
        Log.d("MyLog", "onStop")
        templatesViewModel.recyclerState = recyclerTemplates.layoutManager?.onSaveInstanceState()
        Log.d("MyLog", templatesViewModel.recyclerState.toString())
    }
}