package com.nikolaydemidovez.starmap.pages.templates

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nikolaydemidovez.starmap.pojo.Template
import com.nikolaydemidovez.starmap.room.AppDatabase
import kotlinx.coroutines.launch

class TemplatesViewModel(context: Context) : ViewModel() {

    private val _templateList = MutableLiveData<List<Template?>>().apply {
//        val db: AppDatabase = AppDatabase.getInstance(context)
//
//        viewModelScope.launch {
//            value = db.templateDao()!!.getAll()
//        }


    }

    val templateList: LiveData<List<Template?>> = _templateList
}

