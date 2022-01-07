package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity

class UpdateYeelightEntity(val yeelightDao: YeelightDao) {

    suspend operator fun invoke(
        yeelightEntity: YeelightEntity,
        params: Map<String, String> = emptyMap()
    ) {
        yeelightDao.update(yeelightEntity.copy(params = yeelightEntity.params.plus(params)))
    }
}