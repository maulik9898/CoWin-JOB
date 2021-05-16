package com.cowin.repository

import com.google.api.core.ApiFuture
import com.google.auth.oauth2.GoogleCredentials

import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import com.cowin.model.FCM
import com.cowin.model.Notify
import com.cowin.model.Subscribe
import com.cowin.singleton.FirebaseSingl
import com.cowin.useCase.Firebase
import com.google.cloud.firestore.*

class FirebaseRepo {

    suspend fun getSubscriber():Result<List<QueryDocumentSnapshot>> = kotlin.runCatching {
        val future : ApiFuture<QuerySnapshot> = FirebaseSingl.db.collection("subscribe").get()
        future.get().documents
    }

    suspend fun senPushNotification(user:Subscribe ): Result<Result<ApiFuture<WriteResult>>> = kotlin.runCatching {
        val notify = Notify("Vaccine Available","Vaccine is available in your area. Click to know more","TAG","/assets/icons/android-icon-96x96.png",
            mutableMapOf("url" to "https://cowid.live/center?val=online")
        )
        val fcm = FCM(notify)
        val s = Json.encodeToString(fcm);

        val message = Message.builder().setToken(user.fcmId).putData("data",s).build();
        FirebaseSingl.messaging.send(message)
        this.updateTimestamp(user)


    }

    private suspend fun updateTimestamp(user:Subscribe) = kotlin.runCatching {
        FirebaseSingl.db.collection("subscribe")
            .document(user.id)
            .update("lastTimestamp" ,Math.floorDiv(System.currentTimeMillis() , 1000))
    }

    suspend fun upDateCounter(counter:Int) = kotlin.runCatching {

        val counterRef = FirebaseSingl.db.collection("counters").document("notification")
        counterRef.update("val",FieldValue.increment(counter.toLong()))

    }

}