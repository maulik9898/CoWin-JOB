package com.cowin.useCase

import kotlinx.coroutines.*
import com.cowin.model.Subscribe
import com.cowin.repository.FirebaseRepo

class Firebase(private val firebaseRepo: FirebaseRepo) {

    suspend fun getAllSubscriber():MutableMap<String,MutableList<Subscribe>>{

        val result = mutableMapOf<String, MutableList<Subscribe>>()
        withContext(Dispatchers.IO){
            val response = firebaseRepo.getSubscriber()
            response.onSuccess { it ->
                it.forEach { doc ->
                    val subscribe = doc.toObject(Subscribe::class.java)
                    subscribe.id = doc.id
                    val currentTimestamp  = Math.floorDiv(System.currentTimeMillis() , 1000)
                    if (currentTimestamp > subscribe.lastTimestamp + subscribe.notificationTime) {
                        if (result[subscribe.districtId].isNullOrEmpty()) {
                            result[subscribe.districtId] = mutableListOf(subscribe)
                        } else {
                            result[subscribe.districtId]?.add(subscribe)
                        }
                    } else{
                        println("Skipped User :: ${subscribe.id}")
                    }
                }
            }
        }

        return result

    }

    suspend fun sendPushNotification(users: MutableMap<String, MutableList<Subscribe>>, centers:MutableMap<String,MutableSet<String>> ): Int {
        var counter = 0;
        withContext(Dispatchers.Default){
            val notifiedUser = mutableListOf<Subscribe>()
            users.keys.forEach{key ->
                run {
                        users[key]?.forEach { user ->
                            run {

                                    if (user.age=="0" && centers.containsKey(key)){
                                        notifiedUser.add(user)
                                    }else if (centers[key]?.contains(user.age) == true) {
                                        notifiedUser.add(user)
                                    }

                            }
                        }


                }
            }
            val runningTask  = notifiedUser.map { s  ->
                async(Dispatchers.IO) {
                    s to firebaseRepo.senPushNotification(s)

            } }

                val responses = runningTask.awaitAll()


            responses.forEach { (user, response) ->
                run {
                    response.onSuccess {
                        counter+=1
                        println("Notified User :: ${user.id}")
                    }
                    response.onFailure { println("Failure : ${user.id} , Error : ${it.message}")}

                }
            }

        }
this.updateCounter(counter)
return counter
    }

    suspend fun updateCounter(counter: Int){
        val data = firebaseRepo.upDateCounter(counter)
        data.onSuccess {
            println("Counter updated in database..")
         }

    }
}