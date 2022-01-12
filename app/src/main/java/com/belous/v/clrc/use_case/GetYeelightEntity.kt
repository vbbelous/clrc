package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.data.db.entity.YeelightEntity
import kotlinx.coroutines.flow.Flow

class GetYeelightEntity(
    val yeelightDao: YeelightDao
) {

    operator fun invoke(id: Int): Flow<YeelightEntity> {
        return yeelightDao.getById(id)
    }
}