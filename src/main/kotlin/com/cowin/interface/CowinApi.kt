package com.cowin.`interface`

import com.cowin.model.ResponseDAO

interface CowinApi {
    suspend fun getCalenderByPin(pin : Int, date : String) : ResponseDAO
    suspend fun getCalenderByDistrict(district_id:String,date: String) : ResponseDAO
}