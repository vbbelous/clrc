package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity

class InsertYeelightEntity(val yeelightDao: YeelightDao) {

    suspend operator fun invoke(
        yeelightEntity: YeelightEntity
    ) {
        yeelightDao.insert(yeelightEntity)
    }
}