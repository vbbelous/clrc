package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity

class GetYeelightEntity(
    val yeelightDao: YeelightDao
) {

    suspend operator fun invoke(id: Int): YeelightEntity {
        return yeelightDao.getById(id)
    }
}