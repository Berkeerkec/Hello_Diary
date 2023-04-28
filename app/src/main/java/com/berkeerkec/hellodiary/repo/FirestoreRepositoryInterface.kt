package com.berkeerkec.hellodiary.repo

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface FirestoreRepositoryInterface  {

    fun getDocumennt(collection : String) :Task<QuerySnapshot>

    fun addDocument(collection: String, document : Any) : Task<DocumentReference>

    fun updateDocument(collection: String, documentId: String, data: Map<String, Any>) : Task<Void>

    suspend fun deleteAllData()

}