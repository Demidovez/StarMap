package com.kalambur.mappy_stars.room

import androidx.lifecycle.LiveData
import com.kalambur.mappy_stars.pojo.Template

class TemplateRepository(private val templateDao: TemplateDao) {
    val allDefaultTemplates: LiveData<List<Template>> = templateDao.getAllDefault()
    val allCustomTemplates: LiveData<List<Template>> = templateDao.getAllCustom()

    fun getTemplate(templateId: Int): LiveData<Template> {
        return templateDao.getById(templateId)
    }

    suspend fun insert(template: Template) {
        templateDao.insert(template)
    }

    suspend fun update(template: Template) {
        templateDao.update(template)
    }

    suspend fun deleteById(templateId: Int) {
        templateDao.deleteById(templateId)
    }
}