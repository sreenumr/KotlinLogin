package com.example.sreenu.login

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.Manifest
import android.view.ViewGroup
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import java.security.Permission
import java.security.Permissions
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.annotation.NonNull
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.maps.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home_map.*

private const val  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
private var requestCode:Int?=null;

class HomeFragment:Fragment() {

      var mMapView: MapView? = null
      private var googleMap: GoogleMap? = null
      private var mLocationPermissionGranted:Boolean? =null
      private var mCurrentLocation:Button?=null
      private var mWhereTo:Button?=null
      private var mStartLocation:EditText?=null
      private var mEndLocation:EditText?=null
      private var mGoogleApiClient: GoogleApiClient?=null
      private var mapFragment:HomeFragment?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val myInflatedMapView =  inflater.inflate(R.layout.fragment_home,null)
//            mapFragment = m
//                    .findFragmentById(R.id.map) as HomeFragment

        // Construct a GeoDataClient.
       val  mGeoDataClient = Places.getGeoDataClient(context!!)

        // Construct a PlaceDetectionClient.
       val mPlaceDetectionClient = Places.getGeoDataClient(context!!)

        // Construct a FusedLocationProviderClient.
        val mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

       // mCurrentLocation = myInflatedMapView.findViewById(R.id.current_location)
          mWhereTo = myInflatedMapView.findViewById(R.id.where_to_button)
          mMapView = myInflatedMapView.findViewById(R.id.mapView)
          mMapView!!.onCreate(savedInstanceState)

        //mMapView!!.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

//
        mWhereTo!!.setOnClickListener {

            getDestination()
        }

        getDestination()
        getLocationPermission()
//        mapFragment!!.getMapAsync(this)

        mMapView!!.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            // For showing a move to my location button

            getLocationPermission()
            // googleMap!!.isMyLocationEnabled = true

            // For dropping a marker at a point on the Map
            val amrita  = LatLng(9.0936997, 76.49149549999993)
            googleMap!!.addMarker(MarkerOptions().position(amrita).title("Marker Title").snippet("Marker Description"))

            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(amrita).zoom(12f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        })

        return myInflatedMapView
    }



    override fun onResume() {
        super.onResume()
        mMapView!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMapView!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMapView!!.onLowMemory()
    }


    private fun getDestination(){

        val addAmountDialog = AlertDialog.Builder(activity)
        var linearLayout:LinearLayout?=null
        linearLayout = LinearLayout(context)
        linearLayout.setOrientation(LinearLayout.VERTICAL)
        with(addAmountDialog){

            mStartLocation = EditText(context)
            mStartLocation!!.hint="From"
            mStartLocation!!.inputType = InputType.TYPE_CLASS_TEXT
            linearLayout.addView(mStartLocation)


            mEndLocation = EditText(context)
            mEndLocation!!.hint="To"
            mEndLocation!!.inputType = InputType.TYPE_CLASS_TEXT
            linearLayout.addView(mEndLocation)

            setPositiveButton("Add"){
                dialog, which ->
                dialog.dismiss()
               // mySnackbar!!.show()

            }

            setNegativeButton("Cancel"){
                dialog, which ->
                //Do nothing
                dialog.dismiss()

            }
        }

        //Pops up the dialog box
        val dialog = addAmountDialog.create()
        dialog.setView(linearLayout)
        dialog.show()
    }

//    private fun requestPermission(permissionType:String , requestCode: Int){
//        ActivityCompat.requestPermissions(activity!!, arrayOf(permissionType),requestCode)
//    }

    private fun getLocationPermission() {
        /*
     * Request location permission, so that we can get the location of the
     * device. The result of the permission request is handled by a callback,
     * onRequestPermissionsResult.
     */
        if (ContextCompat.checkSelfPermission(
                        context!!,
                        android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
                googleMap?.isMyLocationEnabled =true
            mLocationPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(activity!!,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
            }
        }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {


        when (requestCode){
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION ->{
                if(grantResults.isEmpty()||grantResults[0]!=PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(context,"Unable to show location - need permission", Toast.LENGTH_SHORT).show()

                }

                else{

                    Toast.makeText(context,"Permission granted", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

}
