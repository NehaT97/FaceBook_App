package com.bridgelabz.facebookapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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

        val fName: String = findViewById<EditText>(R.id.firstName_EditText).text.toString()
        val lName: String = findViewById<EditText>(R.id.lastName_EditText).text.toString()

        val email: String = findViewById<EditText>(R.id.emailId_EditText).text.toString()
        val password: String = findViewById<EditText>(R.id.password_EditText).text.toString()


        println("firstname:$fName")
        println("lastname:$lName")
        println("Emailid:$email")
        println("password:$password")


        if (TextUtils.isEmpty(fName)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter FirstName!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(lName)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter Last Name!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

         if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

       else if ((fName.isEmpty() && lName.isEmpty() && email.isEmpty() && password.isEmpty())){
            Toast.makeText(getApplicationContext(),
                    "Enter The Details!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        firebaseAuth
                ?.createUserWithEmailAndPassword(email, password)
                ?.addOnCompleteListener(object: OnCompleteListener<AuthResult> {
                    @Override
                    override fun onComplete(task: Task<AuthResult>) {

                        if (task.isSuccessful){
                            Toast.makeText(applicationContext,"Registration Successful",Toast.LENGTH_LONG).show()

                            val intent = Intent(this@SignupActivity,
                                    MainActivity::class.java)
                            startActivity(intent)
                        }

                        else{
                            Toast.makeText(applicationContext,"Registration Failed!!!" + "Please Try again later",Toast.LENGTH_LONG).show()
                        }
                    }
                })
    }

    fun setupHyperlink(view: View) {
        val linkTextView = findViewById<TextView>(R.id.hyperlink_TextView)
        linkTextView.setLinkTextColor(Color.BLUE)
        linkTextView.setOnClickListener{
            val switchActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(switchActivityIntent)
        }
    }

}