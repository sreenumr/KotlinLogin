package com.example.sreenu.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


class AdminFragment : Fragment() {

    private var userListView:ListView?=null
    private var mDataBaseReference: DatabaseReference? = null
    private var mDataBase:FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private var userList:ArrayList<String>?=null
    private var adapter:ArrayAdapter<String>?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val myInflatedView =  inflater.inflate(R.layout.fragment_admin,null)

        mDataBase = FirebaseDatabase.getInstance()
        mDataBaseReference  = mDataBase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()

        userList = ArrayList(10)

        userListView = myInflatedView.findViewById(R.id.user_list)
        var uid = FirebaseAuth.getInstance().uid

        mDataBaseReference!!.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot?) {

              for(datasnaphot in snapshot!!.children){


                  val user = datasnaphot.child("userName").value.toString()//.child("userName").toString()
                  Log.d("TAG", user)

                  userList!!.add(user)

              }

                adapter = ArrayAdapter(context,android.R.layout.simple_list_item_1,userList)
                userListView!!.setAdapter(adapter)
            }


            override fun onCancelled(snapshot: DatabaseError?) {

            }
        })



        return myInflatedView
    }

}
