package com.berkeerkec.hellodiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    @Inject
    lateinit var fragmentFactory : DiaryFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null){
            val intent = Intent(this, FeedPageActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}