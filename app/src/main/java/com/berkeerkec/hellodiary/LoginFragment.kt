package com.berkeerkec.hellodiary

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.berkeerkec.hellodiary.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

class LoginFragment : Fragment() {

    private var fragmentBinding : FragmentLoginBinding? = null
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentLoginBinding.bind(view)
        fragmentBinding = binding


        binding.loginButton.setOnClickListener {
            signIn(it)
        }

        binding.goToRegister.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    fun signIn(view : View){
        val email = fragmentBinding!!.loginEmail.text.toString()
        val password = fragmentBinding!!.loginPassword.text.toString()
        if (email.equals("") || password.equals("")){
            Toast.makeText(requireContext(),"Enter email and password!", Toast.LENGTH_LONG).show()
        }else{
            auth.signInWithEmailAndPassword(email,password).addOnSuccessListener {

                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToFeedPageActivity())
            }.addOnFailureListener {
                Toast.makeText(requireContext(),it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }

}