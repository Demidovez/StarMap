package com.nikolaydemidovez.starmap.pages.templates

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikolaydemidovez.starmap.pages.template.TemplateViewModel

class TemplatesViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TemplatesViewModel(context) as T
    }
}