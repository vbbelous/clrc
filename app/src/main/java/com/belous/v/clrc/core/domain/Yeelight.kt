package com.belous.v.clrc.core.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Yeelight(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "port") val port: Int,
    @ColumnInfo(name = "params") val params: Map<String, String>,

//    @ColumnInfo(name = "isPower") val isPower: Boolean,
//    @ColumnInfo(name = "isOnline") val isOnline: Boolean,
//    @ColumnInfo(name = "isActive") val isActive: Boolean,
//    @ColumnInfo(name = "bright") val bright: Int,
//    @ColumnInfo(name = "ct") val ct: String
)