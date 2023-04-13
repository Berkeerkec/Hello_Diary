package com.berkeerkec.hellodiary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.berkeerkec.hellodiary.adapter.DiaryRecyclerAdapter
import com.berkeerkec.hellodiary.repo.DiaryRepositoryInterface
import com.berkeerkec.hellodiary.viewmodel.DiaryViewModel
import javax.inject.Inject

class DiaryFragmentFactory @Inject constructor(
    private val diaryRecyclerAdapter : DiaryRecyclerAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {

        return when(className){
            FeedFragment::class.java.name -> FeedFragment(diaryRecyclerAdapter)

            else -> super.instantiate(classLoader, className)
        }
    }
}