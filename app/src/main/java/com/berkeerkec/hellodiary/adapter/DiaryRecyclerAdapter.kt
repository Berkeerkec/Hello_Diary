package com.berkeerkec.hellodiary.adapter

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.berkeerkec.hellodiary.databinding.FeedAddRowBinding
import com.berkeerkec.hellodiary.roomdb.Diary
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DiaryRecyclerAdapter @Inject constructor() : RecyclerView.Adapter<DiaryRecyclerAdapter.DiaryHolder>() {

    class DiaryHolder(val binding : FeedAddRowBinding) : ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Diary>(){
        override fun areItemsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Diary, newItem: Diary): Boolean {
            return oldItem == newItem
        }

    }

    private val recyclerListDiffer = AsyncListDiffer(this,diffUtil)

    var diaries : List<Diary>
        get() = recyclerListDiffer.currentList
        set(value) = recyclerListDiffer.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiaryHolder {
        val binding = FeedAddRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return DiaryHolder(binding)
    }

    override fun getItemCount(): Int {
        return diaries.size
    }

    override fun onBindViewHolder(holder: DiaryHolder, position: Int) {
        val diary = diaries[position]

        holder.itemView.apply {
            holder.binding.feedTitleView.text = diary.title
            holder.binding.feedDateView.text = diary.date
            val byteArray = diary.image
            val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
            holder.binding.feedEmojiView.setImageBitmap(bitmap)

        }
    }
}