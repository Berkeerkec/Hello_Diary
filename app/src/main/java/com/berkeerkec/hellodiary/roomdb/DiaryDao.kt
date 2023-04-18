package com.berkeerkec.hellodiary.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DiaryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary : Diary)

    @Delete
    suspend fun deleteDiary(diary : Diary)

    @Query("SELECT * FROM diary ORDER BY id DESC")
    fun observeDiary() : LiveData<List<Diary>>

}