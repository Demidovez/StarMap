package com.nikolaydemidovez.starmap.utils.extensions

import androidx.annotation.NonNull
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

fun <T : Any?> MutableLiveData<T>.observe(owner: LifecycleOwner, isCanRun: MutableLiveData<Boolean>, observer: Observer<T>) = apply {
    if(isCanRun.value!!) {
        observe(owner, observer)
    }
} // TODO: Delete