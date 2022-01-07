package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.entity.YeelightEntity

class ParamsToEntity {

    operator fun invoke(params: Map<String, String>): YeelightEntity {
        val location = params["Location"]
        val address = location?.substring(location.indexOf("://") + 3)?.split(":")
        address?.let {
            val ip = it[0]
            val port = it[1].toInt()
            val model = params["model"].orEmpty()
            val name = "$model ($ip)"
            val serial = params["id"].orEmpty()
            return YeelightEntity(
                name = name,
                model = model,
                serial = serial,
                ip = ip,
                port = port,
                params = params
            )
        }
        throw IllegalStateException(this.javaClass.simpleName)
    }
}