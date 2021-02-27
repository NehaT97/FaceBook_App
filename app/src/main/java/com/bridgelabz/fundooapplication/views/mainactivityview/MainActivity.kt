package com.bridgelabz.fundooapplication.views.mainactivityview

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.loadingAlertDialog.ProgressLoader
import com.bridgelabz.fundooapplication.repository.NoteService
import com.bridgelabz.fundooapplication.views.acitivities.HomeDashboardActivity
import com.bridgelabz.fundooapplication.views.acitivities.SignupActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 123
    lateinit var signInByGoogleBtn: SignInButton
    lateinit var noteService: NoteService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        retrieveFCMRegistrationToken()
        socialLogin()
    }

    private fun retrieveFCMRegistrationToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            if (it.isSuccessful){
                val token = it.result
                Log.i("*****Token Generated*****","$token")
                Toast.makeText(this,"Token Generated",Toast.LENGTH_LONG).show()

            }else{
                Log.w("Fails", "Fetching FCM registration token failed", it.exception)

            }
        }
    }

    private fun socialLogin() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        auth = FirebaseAuth.getInstance()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInByGoogleBtn = findViewById(R.id.signInButton)
        signInByGoogleBtn.setOnClickListener {
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)

        val progressLoader = ProgressLoader(this@MainActivity)
        progressLoader.startLoadingDialog()
        val handler = Handler()
        handler.postDelayed({
            progressLoader.dismissDialog()
        }, 7000)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("***********", "authWithGoogle:successfull" + account.id)
                authWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("***********", "Google sign in failed", e)

            }
        }
    }

    private fun authWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("TAG", "signInWithCredential:success")
                    val intentToDashboard = Intent(this,HomeDashboardActivity::class.java)
                    startActivity(intentToDashboard)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }

    fun appLogin(view: View) {
        auth = FirebaseAuth.getInstance()
        val loginButton = findViewById<Button>(R.id.login_button)

        /* Set on Click Listener on login button(Login Functionality) */
        loginButton.setOnClickListener {
            loginUserAccount()
        }
    }

    private fun loginUserAccount() {
        val emailLogin = findViewById<EditText>(R.id.userId_EditTExt)
        val passwordLogin = findViewById<EditText>(R.id.password_EditTExt)

        if (emailLogin.text.toString().isEmpty()) {
            emailLogin.error = "Please Enter The Email"
            emailLogin.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(emailLogin.text.toString()).matches()) {
            emailLogin.error = "Please Enter The Valid Email"
            emailLogin.requestFocus()
            return
        }

        if (passwordLogin.text.toString().isEmpty()) {
            emailLogin.error = "Please Enter The Password"
            emailLogin.requestFocus()
            return
        }

        /* signing existing user */
        auth?.signInWithEmailAndPassword(
            emailLogin.text.toString(),
            passwordLogin.text.toString()
        )
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        applicationContext,
                        "Login successful!!",
                        Toast.LENGTH_LONG
                    ).show();

                    /* If login Successful */
                    val intent = Intent(this@MainActivity, HomeDashboardActivity::class.java)
                    startActivity(intent)
                   // finish()

                } else {
                    Toast.makeText(
                        applicationContext,
                        "Login Failed , Wrong Password!!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    fun registrationPage(view: View) {
        val switchSignupActivityIntent = Intent(this, SignupActivity::class.java)
        startActivity(switchSignupActivityIntent)
    }

    fun setForgotHyperlink(view: View) {
        val forgotPassword = findViewById<TextView>(R.id.ForgotPassword_TextView)
        forgotPassword.setLinkTextColor(Color.BLUE)

        forgotPassword.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Forgot Password")

            val view = layoutInflater.inflate(R.layout.dialog_forgot_password, null)
            val userName = view.findViewById<EditText>(R.id.userName)
            println("UserName1 -----${userName.text.toString()}")

            builder.setView(view)
            builder.setPositiveButton("Reset") { _, _ ->
                forgotPassword(userName)
            }

            builder.setNegativeButton("cancel") { _, _ ->
            }
            builder.show()
        }
    }

    private fun forgotPassword(userName: EditText) {
        if (userName.text.toString().isEmpty()) {
            println("UserName2 -----${userName.text.toString()}")
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(userName.text.toString()).matches()) {
            return
        }

        auth.sendPasswordResetEmail(userName.text.toString())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Email Sent", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(this,"Email Is Invalid",Toast.LENGTH_LONG).show()
                }
            }
    }
}