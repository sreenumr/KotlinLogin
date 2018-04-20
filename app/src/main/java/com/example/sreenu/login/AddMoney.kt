package com.example.sreenu.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast


class AddMoney : AppCompatActivity() {

    private var etEnterAmount:EditText?=null
    private var continueButton: Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_money)



        etEnterAmount = findViewById(R.id.et_enter_amount)
        continueButton = findViewById(R.id.continue_button)

        checkAmount()

        continueButton!!.setOnClickListener{

            if(isValidAmount()){
            val cardDetailsPage = Intent(this,CardDetails::class.java)
            startActivity(cardDetailsPage )
            }

            else{
                Toast.makeText(this,"Enter a valid amount",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkAmount(){

    etEnterAmount!!.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if (etEnterAmount!!.text.toString().isNullOrEmpty()){
                etEnterAmount!!.error  = "Empty"


            }


        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }

    private fun isValidAmount():Boolean{

        if(etEnterAmount!!.text.toString().toInt()==0||etEnterAmount!!.text.isNullOrEmpty())
            return false
        else
            return true
    }
}
