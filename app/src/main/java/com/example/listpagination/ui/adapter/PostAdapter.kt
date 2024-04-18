package com.example.listpagination.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.listpagination.databinding.PostItemBinding
import com.example.listpagination.model.Post
import com.example.listpagination.ui.PostDetailActivity
import com.example.listpagination.utils.Constant
import com.google.gson.Gson

class PostAdapter(val context : Context) :
    PagingDataAdapter<Post, PostAdapter.PostViewHolder>(PostComparator()) {

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(context, item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        return PostViewHolder.from(parent)
    }

    class PostViewHolder(private val binding: PostItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(context: Context, item: Post) {
            binding.postTitle.text = item.title
            binding.postDescription.text = item.body.replace('\n', ' ')
            binding.root.setOnClickListener {
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra(Constant.POST_DETAIL_INFO, Gson().toJson(item))
                context.startActivity(intent)
            }
        }

        companion object {
            fun from(parent: ViewGroup): PostViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = PostItemBinding.inflate(layoutInflater, parent, false)
                return PostViewHolder(binding)
            }
        }
    }
}

class PostComparator : DiffUtil.ItemCallback<Post>() {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}