package com.example.listpagination.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.listpagination.R
import com.example.listpagination.databinding.ActivityPostDetailBinding
import com.example.listpagination.model.Post
import com.example.listpagination.utils.Constant
import com.google.gson.Gson

class PostDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.post_detail)
        val postDetail = Gson().fromJson(intent.getStringExtra(Constant.POST_DETAIL_INFO), Post::class.java)
        binding.postTitle.text = postDetail.title
        binding.postDescription.text = postDetail.body
        binding.postId.text = postDetail.id.toString()
        binding.postUserId.text = postDetail.userId.toString()

        binding.toolBar.setNavigationOnClickListener {
            finish()
        }
    }
}