package com.example.luistrujillo.pomodoro

//import com.google.android.gms.auth.api.signin.GoogleSignIn


import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_settings.*

// TODO: associate this data to a user profile
class SettingsActivity : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 9001
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mGoogleSignInOptions: GoogleSignInOptions


    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Settings"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        configureGoogleSignIn()
        setupUI()
        auth = FirebaseAuth.getInstance()

    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance().reference

        if (currentUser != null) {
            Log.i("auth", "Google sign in success! Settings Activity")

            val fireUser = UserProfile(currentUser.displayName, currentUser.uid, 0f, 0f, hashMapOf(), hashMapOf(), hashMapOf())

            // Create a new user
            database.child("users").child(currentUser.uid).setValue(fireUser)

            userNameTextView.text = "Hi, ${currentUser.displayName}!"
        }
        else {
            Log.i("auth", "No user.")
        }
    }

    private fun configureGoogleSignIn() {
        mGoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, mGoogleSignInOptions)
    }

    private fun setupUI() {
        googleButton.setOnClickListener {
            signIn()
        }
        signOutButton.setOnClickListener {
            signOut()
        }
    }

    private fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
//        startActivity(SignInActivity.getLaunchIntent(this))
        FirebaseAuth.getInstance().signOut()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    firebaseAuthWithGoogle(account)
                    emailTextView.text = account.email
                }
            } catch (e: ApiException) {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(acct: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acct.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
//                val intent =  Intent(this, AuthenticationActivity::class.java)
//                startActivity(intent)
                Toast.makeText(this,"Login success.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_LONG).show()
            }
        }
    }

}
