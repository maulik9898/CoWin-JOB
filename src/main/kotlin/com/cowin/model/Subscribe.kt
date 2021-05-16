package com.cowin.model

import kotlinx.serialization.Serializable

@Serializable
data class Subscribe(
    val age:String="",
    val districtId:String = "",
    val districtName:String = "",
    val stateId:String = "",
    val stateName:String = "",
    val fcmId:String ="",
    var id:String = "",
    var notificationTime: Int = 0,
    var lastTimestamp: Long = 0
) {
}