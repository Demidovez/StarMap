package com.nikolaydemidovez.starmap.ui.template

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TemplateViewModelFactory(private val templateName: String): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TemplateViewModel(templateName) as T
    }
}