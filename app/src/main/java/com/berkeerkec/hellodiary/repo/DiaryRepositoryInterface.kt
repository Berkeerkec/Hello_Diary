package com.berkeerkec.hellodiary.repo

import androidx.lifecycle.LiveData
import com.berkeerkec.hellodiary.roomdb.Diary
import com.berkeerkec.hellodiary.util.Resource
import javax.inject.Singleton

interface DiaryRepositoryInterface{

    suspend fun insertDiary(diary :Diary)

    suspend fun deleteDiary(diary: Diary)

    fun getData() : LiveData<List<Diary>>



}