package com.nikolaydemidovez.starmap.pages.projects

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nikolaydemidovez.starmap.pages.templates.TemplatesViewModel

class ProjectsViewModelFactory(private val context: Context): ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProjectsViewModel(context) as T
    }
}