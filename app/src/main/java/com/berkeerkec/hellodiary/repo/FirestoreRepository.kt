package com.berkeerkec.hellodiary.repo

import com.berkeerkec.hellodiary.roomdb.DiaryDao
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val firestore : FirebaseFirestore,
    private val diaryDao : DiaryDao
) : FirestoreRepositoryInterface {

    override fun getDocumennt(collection: String): Task<QuerySnapshot> {
        return firestore.collection(collection).get()
    }

    override fun addDocument(collection: String, document: Any): Task<DocumentReference> {
        return firestore.collection(collection).add(document)
    }

    override fun updateDocument(
        collection: String,
        documentId: String,
        data: Map<String, Any>
    ): Task<Void> {
        return firestore.collection(collection).document(documentId).update(data)
    }

    override suspend fun deleteAllData() {
        diaryDao.deleteAllData()
    }
}