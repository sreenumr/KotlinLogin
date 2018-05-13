package com.example.sreenu.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_card_details.*

var cardNumber:String?=null
var holderName:String?=null
var cvv : String?=null
var expiryMonth : String?=null
var expiryYear : String?=null


class CardDetails : AppCompatActivity() {

    private var confirmButton: Button?=null
    private var etExpiryYear:EditText?=null
    private var etCvv:EditText?=null
    private var etHolderName:EditText?=null
    private var etCardNumber:EditText?=null
    private var etExpiryMonth:EditText?=null

    private var mAuth = FirebaseAuth.getInstance()

    private var  mDataBase = FirebaseDatabase.getInstance()
    private var  mDataBaseReference  = mDataBase!!.reference!!.child("User Wallet")

    private var mUser = mAuth!!.currentUser
    private var  mUserReference = mDataBaseReference!!.child(mUser!!.uid)
    private var  mUserWallet = mDataBaseReference.child(mUser!!.uid)
    private var  mUserId = mUser!!.uid
    private var  mUserWalletMoney:String?=null
    private var  mUserWalletMoneyInt:Int?=null
    private var  totalAmount:Int?=null

    private var bundle:Bundle?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)

        etCardNumber= findViewById(R.id.card_number)
        etHolderName = findViewById(R.id.card_holder_name)
        etExpiryYear = findViewById(R.id.expiry_date_year)
        etExpiryMonth = findViewById(R.id.expiry_date_month)
        etCvv = findViewById(R.id.cvv_number)
        confirmButton = findViewById(R.id.confirm_pay_button)

        cardNumber = etCardNumber!!.text.toString()
        holderName = etHolderName!!.text.toString()
        cvv = etCvv!!.text.toString()
        expiryMonth = etExpiryMonth!!.text.toString()
        expiryYear =  etExpiryYear!!.text.toString()

        confirmButton!!.setOnClickListener {

            if(       !etCardNumber!!.toString().isNullOrEmpty()
                    &&!etExpiryMonth!!.toString().isNullOrEmpty()
                    &&!etExpiryYear!!.toString().isNullOrEmpty()
                    &&!etCvv!!.toString().isNullOrEmpty()
                    &&!etHolderName!!.toString().isNullOrEmpty()) {

                Log.i("Current User" , mUserId)
                updateWallet()
                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, profile_main::class.java))
            }

            else {
                    Log.i("Empty Field","Fields" + " " + cardNumber+ " "+holderName+" "+cvv+" "+expiryMonth+ " "+expiryYear)
                    Toast.makeText(this, "Don't Leave Fields Empty", Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun updateWallet(){

        bundle = intent.extras
        var temp = bundle!!.getString("money")
        Log.i("bundleMoney","temp : " + temp)


        mUserWallet.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot?) {
                 mUserWalletMoney = snapshot!!.child("userWallet").value as String
                Log.i("money",mUserWalletMoney)
                mUserWalletMoneyInt = mUserWalletMoney!!.toInt()
                mUserWalletMoneyInt = mUserWalletMoneyInt!!.plus(temp.toInt())
                mUserWallet.child("userWallet").setValue(mUserWalletMoneyInt.toString())

                // Toast.makeText(this@CardDetails,mUserWalletMoney,Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(snapshot: DatabaseError?) {

            }
        })



        //Log.i("bundleMoney","DataBase wallet : " + mUserWalletMoneyInt)
            //mUserWallet.child("userName").setValue(mUser)
        //mUserWallet.child("userWallet").setValue(mUserWallet.child("userWallet") )

    }
}
