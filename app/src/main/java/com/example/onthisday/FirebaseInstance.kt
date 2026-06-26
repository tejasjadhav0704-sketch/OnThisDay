package com.example.onthisday

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

    object FireBase_Instance {
        var auth = FirebaseAuth.getInstance()
        var database = FirebaseFirestore.getInstance()
    }