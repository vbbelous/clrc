package com.belous.v.clrc.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Yeelight")
data class YeelightEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int? = null,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "serial") val serial: String,
    @ColumnInfo(name = "ip") val ip: String,
    @ColumnInfo(name = "port") val port: Int,
    @ColumnInfo(name = "params") val params: Map<String, String>
)