package com.bridgelabz.facebookapplication
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN: Int = 123
    var firebaseAuth: FirebaseAuth? = null
    lateinit var signInByGoogleBtn:SignInButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this,gso)
        signInByGoogleBtn = findViewById(R.id.signInButton)

        signInByGoogleBtn.setOnClickListener {
            signIn()
        }

        val forgotPassword = findViewById<TextView>(R.id.ForgotPassword_TextView)
        forgotPassword.setOnClickListener{
        }

    }

    private fun signIn() {
       /* val intent = Intent(this,FacebookHomePage::class.java)
        startActivity(intent)*/
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d("***********", "firebaseAuthWithGoogle:successfull" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w("***********", "Google sign in failed", e)

            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Tag", "signInWithCredential:success")
                    val intentTofacebookHome = Intent(this,FacebookHomePage::class.java)
                    startActivity(intentTofacebookHome)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                    }
            }
    }

    fun facebookLogin(view: View) {
        firebaseAuth = FirebaseAuth.getInstance()
        val loginButton = findViewById<Button>(R.id.login_button)

        /* Set on Click Listener on login button */
        loginButton.setOnClickListener {
            LoginUserAccount()
        }
    }

    private fun LoginUserAccount() {

        val email_login: String = findViewById<EditText>(R.id.userId_EditTExt).text.toString()
        val password_login: String = findViewById<EditText>(R.id.password_EditTExt).text.toString()

        if (TextUtils.isEmpty(email_login)) {
            Toast.makeText(
                getApplicationContext(),
                "Please enter email!!",
                Toast.LENGTH_LONG
            )
                .show();
            return
        } else if (TextUtils.isEmpty(password_login)) {
            Toast.makeText(
                getApplicationContext(),
                "Please enter password!!",
                Toast.LENGTH_LONG
            )
                .show();
            return

        } else if ((TextUtils.isEmpty(email_login) && TextUtils.isEmpty(password_login))) {
            Toast.makeText(
                getApplicationContext(),
                "Please Enter the Details!!",
                Toast.LENGTH_LONG
            )
                .show();
            return
        }

        /* signin existing user */
        firebaseAuth?.signInWithEmailAndPassword(email_login, password_login)
            ?.addOnCompleteListener(object : OnCompleteListener<AuthResult> {

                override fun onComplete(task: Task<AuthResult>) {
                    if (task.isSuccessful()) {
                        Toast.makeText(
                            getApplicationContext(),
                            "Login successful!!",
                            Toast.LENGTH_LONG
                        )
                            .show();


                        /* If login successfull */
                        val intent = Intent(
                            this@MainActivity,
                            FacebookHomePage::class.java
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Login Failed!!", Toast.LENGTH_LONG)
                            .show()
                    }

                }
            })

    }

    fun registrationPage(view: View) {

        val switchSignupActivityIntent = Intent(this, SignupActivity::class.java)
        startActivity(switchSignupActivityIntent)
    }

    fun setForgotHyperlink(view: View) {

        val forgotLink = findViewById<TextView>(R.id.ForgotPassword_TextView)
        forgotLink.setLinkTextColor(Color.BLUE)
        forgotLink.setOnClickListener {
            val switchForgotActivityIntent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(switchForgotActivityIntent)
        }
    }


}