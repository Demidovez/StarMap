package com.nikolaydemidovez.starmap.pages.projects

import android.content.Context
import androidx.lifecycle.*
import com.nikolaydemidovez.starmap.pages.templates.TemplatesViewModel
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.room.AppDatabase
import com.nikolaydemidovez.starmap.room.TemplateRepository
import kotlinx.coroutines.launch

class ProjectsViewModel(context: Context) : ViewModel() {
    private val repository: TemplateRepository
    val allTemplates: LiveData<List<Template>>

    init {
        val templateDao = AppDatabase.getDatabase(context, viewModelScope).templateDao()

        repository = TemplateRepository(templateDao)

        allTemplates = repository.allCustomTemplates
    }

    fun insert(template: Template) = viewModelScope.launch {
        repository.insert(template)
    }
}
