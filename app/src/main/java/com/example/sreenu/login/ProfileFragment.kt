package com.example.sreenu.login

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.w3c.dom.Text
import java.util.zip.Inflater

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
    private var password:String?=null
    private var passwordText:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         myInflatedProfileView = inflater.inflate(R.layout.fragment_profile,null)
         emailId  = myInflatedProfileView!!.findViewById(R.id.profile_email) as TextView
         profileName = myInflatedProfileView!!.findViewById(R.id.profile_name) as TextView
         passwordResetButton = myInflatedProfileView!!.findViewById(R.id.password_reset_button) as Button

         mAuth = FirebaseAuth.getInstance()
         mProgressBar = ProgressDialog(context)
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

        showResetPasswordDialogBox()
    }

    private fun showResetPasswordDialogBox(){

        val addAmountDialog = AlertDialog.Builder(activity)

        val mView = layoutInflater.inflate(R.layout.newpassworddialog,null)
        val password = mView.findViewById(R.id.dialog_new_password) as EditText
        val confirmButton = mView.findViewById(R.id.new_password_confirm_button)as Button
        val cancelButton = mView.findViewById(R.id.cancel_button) as Button

        val dialog = addAmountDialog.create()
        dialog.setView(mView)
        dialog.show()

        val mUser = mAuth!!.currentUser
        user = FirebaseAuth.getInstance().currentUser

        confirmButton.setOnClickListener {

            passwordText = password.text.toString()

            mUser!!.updatePassword(passwordText!!).addOnCompleteListener { task ->
                if(task.isSuccessful)
                {   mProgressBar!!.hide()
                    Toast.makeText(context,R.string.updated_password,Toast.LENGTH_SHORT).show()
                }

                else{
                    mProgressBar!!.hide()
                    Toast.makeText(context,task.exception!!.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
            }


            }

            mProgressBar!!.hide()
            Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
        

        cancelButton.setOnClickListener {
            dialog.hide()
            mProgressBar!!.hide()
        }

    }



//        with(addAmountDialog){
//
//            newPassword = EditText(context)
//            newPassword!!.hint="Enter new Password"
//            newPassword!!.inputType = InputType.TYPE_CLASS_TEXT
//
//
//
//            setPositiveButton("Done"){
//                dialog, which ->
//                dialog.dismiss()
//                //mySnackbar!!.show()
//                Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
//
//            }
//
//            setNegativeButton("Cancel"){
//                dialog, which ->
//                //Do nothing
//                dialog.dismiss()
//
//            }
        }




