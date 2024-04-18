package com.example.listpagination.api.exceptions

import java.io.IOException

class NoNetworkException : IOException() {
    override val message: String
        get() = "Not Internet connection available"
}