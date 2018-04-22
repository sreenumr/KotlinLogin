package com.example.sreenu.login

import android.app.ProgressDialog
import android.app.VoiceInteractor
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.basgeekball.awesomevalidation.AwesomeValidation
import com.basgeekball.awesomevalidation.ValidationStyle
import com.basgeekball.awesomevalidation.utility.RegexTemplate
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_registration.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.regex.Pattern

private val TAG = "CreateAccountActivity"
private var errorMessage:String? = null
private var firstName: String? = null
private var lastName : String? = null
private var userName : String? = null
private var email : String? = null
private var phoneNumber : String? = null
private var password: String? = null
private var confirmPassword: String? = null

class Registration : AppCompatActivity() {

    private var etFirstName: EditText? = null
    private var etLastName: EditText? = null
    private var etUserName: EditText? = null
    private var etEmail: EditText? = null
    private var etPhoneNumber: EditText? = null
    private var etPassword: EditText? = null
    private var etConfirmPassword:EditText? = null
    private var btnRegister: Button? = null
    private var mProgressBar: ProgressDialog? = null

    //Firebase Refs
    private var mDatabaseReferencePromoCodes:DatabaseReference? = null
    private var mDatabaseReferenceWallet :DatabaseReference? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)



        loginText.setOnClickListener {
            val loginPage = Intent(this,MainActivity::class.java)
            startActivity(loginPage)
           // finish()
        }

        initialise()
        validate()

    }

    private fun initialise(){

        etFirstName = findViewById<View>(R.id.regFirstName) as EditText
        etLastName  = findViewById<View>(R.id.regLastName) as EditText
        etUserName  = findViewById<View>(R.id.regUserName) as EditText
        etEmail  = findViewById<View>(R.id.regEmail) as EditText
        etPhoneNumber  = findViewById<View>(R.id.regPhoneNumber) as EditText
        etPassword = findViewById<View>(R.id.regPassword) as EditText
        btnRegister = findViewById<View>(R.id.btnSignUp) as Button
        etConfirmPassword = findViewById<View>(R.id.regConfirmPassword) as EditText
        mProgressBar = ProgressDialog(this)

    mDatabase = FirebaseDatabase.getInstance()
    mDatabaseReference = mDatabase!!.reference!!.child("Users")
    mDatabaseReferenceWallet = mDatabase!!.reference!!.child("User Wallet")
    mDatabaseReferencePromoCodes = mDatabase!!.reference!!.child("Promo Codes")
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
        confirmPassword = etConfirmPassword!!.text.toString()

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
                && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(phoneNumber) && TextUtils.equals(password,confirmPassword) ) {

            //Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
            mProgressBar!!.setMessage("Please Wait...")
            mProgressBar!!.show()
            mAuth!!

                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this){task ->
                        mProgressBar!!.hide()

                        if(task.isSuccessful){

                            Log.d(TAG,"createUserWithEmail:success")

                            val userId = mAuth!!.currentUser!!.uid

                            //Verify email fun

                           verifyEmail()

                            //update user Profile
                            val currentUserDb = mDatabaseReference!!.child(userId)
                            val currentUserDbWallet = mDatabaseReferenceWallet!!.child(userId)
                            currentUserDb.child("firstName").setValue(firstName)
                            currentUserDb.child("lastName").setValue(lastName)
                            currentUserDb.child("userName").setValue(userName)
                            currentUserDb.child("email").setValue(email)
                            currentUserDb.child("phoneNumber").setValue(phoneNumber)
                            currentUserDb.child("password").setValue(password)
                            currentUserDbWallet.child("userWallet").setValue("0")
                            currentUserDbWallet.child("userName").setValue(userName)

                           updateUser()
                        }


                        else{
                            //Failed sign in
                            validate()
                            errorMessage = task.exception!!.localizedMessage.toString()
                            Log.w(TAG,"createUserWithEmail:failure",task.exception)
                            Toast.makeText(this, errorMessage,Toast.LENGTH_SHORT).show()
                        }

                    }
        }

        else if(!(password!!.equals(confirmPassword) ) && !TextUtils.isEmpty(confirmPassword))
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()


        else {

            Toast.makeText(this,"Fields are Empty!",Toast.LENGTH_SHORT).show()
        }


    }

private fun updateUser(){

        val intent = Intent(this,profile_main::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification()
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this,
                                "Verification email sent to " + mUser.getEmail(),
                                Toast.LENGTH_SHORT).show()
                    } else {
                        Log.e(TAG, "sendEmailVerification", task.exception)
                        Toast.makeText(this,
                                "Failed to send verification email.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
    }

//    private fun validate(){
//        if(!Patterns.EMAIL_ADDRESS.matcher(etEmail!!.text.toString()).matches())
//                   etEmail!!.error = "Invalid format"
//        if(!Patterns.PHONE.matcher(etPhoneNumber!!.text.toString()).matches())
//                    etPhoneNumber!!.error = "Invalid Number"
//
//    }
    private fun validate(){

        etFirstName!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etFirstName!!.text.toString().isNullOrEmpty()){
                    etFirstName!!.error  = "Empty"

                }


            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etUserName!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etUserName!!.text.toString().isNullOrEmpty())
                    etUserName!!.error  = "Empty"

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etEmail!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etEmail!!.text.toString().isNullOrEmpty())
                    etEmail!!.error  = "Empty"

                if(!Patterns.EMAIL_ADDRESS.matcher(etEmail!!.text.toString()).matches())
                    etEmail!!.error = "Invalid format"

            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

        etPassword!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (etPassword!!.text.toString().isNullOrEmpty())
                    etPassword!!.error  = "Empty"

               else  if (etPassword!!.toString().length<6)
                    etPassword!!.error = "Password must be between 6 and 20 letters"

            }


            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                 if (etPassword!!.toString().length<6) {
                    etPassword!!.error = "Password length must be at least 6"


                 }
            }
        })

        etConfirmPassword!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!(etPassword!!.text.toString().equals(etConfirmPassword!!.text.toString()) ) )
                    etConfirmPassword!!.error  = "Passwords Do Not Match"

            }
        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

}
