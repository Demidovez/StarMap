package com.kalambur.mappy_stars.pages.projects

import android.content.Context
import androidx.lifecycle.*
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.room.AppDatabase
import com.kalambur.mappy_stars.room.TemplateRepository
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
