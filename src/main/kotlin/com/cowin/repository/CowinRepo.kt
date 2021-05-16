package com.cowin.repository

import com.cowin.`interface`.CowinApiImpl
import com.cowin.model.ResponseDAO

class CowinRepo(private val cowinApiImpl: CowinApiImpl) {

    suspend fun getByPincode(pin:Int,date:String) : Result<ResponseDAO> = kotlin.runCatching {
        cowinApiImpl.getCalenderByPin(pin, date)
    }

    suspend fun getByDistrict(district:String,date:String) : Result<ResponseDAO> = kotlin.runCatching {
        cowinApiImpl.getCalenderByDistrict(district, date)
    }

}