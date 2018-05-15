package com.example.sreenu.login


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Places
import android.app.AlertDialog
import android.app.FragmentManager
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.nfc.Tag
import android.os.Handler
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v7.widget.CardView
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
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
import com.google.android.gms.maps.model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.payment.*
import org.w3c.dom.Text
import java.io.IOException
import java.util.*
import java.util.zip.Inflater
import kotlin.collections.ArrayList

private const val  PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
private var googleMap: GoogleMap? = null
private var mMapView: MapView? = null
private var mLastloction:Location?=null
private var mLocationRequest:LocationRequest?=null
private var mGoogleApiClient:GoogleApiClient?=null
private var destination:String?=null
private var geocoder:Geocoder?=null
private var currentLocation:Location?=null
private var locationManager:LocationManager?=null
private var latitudeList:ArrayList<String>?=null
private var longitudeList:ArrayList<String>?=null
private var newMarker:Marker?=null

private var mDatabaseReference:DatabaseReference?=null
private var mDatabase:FirebaseDatabase?=null
private var mAuth:FirebaseAuth?=null

private var cost:Float?=null
private var distance:Float?=null
private var random:Random?=null
private var radioGroupPayment:RadioGroup?=null

private var driverName:String?=null
private var driverCarNumber:String?=null
private var results = FloatArray(10)
private var total:Float?=null
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
      private var markerList:ArrayList<Marker>?=null
      private var callCabButton:Button?=null
      private var snackbar:Snackbar?=null
      private var myInflatedMapView:View?=null

      private var mDatabaseWalletReference:DatabaseReference?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

         myInflatedMapView =  inflater.inflate(R.layout.fragment_home,null)

        checkInternet()
        initialise()


