package com.berkeerkec.hellodiary.dependencyInject

import android.content.Context
import android.widget.Toast
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berkeerkec.hellodiary.repo.DiaryRepository
import com.berkeerkec.hellodiary.repo.DiaryRepositoryInterface
import com.berkeerkec.hellodiary.roomdb.DiaryDao
import com.berkeerkec.hellodiary.roomdb.DiaryDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun injectRoomDatabase(@ApplicationContext context : Context) = Room.databaseBuilder(context, DiaryDatabase::class.java,"DiaryDB").build()

    @Singleton
    @Provides
    fun injectDao(diaryDatabase : DiaryDatabase) = diaryDatabase.diaryDao()

    @Singleton
    @Provides
    fun provideDiaryRepository(dao : DiaryDao) = DiaryRepository(dao) as DiaryRepositoryInterface


}