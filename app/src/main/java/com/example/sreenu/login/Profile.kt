package com.example.sreenu.login

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

class Profile : AppCompatActivity() {

    private var mDataBaseReference: DatabaseReference? = null
    private var mDataBase:FirebaseDatabase? = null
    private var mAuth:FirebaseAuth? = null

        private var curUser :TextView?=null
        private var curUserEmail :TextView?=null
        private var curUserEmailVerified :TextView?=null
        private var logoutButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initialise()

        logoutButton!!.setOnClickListener {

            mAuth!!.signOut()
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
    }

    override fun onStart() {
        super.onStart()

        val mUser = mAuth!!.currentUser
        val mUserReference = mDataBaseReference!!.child(mUser!!.uid)

        curUserEmail!!.text = mUser.email
        curUserEmailVerified!!.text = mUser.isEmailVerified.toString()

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                curUser!!.text = snapshot.child("userName").value as String
                curUserEmail!!.text = snapshot.child("email").value as String
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }
}
