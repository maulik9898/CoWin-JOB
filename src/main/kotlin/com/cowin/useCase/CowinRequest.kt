package com.cowin.useCase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import com.cowin.model.ResponseDAO
import com.cowin.repository.CowinRepo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CowinRequest(private val cowinRepo: CowinRepo) {

    suspend fun invoke(districtData: List<String>): MutableMap<String, ResponseDAO> {
        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val date = current.format(formatter).toString()
        val result = mutableMapOf<String, ResponseDAO>()
        withContext(Dispatchers.IO) {
            val runningTask = districtData.map { id ->
                async(Dispatchers.IO) {
                    val response = cowinRepo.getByDistrict(id, date)
                    id to response
                }
            }

            val responses = runningTask.awaitAll()
            responses.forEach { (id, response) ->
                run {
                    response.onSuccess { result[id] = it }
                    response.onFailure { println("Failure : $id , Error : ${it.message}")}

                }
            }

        }
        result.forEach { (_, u) ->
            run {
                u.centers.forEach { it ->
                    run {
                        it.sessions.retainAll { it.available_capacity > 0  }
                    }
                }

            }
        }
        result.forEach { (_, u) ->run {
            u.centers.retainAll { !it.sessions.isNullOrEmpty() }
        }  }

        return result

    }

}