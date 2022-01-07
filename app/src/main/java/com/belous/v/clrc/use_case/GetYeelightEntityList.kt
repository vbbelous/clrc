package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity
import kotlinx.coroutines.flow.Flow

class GetYeelightEntityList(
    val yeelightDao: YeelightDao
) {

    operator fun invoke(): Flow<List<YeelightEntity>> {
        return yeelightDao.getAll()
    }
}