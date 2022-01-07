package com.belous.v.clrc.use_case

import com.belous.v.clrc.data.db.YeelightDao

class DeleteYeelightEntity(
    val yeelightDao: YeelightDao
) {

    suspend operator fun invoke(id: Int) {
        return yeelightDao.deleteById(id)
    }
}