package com.example.sreenu.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_card_details.*

class CardDetails : AppCompatActivity() {

    private var confirmButton: Button?=null
    private var etExpiry:EditText?=null
    private var etCvv:EditText?=null
    private var etHolderName:EditText?=null
    private var etCardNumber:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)

        confirmButton = findViewById(R.id.confirm_pay_button)
        etCardNumber = findViewById(R.id.card_number)
        etHolderName = findViewById(R.id.card_holder_name)
        etCvv = findViewById(R.id.cvv_number)
        etExpiry = findViewById(R.id.expiry_date)

        var cardNumber = etCardNumber!!.text.toString()
        var holderName = etHolderName!!.text.toString()
        var cvv = etCvv!!.text.toString()
        var expiry = etExpiry!!.text.toString()

        confirmButton!!.setOnClickListener {

            if(!TextUtils.isEmpty(cardNumber)
                    ||!TextUtils.isEmpty(expiry)
                    ||!TextUtils.isEmpty(cvv)
                    ||!TextUtils.isEmpty(holderName)) {

                Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, profile_main::class.java))
            }

            else
                Toast.makeText(this,"Don't Leave Fields Empty",Toast.LENGTH_SHORT).show()
        }
    }
}
