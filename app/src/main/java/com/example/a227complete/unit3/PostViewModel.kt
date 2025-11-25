package com.example.a227complete.unit3

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData

class PostViewModel : ViewModel() {

    val posts = liveData {
        try {
            val response = RetrofitClient.api.getPosts()
            emit(response)
        } catch (e: Exception) {
            emit(emptyList<Post>())
        }
    }
}
