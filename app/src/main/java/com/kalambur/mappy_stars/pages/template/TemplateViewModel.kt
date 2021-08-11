package com.kalambur.mappy_stars.pages.template

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.room.AppDatabase
import com.kalambur.mappy_stars.room.TemplateRepository
import kotlinx.coroutines.launch

class TemplateViewModel(context: Context, templateId: Int) : ViewModel() {
    private val repository: TemplateRepository
    val template: LiveData<Template>

    init {
        val templateDao = AppDatabase.getDatabase(context, viewModelScope).templateDao()

        repository = TemplateRepository(templateDao)

        template = repository.getTemplate(templateId)
    }

    fun insert(template: Template) = viewModelScope.launch {
        repository.insert(template)
    }



}