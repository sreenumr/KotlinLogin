package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import android.net.NetworkInfo
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.support.v4.view.GravityCompat
import android.view.Gravity
import kotlinx.android.synthetic.main.activity_profile_main.*


private  var email:String? = null
private var password:String? = null
private  var toast:Toast?=null

class MainActivity : AppCompatActivity() {


    private var query :Query?=null

    private val TAG = "Login"

    private var loginUserName:String? = null
    private var etEmail:EditText? = null
    private var etPassword:EditText? = null
    private var loginButton:Button? = null
    private var mProgressBar:ProgressDialog?  = null
    private var mAuth: FirebaseAuth? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(FirebaseAuth.getInstance().currentUser!=null)
         {
             finish()
             val goToProfile = Intent(this,profile_main::class.java)
                startActivity(goToProfile)

            }

        signUpText.setOnClickListener {
            val signUpPage = Intent(this,Registration::class.java)
            startActivity(signUpPage)
        }

        initialise()
        mProgressBar!!.hide()

    }

    private fun initialise(){
        etEmail = findViewById<View>(R.id.loginEmail) as EditText
        etPassword = findViewById<View>(R.id.loginPassword) as EditText
        loginButton = findViewById<View>(R.id.loginButton) as Button
        mProgressBar =  ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        loginButton!!.setOnClickListener {

            if(hasNetwork())
            loginUser()

            else {
                toast =  Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT)
                toast!!.setGravity(Gravity.TOP,0,0)
                toast!!.show()
            }

            }
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
                            updateUI()
                            mProgressBar!!.hide()
                        }
                        else{
                            //Failed sign in
                            mProgressBar!!.hide()
                            Log.e(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()
                        }
                    }

        }
        else Toast.makeText(this,"Invalid Details",Toast.LENGTH_SHORT).show()
    }

    private  fun updateUI(){
        val intent = Intent(this, profile_main::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun hasNetwork(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
         if (netInfo != null && netInfo.isConnected)
           return  true
         else
            return false

    }

    private fun checkConnection() {
        if (hasNetwork()) {
            Toast.makeText(this, "You are connected to Internet", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "You are not connected to Internet", Toast.LENGTH_SHORT).show()
        }
    }

}
