package com.berkeerkec.hellodiary

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.berkeerkec.hellodiary.adapter.DiaryRecyclerAdapter
import com.berkeerkec.hellodiary.databinding.FragmentFeedBinding
import com.berkeerkec.hellodiary.viewmodel.DiaryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class FeedFragment @Inject constructor(
    private val diaryRecyclerAdapter : DiaryRecyclerAdapter
) : Fragment() {

    private var fragmentBinding : FragmentFeedBinding? = null
    private var clicked = false
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private var feedName : String? = null
    lateinit var viewModel : DiaryViewModel

    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.rotate_open_anim
    ) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.rotate_close_anim
    ) }
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.from_bottom_anim
    ) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(requireContext(),
        R.anim.to_bottom_anim
    ) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        firestore.collection("User").document(auth.uid.toString()).addSnapshotListener { value, error ->

            if (value != null){
                val name = value.get("name")
                fragmentBinding!!.feedNameText.text = name.toString()

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DiaryViewModel::class.java]

        val binding = FragmentFeedBinding.bind(view)
        fragmentBinding = binding

        binding.recyclerViewDiary.adapter = diaryRecyclerAdapter
        binding.recyclerViewDiary.layoutManager = LinearLayoutManager(requireContext())

        subscribeToObserver()
        binding.fab.setOnClickListener {
            onAddButtonClicked()
        }

        binding.exitFab.setOnClickListener {
            auth.signOut()
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        binding.editFab.setOnClickListener {
            findNavController().navigate(FeedFragmentDirections.actionFeedFragmentToDetailsFragment("new",0))

        }

    }

    private fun subscribeToObserver(){
        viewModel.diaryList.observe(viewLifecycleOwner, Observer {
            diaryRecyclerAdapter.diaries = it
        })
    }

    private fun onAddButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }

    private fun setAnimation(clicked : Boolean) {
        if (!clicked){
            fragmentBinding!!.editFab.startAnimation(fromBottom)
            fragmentBinding!!.exitFab.startAnimation(fromBottom)
            fragmentBinding!!.fab.startAnimation(rotateOpen)
        }else{
            fragmentBinding!!.editFab.startAnimation(toBottom)
            fragmentBinding!!.exitFab.startAnimation(toBottom)
            fragmentBinding!!.fab.startAnimation(rotateClose)
        }
    }

    private fun setVisibility(clicked: Boolean) {
        if (!clicked){
            fragmentBinding!!.editFab.visibility = View.VISIBLE
            fragmentBinding!!.exitFab.visibility = View.VISIBLE
        }else{
            fragmentBinding!!.editFab.visibility = View.INVISIBLE
            fragmentBinding!!.exitFab.visibility = View.INVISIBLE
        }
    }

    private fun setClickable(clicked: Boolean){
        if (!clicked){
            fragmentBinding!!.editFab.isClickable = true
            fragmentBinding!!.exitFab.isClickable = true
        }else{
            fragmentBinding!!.editFab.isClickable = false
            fragmentBinding!!.exitFab.isClickable = false
        }
    }

    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }

}