package com.example.sreenu.login

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile_main.*
import kotlinx.android.synthetic.main.app_bar_profile_main.*


class profile_main : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var mDataBaseReference: DatabaseReference? = null
    private var mDataBase:FirebaseDatabase? = null
    private var mAuth:FirebaseAuth? = null

    private val TAG = "Loguot"

    private var curUser : TextView?=null
    private var curUserEmail : TextView?=null
    private var curUserEmailVerified : TextView?=null
    private var curUserWallet: TextView? = null
    private var navigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_main)

        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        initialise()

        nav_view.setNavigationItemSelectedListener(this)
        displayFragment(-1)

        navigationView = findViewById(R.id.nav_view)

    }

    private fun initialise(){
        mDataBase = FirebaseDatabase.getInstance()
        mDataBaseReference  = mDataBase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener(this)
        val header = navigationView.getHeaderView(0)

        Log.i("nullUser",curUser.toString())

        curUser = findViewById(R.id.navHeaderName)
        curUserEmail = findViewById(R.id.navHeaderEmail)

        curUser = header.findViewById(R.id.navHeaderName)
        curUserEmail = header.findViewById(R.id.navHeaderEmail)
        curUserWallet = findViewById(R.id.walletAmount)
        curUserEmailVerified = header.findViewById(R.id.navHeadConfirmEmail)
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

        mUserReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Log.i("nullEmail",mUser.email.toString())
                Log.i("nullEmail",snapshot.child("userName").value.toString())

                curUser!!.text = snapshot.child("userName").value as String
                curUserEmail!!.text = snapshot.child("email").value as String
                Log.w("wallet",snapshot.child("userWallet").value.toString())
                //curUserWallet!!.text = snapshot.child("userWallet").value as String
                Log.e("walletNull",curUserWallet.toString().isNullOrEmpty().toString() )
                //curUserWallet?.text = "1000"
            }
            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.profile_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            R.id.action_logout -> {
                logout()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

   private fun displayFragment(id:Int){

            val fragment = when(id){
                R.id.navWallet ->{
                    WalletFragment()
                }

                R.id.navHome ->{
                    HomeFragment()
                }

                else->{
                    HomeFragment()
                }
            }

        supportFragmentManager.beginTransaction().replace(R.id.linearLayout,fragment).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.

        displayFragment(item.itemId)

        when (item.itemId) {


        }


        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout(){

        mAuth!!.signOut()
        val intent = Intent(this,MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        Toast.makeText(this,"Logged Out Successfully", Toast.LENGTH_SHORT).show()

        Log.d(TAG,"Logout:successful")
    }


}
