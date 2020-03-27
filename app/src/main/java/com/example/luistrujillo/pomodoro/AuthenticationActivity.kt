package com.example.luistrujillo.pomodoro

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, AuthenticationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        setupUI()
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance().reference

        if (currentUser != null) {
//            Log.w(TAG, "Google sign in success!")
//            val templst: MutableList<UsageEvents.Event?> = getDefaultSaved()

            val fireUser = UserProfile(currentUser.displayName, currentUser.uid, 0f, 0f, hashMapOf(), hashMapOf(), hashMapOf())

            // Create a new user
            database.child("users").child(currentUser.uid).setValue(fireUser)

//            userName.text = "Hi, ${currentUser.displayName}!"
        }
        else {
            Log.i("auth", "No user.")
        }
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        startActivity(SignInActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
    }
}
