package com.belous.v.clrc.core.domain

data class Yeelight(
    val id: Int,
    val name: String,
    val model: String,
    val serial: String,
    val ip: String,
    val port: Int,
    val bright: Int,
    val ct: Int,
    val isActive: Boolean,
    val isOnline: Boolean,
    val isPower: Boolean
)