package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.w3c.dom.Text

private var mDataBaseReference: DatabaseReference? = null
private var mDataBase: FirebaseDatabase? = null
private var mAuth: FirebaseAuth? = null
private var mProgressBar:ProgressDialog?=null
private var user:FirebaseUser?=null

class ProfileFragment : Fragment() {

    private var emailId:TextView?=null
    private var profileName: TextView?=null
    private var passwordResetButton:Button?=null
    private var myInflatedProfileView:View?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         myInflatedProfileView = inflater.inflate(R.layout.fragment_profile,null)
         emailId  = myInflatedProfileView!!.findViewById(R.id.profile_email) as TextView
         profileName = myInflatedProfileView!!.findViewById(R.id.profile_name) as TextView
         passwordResetButton = myInflatedProfileView!!.findViewById(R.id.password_reset_button) as Button

        mAuth = FirebaseAuth.getInstance()

        initialise()

        passwordResetButton!!.setOnClickListener {

            mProgressBar!!.setMessage("Please Wait..")
            mProgressBar!!.show()
            resetPassword()

        }

        return myInflatedProfileView

    }

    private fun initialise(){

        mDataBase = FirebaseDatabase.getInstance()
        mDataBaseReference  = mDataBase!!.reference!!.child("Users")
        val mUser = FirebaseAuth.getInstance().currentUser
        val mUserReference = mDataBaseReference!!.child(mUser!!.uid)

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                emailId!!.text = snapshot.child("email").value as String
                profileName!!.text = snapshot.child("userName").value as String

            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun resetPassword(){

        val emailAddress = mAuth!!.currentUser!!.email
        user = FirebaseAuth.getInstance().currentUser

        //val password = user.

        //Log.i("email" , "Reset Email : " + emailAddress)
        mAuth!!.sendPasswordResetEmail(emailAddress!!).addOnCompleteListener{task ->

            if(task.isSuccessful){
                mProgressBar!!.hide()
                Toast.makeText(context,R.string.reset_password_verification,Toast.LENGTH_SHORT).show()
                Toast.makeText(context,R.string.re_login,Toast.LENGTH_SHORT).show()
                mAuth!!.signOut()
               // mDataBaseReference!!.child(m)
            }
            else{
                mProgressBar!!.hide()
                Toast.makeText(context,R.string.reset_password_verification_failed,Toast.LENGTH_SHORT).show()
            }

        }

    }
}
