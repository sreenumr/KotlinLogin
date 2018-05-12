package com.example.sreenu.login


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.LatLng
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import android.app.AlertDialog
import android.app.FragmentManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.nfc.Tag
import android.support.v7.widget.CardView
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.widget.*
import com.directions.route.RoutingListener
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment
import com.google.android.gms.location.places.ui.PlaceSelectionListener
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.fragment_home.*
import java.io.IOException

private const val  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
private var googleMap: GoogleMap? = null
private var mMapView: MapView? = null
private var mLastloction:Location?=null
private var mLocationRequest:LocationRequest?=null
private var mGoogleApiClient:GoogleApiClient?=null
private var destination:String?=null
private var geocoder:Geocoder?=null
private var currentLocation:Location?=null

private var results = FloatArray(10)

private const  val TAG = "search"

class HomeFragment:Fragment(),LocationListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{


      private var searchAddress:CardView?=null
      private var mLocationPermissionGranted:Boolean? =null
      private var mCurrentLocation:Button?=null
      private var mWhereTo:Button?=null
      private var mStartLocation:EditText?=null
      private var mEndLocation:EditText?=null
      private var etSearchLocation:EditText?=null
      private var addressList:List<Address>?=null
      private var address:Address?=null
      private var markers:ArrayList<Marker>?=null
      private var callCabButton:Button?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val myInflatedMapView =  inflater.inflate(R.layout.fragment_home,null)
//            mapFragment = m
//                    .findFragmentById(R.id.map) as HomeFragment

        // Construct a GeoDataClient.
       val  mGeoDataClient = Places.getGeoDataClient(context!!)

        // Construct a PlaceDetectionClient.
       val mPlaceDetectionClient = Places.getGeoDataClient(context!!)

       // mCurrentLocation = myInflatedMapView.findViewById(R.id.current_location)
          mWhereTo = myInflatedMapView.findViewById(R.id.where_to_button)
          mMapView = myInflatedMapView.findViewById(R.id.mapView)
          etSearchLocation = myInflatedMapView.findViewById(R.id.search_text)
          callCabButton = myInflatedMapView.findViewById(R.id.call_cab)
          //searchAddress = myInflatedMapView.findViewById(R.id.search_address)


                  mMapView!!.onCreate(savedInstanceState)

        //mMapView!!.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }



        mWhereTo!!.setOnClickListener {

            onSearch()
        }

        callCabButton!!.setOnClickListener{
            callCab()
        }

        //getDestination()
        getLocationPermission()

       // onClickMap()

        mMapView!!.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            getLocationPermission()

            val amrita  = LatLng(9.0936997, 76.49149549999993)
            //googleMap!!.addMarker(MarkerOptions().position(amrita).title("Marker Title").snippet("Marker Description"))

            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(amrita).zoom(12f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        })

        return myInflatedMapView


    }

//    private fun onClickMap{
//
//
//    }


    private fun getDistance(){

        currentLocation = googleMap!!.myLocation
        val startLat = currentLocation!!.latitude
        val startLong = currentLocation!!.longitude
        val endLat = marker!!.position.latitude
        val endLong = marker!!.position.longitude

         Location.distanceBetween(startLat,startLong,endLat,endLong,results)

        Toast.makeText(context,"Distance: " + results[0], Toast.LENGTH_SHORT).show()
    }


    private fun callCab(){
        if(marker!=null) {
            //Toast.makeText(context,"call cab",Toast.LENGTH_SHORT).show()
            getDistance()
        }
        else
            Toast.makeText(context,"Enter or Search a Location",Toast.LENGTH_SHORT).show()
    }

    private var marker:Marker?=null

    private fun onSearch(){

        val searchLocation = etSearchLocation!!.text.toString()
            Log.i("searchLocation",searchLocation)
            //Log.i("address",geocoder!!.getFromLocationName(searchLocation,1).get(0).toString())

        geocoder = Geocoder(context)

        if(!searchLocation.isNullOrEmpty()) {
            try {
                addressList = geocoder!!.getFromLocationName(searchLocation, 1)
            } catch (e: IOException) {
                Toast.makeText(context, "Enter a location", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

            if (addressList!!.isNotEmpty()) {
                
                marker?.remove()
                address = addressList!!.get(0)

                val latLng = LatLng(address!!.latitude, address!!.longitude)

                //Log.i("marker",markers!!.size)

                //markers!!.clear()

                //markers!!.clear()
                marker = googleMap!!.addMarker(MarkerOptions().position(latLng))

                // markers?.add(marker)

                googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
            }
        }
            else
                Toast.makeText(context,"Invalid input Location",Toast.LENGTH_SHORT).show()

    }
    override fun onLocationChanged(location: Location?) {

//        val mGoogleApiClient = GoogleApiClient.Builder(context!!)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build()
//        mGoogleApiClient.connect()

        mLastloction = location

        val latLng  = LatLng(location!!.latitude,location!!.longitude)
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))


    }


    override fun onConnected(p0: Bundle?) {
        mLocationRequest!!.setInterval(1000)
        mLocationRequest!!.setFastestInterval(1000)
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ContextCompat.checkSelfPermission(context!!, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            googleMap?.isMyLocationEnabled =true
            mLocationPermissionGranted = true

        } else { ActivityCompat.requestPermissions(activity!!, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this)
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

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
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
