package com.example.sreenu.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText


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

            val cardDetailsPage = Intent(this,CardDetails::class.java)
            startActivity(cardDetailsPage )
        }
    }

    private fun checkAmount(){

    etEnterAmount!!.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            if (etEnterAmount!!.text.toString().isNullOrEmpty()){
                etEnterAmount!!.error  = "Empty"

            if(etEnterAmount!!.text.toString().toInt()>=2000)
                etEnterAmount!!.error = "Should be less than or equal to 2000"

            }


        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })

    }
}
