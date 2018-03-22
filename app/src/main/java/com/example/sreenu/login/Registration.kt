package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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
    private var userName: EditText? = null
    private var etEmail: EditText? = null
    private var etPhoneNumber: EditText? = null
    private var etPassword: EditText? = null
    private var btnRegister: Button? = null
    private var mProgressBar: ProgressDialog? = null

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



    }
}
