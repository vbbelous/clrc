package com.belous.v.clrc.core.util

import com.belous.v.clrc.core.data.db.entity.YeelightEntity
import com.belous.v.clrc.core.domain.Yeelight
import com.belous.v.clrc.core.domain.YeelightParams

object YeelightConvert {

    operator fun invoke(yeelightEntity: YeelightEntity): Yeelight {

        val params = yeelightEntity.params
        val isActive = params[YeelightParams.ACTIVE_MODE] == "1"
        return Yeelight(
            id = yeelightEntity.id ?: 0,
            name = yeelightEntity.name,
            model = yeelightEntity.model,
            serial = yeelightEntity.serial,
            ip = yeelightEntity.ip,
            port = yeelightEntity.port,
            bright = (if (isActive) params[YeelightParams.NL_BR]
            else params[YeelightParams.BRIGHT])?.toInt() ?: 0,
            ct = params[YeelightParams.CT]?.toInt() ?: 0,
            isActive = isActive,
            isOnline = true,
            isPower = params[YeelightParams.POWER] == "on"
        )
    }
}