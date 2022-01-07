package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity

class GetYeelightEntityList(
    val yeelightDao: YeelightDao
) {

    suspend operator fun invoke(): List<YeelightEntity> {
        return yeelightDao.getAll()
    }
}