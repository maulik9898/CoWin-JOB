package com.cowin

import com.cowin.`interface`.CowinApiImpl
import com.cowin.model.Subscribe
import kotlinx.coroutines.runBlocking
import com.cowin.repository.CowinRepo
import com.cowin.repository.FirebaseRepo
import com.cowin.useCase.CowinRequest
import com.cowin.useCase.Firebase
import kotlinx.coroutines.delay
import java.util.*
import java.util.function.Function
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.math.ceil
import kotlin.system.measureTimeMillis
import java.util.stream.Collectors

import java.util.stream.Collector
import kotlin.math.floor


fun main(args: Array<String>) {
    println()
    println("COWID-JOB STARTED :: ${Date().toString()}" )
    println()
    timer("mainJob",false,5000,5000){
        doWork()
    }


}

fun doWork() {
    println()
    println("---------------------------------------------------------------------")
    println("                   Job started :: ${Date().toString()}")
    println()
    val firebaseRepo = FirebaseRepo()
    val firebase = Firebase(firebaseRepo)
    val time = measureTimeMillis {
        runBlocking {
            val first = firebase.getAllSubscriber()
            val batches = getBaches(first,99)
            var no = 1
            batches.forEach {
                println("###########  Batch $no Start :: ${Date().toString()} ##########")
                runJobInBatch(it , firebase)
                delay(300000)
                println("###########  Batch $no Start :: ${Date().toString()} ##########")
                no+=1
            }


        }
    }
    println()
    println("Completed in $time ms")
    println()
    println("                   Job Completed :: ${Date().toString()}")
    println("---------------------------------------------------------------------")
    println()


}

fun getBaches(data: MutableMap<String, MutableList<Subscribe>> , count:Int): List<MutableMap<String, MutableList<Subscribe>>> {
    val len = data.keys.size;
    var bachNo = (len/count).toInt() + 1
    println("### Total district record : $len  Dividing into $bachNo  batch  ")
    var baches: MutableList<MutableMap<String, MutableList<Subscribe>>> =  mutableListOf();
    var counter = 0;
    for ( j in 1..bachNo.toInt()){
        val temp : MutableMap<String, MutableList<Subscribe>> = mutableMapOf();
        baches.add(temp)
    }
    var i = 0
    for (e in data.entries){
        baches[i][e.key] = e.value
        counter += 1
        if(counter >= count){
            counter = 0
            i+=1
        }
    }

    return baches
}



suspend fun runJobInBatch(first:MutableMap<String, MutableList<Subscribe>> , firebase: Firebase){
    val cowinRequest = CowinRequest(CowinRepo(CowinApiImpl()))
    val apiData = cowinRequest.invoke(first.keys.toList())
    val resultMap = mutableMapOf<String,MutableSet<String>>()
    apiData.keys.forEach { s ->
        run {
            apiData[s]?.centers?.forEach { c -> run {
                c.sessions.forEach{session -> run {
                    if (session.min_age_limit == 18 && session.available_capacity > 0){
                        val set:MutableSet<String> = (resultMap.getOrDefault(s,
                            mutableSetOf<String>()) union mutableListOf("18")) as MutableSet<String>
                        resultMap[s] = set
                    }else if (session.min_age_limit == 45  && session.available_capacity > 0){
                        val set:MutableSet<String> = (resultMap.getOrDefault(s,
                            mutableSetOf<String>()) union mutableListOf("45")) as MutableSet<String>
                        resultMap[s] = set
                    }
                }}
            }  }
        }
    }
    val count =  firebase.sendPushNotification(first,resultMap)
    println()
    println("Total user notified : $count")
    println()
}
