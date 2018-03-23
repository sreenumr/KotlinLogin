package com.example.sreenu.login

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.android.synthetic.main.activity_main.*

private  var email:String? = null
private var passsword:String? = null

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        signUpText.setOnClickListener {
            finish()
            val signUpPage = Intent(this,Registration::class.java)

            startActivity(signUpPage)

        }



    }
}
