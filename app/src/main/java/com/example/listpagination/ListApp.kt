package com.example.listpagination

import android.app.Application

class ListApp : Application() {
    private lateinit var INSTANCE : ListApp

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}