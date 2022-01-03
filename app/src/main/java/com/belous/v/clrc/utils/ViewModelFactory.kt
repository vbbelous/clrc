package com.belous.v.clrc.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlin.reflect.full.primaryConstructor

class ViewModelFactory(private vararg val args: Any) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.kotlin.primaryConstructor?.call(*args)
            ?: throw IllegalArgumentException("$modelClass primaryConstructor is null")
    }
}