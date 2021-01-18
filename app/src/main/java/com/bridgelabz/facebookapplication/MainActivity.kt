package com.bridgelabz.facebookapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        firebaseAuth = FirebaseAuth.getInstance()
        var loginButton = findViewById<Button>(R.id.login_button)

        /* Set on Click Listener on Sign-in button */
        loginButton.setOnClickListener {
            LoginUserAccount()
        }
    }

    private fun LoginUserAccount() {

        val email_login: String = findViewById<EditText>(R.id.userId_EditTExt).text.toString()
        val password_login: String = findViewById<EditText>(R.id.password_EditTExt).text.toString()

        if (TextUtils.isEmpty(email_login)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return
        } else if (TextUtils.isEmpty(password_login)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return

        } else if ((TextUtils.isEmpty(email_login) && TextUtils.isEmpty(password_login))) {
            Toast.makeText(getApplicationContext(),
                    "Please Enter the Details!!",
                    Toast.LENGTH_LONG)
                    .show();
            return
        }

        /* signin existing user */
        firebaseAuth?.signInWithEmailAndPassword(email_login, password_login)
                ?.addOnCompleteListener(object : OnCompleteListener<AuthResult> {

                    override fun onComplete(task: Task<AuthResult>) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(),
                                    "Login successful!!",
                                    Toast.LENGTH_LONG)
                                    .show();


                            /* If login successfull */
                            val intent = Intent(this@MainActivity,
                                    FacebookHomePage::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(applicationContext, "Login Failed!!", Toast.LENGTH_LONG).show()
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