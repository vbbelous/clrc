package com.belous.v.clrc.utils

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children

fun ViewGroup.findChildren(): List<View> {
    val viewList = ArrayList<View>()
    children.forEach { child ->
        if (child is ViewGroup) {
            viewList.addAll(child.findChildren())
        } else {
            if (child.tag == tag) {
                viewList.add(child)
            }
        }
    }
    return viewList
}