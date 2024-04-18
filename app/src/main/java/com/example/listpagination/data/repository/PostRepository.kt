package com.example.listpagination.data.repository

import com.example.listpagination.api.PostDataSource
import com.example.listpagination.model.Post
import com.example.listpagination.utils.NetworkResult

class PostRepository(private val remote: PostDataSource) {
    suspend fun fetchPosts(page: Int): List<Post> {
        return when (val response = remote.selectPosts(page)) {
            is NetworkResult.Success -> response.data
            is NetworkResult.Error -> throw response.exception
        }
    }
}