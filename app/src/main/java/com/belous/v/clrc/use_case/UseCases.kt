package com.belous.v.clrc.use_case

data class UseCases(
    val findNewDevices: FindNewDevices,
    val paramsToEntity: ParamsToEntity,
    val insertYeelightEntity: InsertYeelightEntity,
    val getYeelightEntity: GetYeelightEntity,
    val getYeelightEntityList: GetYeelightEntityList,
    val deleteYeelightEntity: DeleteYeelightEntity,
    val entityToYeelight: EntityToYeelight,
    val getYeelightParams: GetYeelightParams,
    val setYeelightParams: SetYeelightParams,
    val updateYeelightEntity: UpdateYeelightEntity
)