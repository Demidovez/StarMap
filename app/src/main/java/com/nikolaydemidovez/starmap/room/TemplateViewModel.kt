package com.nikolaydemidovez.starmap.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.nikolaydemidovez.starmap.pojo.Template
import kotlinx.coroutines.launch

class TemplateViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TemplateRepository
    val allTemplates: LiveData<List<Template>>

    init {
        val templateDao = AppDatabase.getDatabase(application, viewModelScope).templateDao()

        repository = TemplateRepository(templateDao)

        allTemplates = repository.allTemplates
    }

    fun insert(template: Template) = viewModelScope.launch {
        repository.insert(template)
    }
}
