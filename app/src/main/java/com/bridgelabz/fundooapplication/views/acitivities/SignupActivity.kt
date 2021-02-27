package com.bridgelabz.fundooapplication.views.acitivities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bridgelabz.fundooapplication.R
import com.bridgelabz.fundooapplication.views.mainactivityview.MainActivity
import com.google.firebase.auth.FirebaseAuth

class SignupActivity : AppCompatActivity() {

    var firebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
    }


    fun facebookRegistration(view: View) {
        firebaseAuth = FirebaseAuth.getInstance()
        val signUpButton = findViewById<Button>(R.id.register_button)

        signUpButton.setOnClickListener {
            registerNewUser()
        }
    }

    private fun registerNewUser() {
        val fName = findViewById<EditText>(R.id.firstName_EditText)
        val lName = findViewById<EditText>(R.id.lastName_EditText)

        val email = findViewById<EditText>(R.id.emailId_EditText)
        val password = findViewById<EditText>(R.id.password_EditText)


        /* Debugging */
        println("firstname:$fName")
        println("lastname:$lName")
        println("Emailid:$email")
        println("password:$password")

        if (fName.text.toString().isEmpty()) {
            fName.error = "Please Enter The First Name"
            fName.requestFocus()
            return;
        }

        if (!fName.text.toString().matches("[A-Z][a-z]*" .toRegex())){
            fName.error = "FirstName Should Contain Only Alphabets"
            fName.requestFocus()
            return
        }

        if (lName.text.toString().isEmpty()) {
            lName.error = "Please Enter The Last Name"
            lName.requestFocus()
            return;
        }

        if (!lName.text.toString().matches("[A-Z][a-z]*" .toRegex())){
            lName.error = "lastName Should Contain Only Alphabets"
            lName.requestFocus()
            return
        }

        if (email.text.toString().isEmpty()) {
            email.error = "Please Enter The Email Id"
            email.requestFocus()
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()) {
            email.error = "Email Should Be Valid"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()) {
            password.error = "Please Enter The Password"
            password.requestFocus()
            return;
        }

        if (password.text.toString().length < 8) {
            password.error = "Password should be minimum 8 characters"
            password.requestFocus()
              return;
        }

        if (!password.text.toString().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[!@#$%^&*+=?-]).{8,15}$".toRegex())){
            password.error = "Password should contain Numbers, Alphabets,[@#$%^&+=] one of the symbol"
            password.requestFocus()
            return;
        }

        firebaseAuth
            ?.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_LONG).show()

                    val intent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(applicationContext, "Registration Failed!!!" + "Please Try again later", Toast.LENGTH_LONG).show()
                }
            }
    }

    fun setupHyperlink(view: View) {
        val linkTextView = findViewById<TextView>(R.id.hyperlink_TextView)
        linkTextView.setLinkTextColor(Color.BLUE)
        linkTextView.setOnClickListener {
            val switchActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(switchActivityIntent)
        }
    }
}