package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.entity.YeelightEntity
import com.belous.v.clrc.domain.Yeelight
import com.belous.v.clrc.domain.YeelightParams

class EntityToYeelight {

    operator fun invoke(yeelightEntity: YeelightEntity): Yeelight {
        val params = yeelightEntity.params
        val isActive = params[YeelightParams.ACTIVE_MODE] == "1"
        return Yeelight(
            id = yeelightEntity.id,
            name = yeelightEntity.name,
            model = yeelightEntity.model,
            serial = yeelightEntity.serial,
            ip = yeelightEntity.ip,
            port = yeelightEntity.port,
            bright = (if (isActive) params[YeelightParams.NL_BR]
            else params[YeelightParams.BRIGHT])?.toIntOrNull() ?: 0,
            ct = params[YeelightParams.CT]?.toIntOrNull() ?: 0,
            isActive = isActive,
            isOnline = params[YeelightParams.ONLINE]?.isNotEmpty() ?: true,
            isPower = params[YeelightParams.POWER] == "on"
        )
    }
}