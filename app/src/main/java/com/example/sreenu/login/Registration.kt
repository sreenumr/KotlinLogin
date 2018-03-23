package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_registration.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

private val TAG = "CreateAccountActivity"

private var firstName: String? = null
private var lastName : String? = null
private var userName : String? = null
private var email : String? = null
private var phoneNumber : String? = null
private var password: String? = null


class Registration : AppCompatActivity() {

    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etUserName: EditText? = null
    private var etEmail: EditText? = null
    private var etPhoneNumber: EditText? = null
    private var etPassword: EditText? = null
    private var btnRegister: Button? = null
    private var mProgressBar: ProgressBar? = null

    //Firebase Refs
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        loginText.setOnClickListener {
            finish()
            val loginPage = Intent(this,MainActivity::class.java)
            startActivity(loginPage)

        }

        initialise()

    }

    private fun initialise(){

        etFirstName = findViewById<View>(R.id.regFirstName) as EditText
        etLastName  = findViewById<View>(R.id.regLastName) as EditText
        etUserName  = findViewById<View>(R.id.regUserName) as EditText
        etEmail  = findViewById<View>(R.id.regEmail) as EditText
        etPhoneNumber  = findViewById<View>(R.id.regPhoneNumber) as EditText
        etPassword = findViewById<View>(R.id.regPassword) as EditText
        btnRegister = findViewById<View>(R.id.btnSignUp) as Button

        mProgressBar = ProgressBar(this)
        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        btnRegister!!.setOnClickListener { createNewAccount() }
    }

    private fun createNewAccount(){
        firstName = etFirstName!!.text.toString()
        lastName = etLastName!!.text.toString()
        userName = etUserName!!.text.toString()
        email = etEmail!!.text.toString()
        phoneNumber = etPhoneNumber!!.text.toString()
        password = etPassword!!.text.toString()

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneNumber))

                Toast.makeText(this,"Welcome",Toast.LENGTH_SHORT).show();

            else {

            Toast.makeText(this,"Fields are Empty!",Toast.LENGTH_SHORT).show()
        }
    }
}
