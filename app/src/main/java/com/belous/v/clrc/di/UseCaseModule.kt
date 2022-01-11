package com.belous.v.clrc.di

import com.belous.v.clrc.data.db.YeelightDao
import com.belous.v.clrc.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    fun provideYeelightUseCases(yeelightDao: YeelightDao): UseCases {
        return UseCases(
            findNewDevices = FindNewDevices(),
            insertYeelightEntity = InsertYeelightEntity(yeelightDao),
            paramsToEntity = ParamsToEntity(),
            getYeelightEntityList = GetYeelightEntityList(yeelightDao),
            getYeelightEntity = GetYeelightEntity(yeelightDao),
            deleteYeelightEntity = DeleteYeelightEntity(yeelightDao),
            entityToYeelight = EntityToYeelight(),
            getYeelightParams = GetYeelightParams(),
            setYeelightParams = SetYeelightParams(),
            updateYeelightEntity = UpdateYeelightEntity(yeelightDao)
        )
    }
}