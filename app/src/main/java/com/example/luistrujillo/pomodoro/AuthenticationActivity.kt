package com.example.luistrujillo.pomodoro

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_authentication.*

class AuthenticationActivity : AppCompatActivity() {

    companion object {
        fun getLaunchIntent(from: Context) = Intent(from, AuthenticationActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        setupUI()
    }

    private fun setupUI() {
        sign_out_button.setOnClickListener {
            signOut()
        }
    }

    private fun signOut() {
        startActivity(SignInActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut();
    }
}
