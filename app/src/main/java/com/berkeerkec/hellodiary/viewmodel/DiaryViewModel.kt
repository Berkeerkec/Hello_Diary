package com.berkeerkec.hellodiary.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.berkeerkec.hellodiary.repo.DiaryRepositoryInterface
import com.berkeerkec.hellodiary.roomdb.Diary
import com.berkeerkec.hellodiary.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val repository : DiaryRepositoryInterface
) :ViewModel(){

    val diaryList = repository.getData()

    private var insertDiaryMsg = MutableLiveData<Resource<Diary>>()
    val insertDiaryMessage : LiveData<Resource<Diary>>
    get() = insertDiaryMsg

    fun resertInsertDiaryMsg(){
        insertDiaryMsg = MutableLiveData<Resource<Diary>>()
    }

    fun insertDiary(diary : Diary) = viewModelScope.launch {
        repository.insertDiary(diary)
    }

    fun deleteDiary(diary : Diary) = viewModelScope.launch {
        repository.deleteDiary(diary)
    }

    fun makeDiary(title : String, text : String, image : ByteArray, date : String){
        if (title.isEmpty() || text.isEmpty() || image.isEmpty() || date.isEmpty()){
            insertDiaryMsg.postValue(Resource.error("Enter title, text, date and emoji", null))
            return
        }

        val diary = Diary(title,text,image,date)
        insertDiary(diary)
        insertDiaryMsg.postValue(Resource.success(diary))

    }



}