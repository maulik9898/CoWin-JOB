package com.cowin.`interface`

import com.cowin.`interface`.CowinApi
import com.cowin.constants.ByDistrict
import com.cowin.constants.ByPin
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import com.cowin.model.ResponseDAO


private const val TIME_OUT = 60_000

class CowinApiImpl : CowinApi {
    private val client = HttpClient(CIO){

        install(JsonFeature) {
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json {
                prettyPrint = true
                isLenient = true
            })
        }


        install(DefaultRequest) {
            header(HttpHeaders.ContentType, ContentType.Application.Json)

        }
    }

    override suspend fun getCalenderByPin(pin: Int, date: String) : ResponseDAO =
        client.get<ResponseDAO>(ByPin +"pincode=$pin&date=$date")


    override suspend fun getCalenderByDistrict(district_id: String, date: String) : ResponseDAO =
        client.get<ResponseDAO>(ByDistrict +"district_id=$district_id&date=$date")

}

