package com.berkeerkec.hellodiary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.berkeerkec.hellodiary.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.util.UUID

class RegisterFragment : Fragment() {

    private var fragmentBinding : FragmentRegisterBinding? = null
    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var storage : FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        firestore = Firebase.firestore
        storage = Firebase.storage
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRegisterBinding.bind(view)
        fragmentBinding = binding

        binding.registerButton.setOnClickListener {
            signUp(it)

        }
    }


    fun signUp(view : View){
        val name = fragmentBinding!!.registerUserName.text.toString()
        val email = fragmentBinding!!.registerEmail.text.toString()
        val password = fragmentBinding!!.registerPassword.text.toString()
        val userMap = HashMap<String,Any>()
        userMap["name"] = name
        userMap["email"] = email
        if (name.equals("") || email.equals("") || password.equals("")){
            Toast.makeText(requireContext(),"Enter name, email and password!", Toast.LENGTH_LONG).show()
        }else{
            auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener {

                firestore.collection("User").document(auth.uid.toString()).set(userMap).addOnSuccessListener {

                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToFeedPageActivity())
                }.addOnFailureListener {
                    Toast.makeText(requireContext(),it.localizedMessage,Toast.LENGTH_LONG).show()
                }



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