//        myInflatedMapView!!.setOnTouchListener(object: View.OnTouchListener{
//            override fun onTouch(view:View, ev: MotionEvent):Boolean {
//                hideKeyboard(view)
//                return false
//            }
//        })

        mMapView!!.onCreate(savedInstanceState)

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

        if(results[0]/1000>150) {
            distance = results[0]/1000
            Toast.makeText(context, "Location too far", Toast.LENGTH_SHORT).show()
        }

        else {
            distance = results[0]/1000
             total = distance!! * cost!!
            Toast.makeText(context, "Distance: " + distance + " km", Toast.LENGTH_SHORT).show()
        }
    }


    private fun callCab(){

//        val lat = random!!.nextDouble()%100
//        val long = random!!.nextDouble()%100

       // val latLng = LatLng(lat,long)

        //markerList!!.add(latLng)

        if(marker!=null) {
            //Toast.makeText(context,"call cab",Toast.LENGTH_SHORT).show()
            getDistance()
            placeRequest()

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

        if(hasNetwork())
        {
        if(!searchLocation.isNullOrEmpty()) {
            try {
                addressList = geocoder!!.getFromLocationName(searchLocation, 1)
            } catch (e: IOException) {
                Toast.makeText(context, "Enter a location", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }

           try {


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
           } catch (e:NullPointerException){
               Toast.makeText(context,"Try Again",Toast.LENGTH_SHORT).show()

           }

             if(addressList!!.isEmpty())
                Toast.makeText(context,"Invalid input Location",Toast.LENGTH_SHORT).show()
        }
            else
                Toast.makeText(context,"Invalid input Location",Toast.LENGTH_SHORT).show()

                }
        else
            Toast.makeText(context,R.string.no_network,Toast.LENGTH_SHORT).show()
    }
    override fun onLocationChanged(location: Location?) {

        mLastloction = location

        val mUser = mAuth!!.currentUser
        val mUserReference = mDatabaseReference!!.child(mUser!!.uid)
        val latLng  = LatLng(location!!.latitude,location!!.longitude)
        googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap!!.animateCamera(CameraUpdateFactory.zoomTo(11f))


//        mDatabaseReference!!.addValueEventListener(object : ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot?) {
//
//                for(datasnapshot in snapshot!!.children){
//
//                    if(datasnapshot.child("Driver").value!!.equals(true)&&datasnapshot.hasChild("latitude")&&datasnapshot.hasChild("longitude"))
//                        latitudeList!!.add(datasnapshot.child("latitude").value.toString())
//                        longitudeList!!.add(datasnapshot.child("longitude").value.toString())
//
//                        Log.i("latlong",datasnapshot.child("latitude").toString() + "," + datasnapshot.child("longitude"))
//                }
//            }
//
//            override fun onCancelled(snapshot: DatabaseError?) {
//
//            }
//        })

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

    private fun locationDialog(){

        val addAmountDialog = AlertDialog.Builder(activity)
        var linearLayout:LinearLayout?=null
        linearLayout = LinearLayout(context)
        linearLayout.setOrientation(LinearLayout.VERTICAL)
        with(addAmountDialog){

            setPositiveButton("Ok"){
                dialog, which ->
                dialog.dismiss()
               // mySnackbar!!.show()
                enableGPS()
            }

            setNegativeButton("Cancel"){
                dialog, which ->
                //Do nothing
                dialog.dismiss()

            }
        }

        //Pops up the dialog box
        val dialog = addAmountDialog.create()
        dialog.setMessage("Turn On Location?")
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

    private fun isGPSEnabled(): Boolean {

            val  service = context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
             return service!!.isProviderEnabled(LocationManager.GPS_PROVIDER)//isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private fun enableGPS(){

               val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent)
    }

    private fun placeRequest(){
        chooseCarDialog()
    }

    private fun chooseCarDialog(){


            val addAmountDialog = AlertDialog.Builder(activity)
            var linearLayout:LinearLayout?=null
            //linearLayout = LinearLayout(context)
            //linearLayout.setOrientation(LinearLayout.VERTICAL)

            val mView = layoutInflater.inflate(R.layout.choose_car,null)
            val car = mView.findViewById(R.id.radio_car_type) as RadioGroup
            val tvDone = mView.findViewById(R.id.done_text) as TextView



            with(addAmountDialog){

//                setPositiveButton("Ok"){
//                    dialog, which ->
//                    dialog.dismiss()
//                    // mySnackbar!!.show()
//                    enableGPS()
//                }
//
//                setNegativeButton("Cancel"){
//                    dialog, which ->
//                    //Do nothing
//                    dialog.dismiss()
//
//                }
            }

            //Pops up the dialog box
            val dialog = addAmountDialog.create()
            dialog.setMessage("Choose Car")
            dialog.setView(mView)
        if(distance!!<150)
            dialog.show()

        tvDone.setOnClickListener {
            dialog.dismiss()

            Handler().postDelayed({
                makePayment()
            },5000)
            }
        }

    private fun showDrivers(){

        val lat = googleMap!!.myLocation.latitude+0.005
        val long = googleMap!!.myLocation.longitude+0.005

        val latLng = LatLng(lat,long)

//        val startLat = currentLocation!!.latitude
//        val startLong = currentLocation!!.longitude
//        val endLat = marker!!.position.latitude
//        val endLong = marker!!.position.longitude
        driverCarNumber="ABC123"
        driverName="Driver"
         val newMarker = googleMap!!.addMarker(MarkerOptions().position(latLng).title("New cab\n" ).snippet(driverName +"\n"+ driverCarNumber)
                 .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))

        newMarker.showInfoWindow()


        googleMap!!.setOnMarkerClickListener(object:GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(marker:Marker):Boolean {
                if(newMarker.title.equals("New Cab"))
                Toast.makeText(context, "Cab", Toast.LENGTH_SHORT).show()


                return false
            }
        })
        //marker = googleMap!!.addMarker(MarkerOptions().position(latLng))
        Log.i("cabLocation",latLng.toString())
          //markerList!!.add(googleMap!!.addMarker(MarkerOptions().position(latLng)))
    }

    private fun makePayment(){

        paymentDialog()

    }

    private fun cardPayment(){
        val payment = Intent(context,Payment::class.java)
        startActivity(payment)
    }

    private fun paymentDialog(){
        val addAmountDialog = AlertDialog.Builder(activity)
        var linearLayout:LinearLayout?=null
        //linearLayout = LinearLayout(context)
        //linearLayout.setOrientation(LinearLayout.VERTICAL)

        val mView = layoutInflater.inflate(R.layout.payment,null)
        val type = mView.findViewById(R.id.radio_payment) as RadioGroup
        val tvDone = mView.findViewById(R.id.payment_done) as TextView
        val tvtotal = mView.findViewById(R.id.total_amount) as TextView
        tvtotal.text ="₹" + total.toString()
        radioGroupPayment = mView.findViewById(R.id.radio_payment) as RadioGroup



        with(addAmountDialog){

            //                setPositiveButton("Ok"){
//                    dialog, which ->
//                    dialog.dismiss()
//                    // mySnackbar!!.show()
//                    enableGPS()
//                }
//
//                setNegativeButton("Cancel"){
//                    dialog, which ->
//                    //Do nothing
//                    dialog.dismiss()
//
//                }
        }

        //Pops up the dialog box
        val dialog = addAmountDialog.create()
        dialog.setMessage("Payment method")
        dialog.setView(mView)
        //dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()

        tvDone.setOnClickListener {

            if(radioGroupPayment!!.checkedRadioButtonId==R.id.radio_wallet){
                val mUser = mAuth!!.currentUser
                val   mUserWallet = mDatabaseWalletReference!!.child(mUser!!.uid)

                mUserWallet!!.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot:DataSnapshot?) {
                      val walletMoney = snapshot!!.child("userWallet").value
                        if (walletMoney.toString().toInt()<total!!.toInt())
                            Toast.makeText(context,R.string.insufficient_funds,Toast.LENGTH_SHORT).show()
                        else{
                            Toast.makeText(context,R.string.success,Toast.LENGTH_SHORT).show()
                            mUserWallet.child("userWallet").setValue((walletMoney.toString().toInt() - total!!.toInt()).toString())
                            dialog.dismiss()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError?) {

                    }
                }
                )


            }
            else if(radioGroupPayment!!.checkedRadioButtonId==R.id.radio_other) cardPayment()
            else dialog.dismiss()
        }
    }

    private fun hasNetwork(): Boolean {
        val cm = context!!.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnected)
            return  true
        else
            return false

    }

    private fun checkInternet(){
        snackbar = Snackbar.make(myInflatedMapView!!,R.string.no_network,Snackbar.LENGTH_INDEFINITE)
        if(!hasNetwork()){
            snackbar!!.show()
        }
            snackbar?.dismiss()
    }

    private fun initialise(){
        // mCurrentLocation = myInflatedMapView.findViewById(R.id.current_location)
        mWhereTo = myInflatedMapView!!.findViewById(R.id.where_to_button)
        mMapView = myInflatedMapView!!.findViewById(R.id.mapView)
        etSearchLocation = myInflatedMapView!!.findViewById(R.id.search_text)
        callCabButton = myInflatedMapView!!.findViewById(R.id.call_cab)

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseWalletReference = mDatabase!!.reference!!.child("User Wallet")
        mDatabaseReference  = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()


        markerList = ArrayList(10)
        latitudeList = ArrayList(10)
        longitudeList = ArrayList(10)
        //searchAddress = myInflatedMapView.findViewById(R.id.search_address)

        cost = 20f

        radioGroupPayment = myInflatedMapView!!.findViewById(R.id.radio_payment)

        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mWhereTo!!.setOnClickListener {

            onSearch()
        }

        callCabButton!!.setOnClickListener{
            if(!isGPSEnabled())
                locationDialog()
            else{
                showDrivers()
                callCab()
            }
        }

        //getDestination()
        getLocationPermission()

        // onClickMap()

        //Show location enable dialog
        if(!isGPSEnabled())
            locationDialog()

        mMapView!!.getMapAsync(OnMapReadyCallback { mMap ->
            googleMap = mMap

            getLocationPermission()

            val amrita  = LatLng(9.0936997, 76.49149549999993)
            //googleMap!!.addMarker(MarkerOptions().position(amrita).title("Marker Title").snippet("Marker Description"))

            // For zooming automatically to the location of the marker
            val cameraPosition = CameraPosition.Builder().target(amrita).zoom(12f).build()
            googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

        })
    }

//    private fun hideKeyboard(view:View) {
//        val keyboard = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//        keyboard.hideSoftInputFromInputMethod(view.windowToken,)
//    }

}

