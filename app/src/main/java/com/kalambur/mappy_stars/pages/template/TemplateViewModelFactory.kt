package com.kalambur.mappy_stars.pages.template

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class TemplateViewModelFactory(private val context: Context, private val templateId: Int): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TemplateViewModel(context, templateId) as T
    }
}