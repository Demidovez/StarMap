package com.nikolaydemidovez.starmap.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.nikolaydemidovez.starmap.pojo.Template


@Dao
interface TemplateDao {
    @Query("SELECT * FROM template")
    fun getAll(): LiveData<List<Template>>

    @Query("SELECT * FROM template WHERE id = :id")
    fun getById(id: Long): LiveData<Template>

    @Insert
    suspend fun insert(template: Template)

    @Insert
    suspend fun insertDefault(templates: List<Template>)

    @Update
    suspend fun update(template: Template)

    @Delete
    suspend fun delete(template: Template)

    @Query("DELETE FROM template")
    suspend fun deleteAll()
}