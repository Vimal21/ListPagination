package com.example.listpagination.api

import com.example.listpagination.model.Post
import com.example.listpagination.utils.NetworkResult

class PostDataSource(private val service: PostService) : NetworkCaller() {
    suspend fun selectPosts(page: Int): NetworkResult<List<Post>> {
        return getResult { service.getPosts(page) }
    }
}