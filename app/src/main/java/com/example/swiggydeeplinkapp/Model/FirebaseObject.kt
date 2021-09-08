package com.example.swiggydeeplinkapp.Model

import com.google.firebase.database.FirebaseDatabase

object FirebaseObject {
    val database by lazy{
        FirebaseDatabase.getInstance()
    }
}