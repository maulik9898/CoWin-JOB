package com.cowin.model

import kotlinx.serialization.Serializable


@Serializable
data class FCM (
    private val notification: Notify
        ){
}


@Serializable
data class Notify(
    private val title:String,
    private val body:String,
    private val tag:String,
    private val icon:String,
    private val data:MutableMap<String,String>

){

}