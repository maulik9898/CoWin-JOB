package com.cowin.singleton

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.messaging.FirebaseMessaging

object FirebaseSingl {
    private val fireBaseApp: FirebaseApp by lazy {
        val options: FirebaseOptions = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.getApplicationDefault())
            .build()
        FirebaseApp.initializeApp(options)
    }
    val db: Firestore by lazy {
        FirestoreClient.getFirestore(fireBaseApp)
    }
    val messaging: FirebaseMessaging by lazy {
        FirebaseMessaging.getInstance();
    }
}