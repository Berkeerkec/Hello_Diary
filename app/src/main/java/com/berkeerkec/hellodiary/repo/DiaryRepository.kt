package com.berkeerkec.hellodiary.repo

import androidx.lifecycle.LiveData
import com.berkeerkec.hellodiary.roomdb.Diary
import com.berkeerkec.hellodiary.roomdb.DiaryDao
import com.berkeerkec.hellodiary.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRepository @Inject constructor(
    private val diaryDao : DiaryDao
) : DiaryRepositoryInterface {

    override suspend fun insertDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }

    override suspend fun deleteDiary(diary: Diary) {
        diaryDao.deleteDiary(diary)
    }

    override fun getData(): LiveData<List<Diary>> {
        return diaryDao.observeDiary()
    }



}