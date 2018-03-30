package com.example.sreenu.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*
import org.w3c.dom.Text

class Profile : AppCompatActivity() {

    private var mDataBaseReference: DatabaseReference? = null
    private var mDataBase:FirebaseDatabase? = null
    private var mAuth:FirebaseAuth? = null

    private val TAG = "Loguot"


        private var curUser :TextView?=null
        private var curUserEmail :TextView?=null
        private var curUserEmailVerified :TextView?=null
        private var logoutButton: Button? = null
        private var curUserWallet:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initialise()

        logoutButton!!.setOnClickListener {


            logout()
        }
    }

    private fun initialise(){
        mDataBase = FirebaseDatabase.getInstance()
        mDataBaseReference  = mDataBase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        curUser = findViewById<View>(R.id.userProId) as TextView
        curUserEmail = findViewById<View>(R.id.userProEmail) as TextView
        curUserEmailVerified = findViewById<View>(R.id.userProEmailVerify) as TextView
        logoutButton = findViewById<View>(R.id.proLogoutButton) as Button
        curUserWallet = findViewById<View>(R.id.proWallet) as TextView
    }

    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDataBaseReference!!.child(mUser!!.uid)

        curUserEmail!!.text = mUser.email

        if(!mUser.isEmailVerified)
            curUserEmailVerified!!.text = "Email not verified. Please check your mail for verification link"
        else
            curUserEmailVerified!!.text = "Email has been verified."

        //curUserEmailVerified!!.text = mUser.isEmailVerified.toString()

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                curUser!!.text = snapshot.child("userName").value as String
                curUserEmail!!.text = snapshot.child("email").value as String
                curUserWallet!!.text = snapshot.child("userWallet").value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun logout(){

        mAuth!!.signOut()
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Toast.makeText(this,"Logged Out Successfully",Toast.LENGTH_SHORT).show()

        Log.d(TAG,"Logout:successful")
    }
}
