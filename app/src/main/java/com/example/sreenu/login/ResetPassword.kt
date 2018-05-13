package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.sreenu.login.R.id.toolbar
import com.google.firebase.auth.FirebaseAuth

import kotlinx.android.synthetic.main.activity_reset_password.*

class ResetPassword : AppCompatActivity() {

    private var etResetPasswordEmail: EditText?=null
    private var mProgressBar:ProgressDialog?=null
    private var confirmPasswordResetButton: Button?=null
    private var mAuth:FirebaseAuth?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        confirmPasswordResetButton = findViewById(R.id.reset_password_confirm_button)
        etResetPasswordEmail =findViewById(R.id.reset_password_email)
        mAuth = FirebaseAuth.getInstance()
        mProgressBar = ProgressDialog(this)

        confirmPasswordResetButton!!.setOnClickListener {


            mProgressBar!!.show()
            resetPassword()

            finish()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun resetPassword(){

        etResetPasswordEmail = findViewById(R.id.reset_password_email)
        val email = etResetPasswordEmail!!.text.toString()

        mAuth!!.sendPasswordResetEmail(email!!).addOnCompleteListener{ task ->

            if(task.isSuccessful){
                mProgressBar!!.hide()
                Toast.makeText(this,R.string.reset_password_verification, Toast.LENGTH_SHORT).show()
                //Toast.makeText(this,R.string.re_login, Toast.LENGTH_SHORT).show()
                mAuth!!.signOut()


            }

            else{

                mProgressBar!!.hide()
                Toast.makeText(this,R.string.reset_password_verification_failed, Toast.LENGTH_SHORT).show()
            }

        }
    }

}
