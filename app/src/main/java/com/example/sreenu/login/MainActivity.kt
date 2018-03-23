package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

private  var email:String? = null
private var password:String? = null

class MainActivity : AppCompatActivity() {


    private val TAG = "Login"

    private var etEmail:EditText? = null
    private var etPassword:EditText? = null
    private var loginButton:Button? = null
    private var mProgressBar:ProgressDialog?  = null

    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signUpText.setOnClickListener {
            finish()
            val signUpPage = Intent(this,Registration::class.java)

            startActivity(signUpPage)

        }

        initialise()

    }

    private fun initialise(){
        etEmail = findViewById<View>(R.id.loginEmail) as EditText
        etPassword = findViewById<View>(R.id.loginPassword) as EditText
        loginButton = findViewById<View>(R.id.loginButton) as Button
        mProgressBar =  ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        loginButton!!.setOnClickListener { loginUser() }
    }

    private fun loginUser(){

        email = etEmail?.text.toString()
        password = etPassword?.text.toString()

        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){

            mProgressBar!!.setMessage("Logging in ...")
            mProgressBar!!.show()

            Log.d(TAG,"Logging in user")

            mAuth!!.signInWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            Log.d(TAG, "signInWithEmail:success")
                            //updateUI()
                        }
                        else{
                            //Failed sign in
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this,"Login Failled",Toast.LENGTH_SHORT).show()
                        }
                    }

        }
        else Toast.makeText(this,"Invalid Details",Toast.LENGTH_SHORT).show()
    }

}
