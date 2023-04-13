package com.berkeerkec.hellodiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.berkeerkec.hellodiary.databinding.ActivityFeedPageBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FeedPageActivity : AppCompatActivity() {

    @Inject
    lateinit var fragmentFactory : DiaryFragmentFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_feed_page)
    }
}