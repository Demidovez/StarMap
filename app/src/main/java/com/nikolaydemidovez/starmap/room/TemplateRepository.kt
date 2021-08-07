package com.nikolaydemidovez.starmap.room

import androidx.lifecycle.LiveData
import com.nikolaydemidovez.starmap.pojo.Template

class TemplateRepository(private val templateDao: TemplateDao) {
    val allTemplates: LiveData<List<Template>> = templateDao.getAll()

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }
}