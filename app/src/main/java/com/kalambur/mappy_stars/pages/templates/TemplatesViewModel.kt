package com.kalambur.mappy_stars.pages.templates

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.kalambur.mappy_stars.adapters.TemplateAdapter
import com.kalambur.mappy_stars.pojo.Template
import com.kalambur.mappy_stars.room.AppDatabase
import com.kalambur.mappy_stars.room.TemplateRepository
import kotlinx.coroutines.launch

class TemplatesViewModel(context: Context) : ViewModel() {
    private val repository: TemplateRepository
    val allTemplates: LiveData<List<Template>>

    init {
        val templateDao = AppDatabase.getDatabase(context, viewModelScope).templateDao()

        repository = TemplateRepository(templateDao)

        allTemplates = repository.allDefaultTemplates
    }

    fun insert(template: Template) = viewModelScope.launch {
        repository.insert(template)
    }
}
