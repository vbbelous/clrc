package com.belous.v.clrc.core.util

import com.belous.v.clrc.core.domain.Yeelight

object YeelightBuilder {

    fun build(params: Map<String, String>): Yeelight {
        val yeelightParams = HashMap(params)
        val location = yeelightParams["Location"]
        val address = location?.substring(location.indexOf("://") + 3)?.split(":")
        address?.let {
            val ip = it[0]
            val port = it[1].toInt()
            val name = params["model"].toString() + " (" + ip + ")"
            return Yeelight(
                name = name,
                ip = ip,
                port = port,
                params = params
            )
        }
        throw IllegalStateException(this.javaClass.methods.last().name)
    }
}