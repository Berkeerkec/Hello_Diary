package com.berkeerkec.hellodiary

import android.app.DatePickerDialog
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.drawToBitmap
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.berkeerkec.hellodiary.databinding.FragmentDetailsBinding
import com.berkeerkec.hellodiary.roomdb.Diary
import com.berkeerkec.hellodiary.util.Status
import com.berkeerkec.hellodiary.viewmodel.DiaryViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar
import java.util.UUID

class DetailsFragment : Fragment() {

    private lateinit var fragmentBinding : FragmentDetailsBinding
    lateinit var viewModel : DiaryViewModel
    private lateinit var storage : FirebaseStorage
    private lateinit var firestore : FirebaseFirestore
    private lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = Firebase.storage
        firestore = Firebase.firestore
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity())[DiaryViewModel::class.java]

        val binding = FragmentDetailsBinding.bind(view)
        fragmentBinding = binding

        arguments?.let {
            val info = DetailsFragmentArgs.fromBundle(it).info

            if (info.equals("new")){
                binding.detailsTitleView.setText("")
                binding.detailsTextView.setText("")
                binding.imageAdd.setImageResource(R.drawable.emoji_add)
                binding.dateView.text = ""
                binding.saveButton.visibility = View.VISIBLE
                binding.deleteButton.visibility = View.GONE

            }else{
                binding.saveButton.visibility = View.GONE
                binding.deleteButton.visibility = View.VISIBLE

                val selectedId = DetailsFragmentArgs.fromBundle(it).id


                viewModel.diaryList.observe(viewLifecycleOwner, Observer {
                    it.map {
                      if (it.id == selectedId){
                          binding.detailsTitleView.setText(it.title)
                          binding.detailsTextView.setText(it.text)
                          binding.dateView.text = it.date
                          val bitmap = BitmapFactory.decodeByteArray(it.image,0,it.image.size)
                          binding.imageAdd.setImageBitmap(bitmap)

                          binding.deleteButton.setOnClickListener { view ->
                              if (it.id == selectedId){
                                  val choosenDiary = it
                                  viewModel.deleteDiary(choosenDiary)
                                  findNavController().navigate(DetailsFragmentDirections.actionDetailsFragment2ToFeedFragment2())
                              }
                          }

                      }
                    }
                })
            }
        }

        subscribeToObserver()

        binding.imageAdd.setOnClickListener {
            showDialog()
        }

        binding.dateView.setOnClickListener {
            val calender = Calendar.getInstance()
            val cyear = calender.get(Calendar.YEAR)
            val cday = calender.get(Calendar.DAY_OF_MONTH)
            val cmonth = calender.get(Calendar.MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(),DatePickerDialog.OnDateSetListener { datePicker, year, month, day ->
                binding.dateView.text = "Date: $day/$month/$year"
            },cyear,cmonth + 1,cday)
            datePickerDialog.show()
        }

        binding.saveButton.setOnClickListener {
            if (binding.imageAdd != null){
                val smallBitmap = makeSmallerImage(binding.imageAdd.drawToBitmap(),100)
                val outputStream = ByteArrayOutputStream()
                smallBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream)
                val byte = outputStream.toByteArray()
                val date = binding.dateView.text.toString()
                viewModel.makeDiary(
                    binding.detailsTitleView.text.toString(),
                    binding.detailsTextView.text.toString(),
                    byte,
                    date)


                val storageRef = storage.reference
                val fileName = "my_emoji.jpg"
                val imageRef = storageRef.child("images/$fileName")
                val uploadTask = imageRef.putBytes(byte).addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener {

                        val imageUrl = it.toString()
                        val map = HashMap<String,Any>()
                        map.put("title", binding.detailsTitleView.text.toString())
                        map.put("text", binding.detailsTextView.text.toString())
                        map.put("emojiUrl", imageUrl)
                        map.put("date", date)
                        viewModel.addMyDocuments(map)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(),"Your data could not be loaded into the database",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }

    }

    private fun subscribeToObserver(){
        viewModel.insertDiaryMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    findNavController().popBackStack()
                    viewModel.resertInsertDiaryMsg()
                }
                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?: "Error", Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {}
            }
        })
    }


    fun makeSmallerImage(image : Bitmap, maximumSize : Int) : Bitmap{
        var width = image.width
        var height = image.height
        val bitmapRatio : Double = width.toDouble() / height.toDouble()

        if (bitmapRatio > 1){
            width = maximumSize
            val selectedHeight = width/bitmapRatio
            height = selectedHeight.toInt()
        }else{
            height = maximumSize
            val selectedWidth = height * bitmapRatio
            width = selectedWidth.toInt()
        }
        return Bitmap.createScaledBitmap(image,width,height,true)
    }



    private fun showDialog(){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.bottomsheetlayout)

        val angryView : ImageView = dialog.findViewById(R.id.angryView)
        val smileView : ImageView = dialog.findViewById(R.id.smileView)
        val inloveView : ImageView = dialog.findViewById(R.id.inloveView)
        val laughingView : ImageView = dialog.findViewById(R.id.laughingView)
        val deadView : ImageView = dialog.findViewById(R.id.deadView)
        val cryView : ImageView = dialog.findViewById(R.id.cryView)
        val nerdView : ImageView = dialog.findViewById(R.id.nerdView)
        val confusedView : ImageView = dialog.findViewById(R.id.confusedView)
        val sadView : ImageView = dialog.findViewById(R.id.sadView)

        angryView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.angry_image)
        }
        smileView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.smile_image)
        }
        inloveView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.inlove_image)
        }
        laughingView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.laughing_image)
        }
        deadView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.dead_image)
        }
        cryView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.sad2_image)
        }
        nerdView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.nerd_image)
        }
        confusedView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.confused_image)
        }
        sadView.setOnClickListener {
            dialog.dismiss()
            fragmentBinding.imageAdd.setImageResource(R.drawable.sad_image)
        }
        dialog.show()
        dialog.window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.window!!.setGravity(Gravity.BOTTOM)

    }


}