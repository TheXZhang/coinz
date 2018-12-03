package com.example.s1604556.coinz

import android.content.Context
import android.content.pm.FeatureGroupInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Icon
import android.location.Location
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.s1604556.coinz.DownloadCompleteRunner.result
//import com.example.s1604556.coinz.R.id.toolbar
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineListener
import com.mapbox.android.core.location.LocationEnginePriority
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.geojson.Feature
import com.mapbox.geojson.FeatureCollection
import com.mapbox.geojson.Point
import com.mapbox.mapboxsdk.Mapbox
import com.mapbox.mapboxsdk.Mapbox.getInstance
import com.mapbox.mapboxsdk.annotations.IconFactory
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraUpdate
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.maps.MapView
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback


import kotlinx.android.synthetic.main.activity_coinz.*

class coinz : AppCompatActivity(), OnMapReadyCallback, LocationEngineListener,PermissionsListener {

    private val tag = "MainActivity"
    private var mapView: MapView? = null
    private var map: MapboxMap? = null
    private var coinList=ArrayList<Coin>()

    private var downloadDate = "" // Format: YYYY/MM/DD
    private val preferencesFile = "MyPrefsFile" // for strong preferences

    private lateinit var originLocation : Location
    private lateinit var permissionsManager : PermissionsManager
    private lateinit var locationEngine : LocationEngine
    private lateinit var locationLayerPlugin : LocationLayerPlugin
    private var _initializing : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coinz)
        //setSupportActionBar(toolbar)

        getInstance(this,"pk.eyJ1IjoiczE2MDQ1NTYiLCJhIjoiY2pud2Y5ZXB4MDFhNDNxbjdueDg2YTFubCJ9.mDJ39QOCIA1DBnpFuVZC9A")

        mapView = findViewById(R.id.mapboxMapView)

        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync(this)

        DownloadFileTask(DownloadCompleteRunner).execute("http://homepages.inf.ed.ac.uk/stg/coinz/2018/10/03/coinzmap.geojson")



    }



    override fun onMapReady(mapboxMap: MapboxMap?){
        if (mapboxMap == null){
            Log.d(tag, "[onMapReady] mapboxMap is null")
        }else{
            map = mapboxMap
            //set user interface options
            map?.uiSettings?.isCompassEnabled = true
            map?.uiSettings?.isZoomControlsEnabled = true

            //make location info available
            enableLocation()
            val fc = FeatureCollection.fromJson(DownloadCompleteRunner.result)
            val feature =fc.features()
            var ic: com.mapbox.mapboxsdk.annotations.Icon

            for (item in feature!!){
                val p=item.geometry() as Point
                val c=LatLng(p.latitude(),p.longitude())
                val j=item.properties()
                val currency= j?.get("currency").toString().drop(1).dropLast(1)

                val id= j?.get("id").toString().drop(1).dropLast(1)
                val value=j?.get("value").toString().drop(1).dropLast(1)

                val markerColour=j?.get("marker-color").toString()


                ic = when (markerColour) {
                    "\"#008000\"" -> IconFactory.getInstance (this).fromResource(R.drawable.green)
                    "\"#ffdf00\"" -> IconFactory.getInstance (this).fromResource(R.drawable.yellow)
                    "\"#0000ff\"" -> IconFactory.getInstance (this).fromResource(R.drawable.blue)

                    else -> IconFactory.getInstance (this).fromResource(R.drawable.red)
                }

                map?.addMarker(MarkerOptions().position(c).title(id).snippet(currency+value).icon(ic))
                coinList.add(Coin(id,currency,value,c,markerColour))
            }


        }
    }




    private fun enableLocation(){
        if (PermissionsManager.areLocationPermissionsGranted(this)){
            Log.d(tag,"Permissions are granted")
            initialiseLocationEngine()
            initialiseLocationLayer()
        }else{
            Log.d(tag,"Permissions are not granted")
            permissionsManager = PermissionsManager(this)
            permissionsManager.requestLocationPermissions(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initialiseLocationEngine() {
        locationEngine = LocationEngineProvider(this)
                .obtainBestLocationEngineAvailable()
        locationEngine.apply {
            interval = 5000 //preferably every 5s
            fastestInterval = 1000 // at most every second
            priority = LocationEnginePriority.HIGH_ACCURACY
            activate()
        }
        val lastLocation = locationEngine.lastLocation
        if (lastLocation != null) {
            originLocation = lastLocation
            setCameraPosition(lastLocation)
        } else {
            locationEngine.addLocationEngineListener(this)
        }
    }

    @SuppressWarnings("MissingPermission")
    private fun initialiseLocationLayer() {
        if (mapView == null) {
            Log.d(tag, "mapView is null")
        } else {
            if (map == null) {
                Log.d(tag, "map is null")
            } else {
                locationLayerPlugin = LocationLayerPlugin(mapView!!, map!!,locationEngine)
                locationLayerPlugin.apply {
                    setLocationLayerEnabled(true)
                    cameraMode = CameraMode.TRACKING
                    renderMode = RenderMode.NORMAL
                }

            }

        }
    }

    private fun setCameraPosition(location: Location){
        val latLng = LatLng (location.latitude, location.longitude)
        map?.animateCamera(CameraUpdateFactory.newLatLng(latLng))
    }

    override fun onLocationChanged(location: Location?){
        if (location == null){
            Log.d(tag,"[onLocationChanged] location is null")
        }else{
            originLocation = location
            setCameraPosition(originLocation)
            _initializing= true                                     //making sure initialising is successful, so that button press does not crash the app
        }
    }

    @SuppressWarnings ("MissingPermission")
    override fun onConnected(){
        Log.d(tag,"[onConnected] requesting location updates")
        locationEngine.requestLocationUpdates()
    }

    override fun onExplanationNeeded(permissionsToExplain:
     MutableList<String>?){
        Log.d(tag,"Permissions: $permissionsToExplain")
        //present popup message or dialog
    }

    override fun onPermissionResult(granted: Boolean){
        Log.d(tag, "[onPermissionResult] granted == $granted")
        if (granted){
            enableLocation()
        }else{
            //open a dialogue with the user
        }
    }




    override  fun onStart(){

        super.onStart()
        mapView?.onStart()



        collect.setOnClickListener { view ->
            if (!_initializing) {
                Snackbar.make(view, "Please wait while locate your position", Snackbar.LENGTH_LONG).setAction("Action", null).show()
            }else{
                Snackbar.make(view, "Coins collected", Snackbar.LENGTH_LONG).setAction("Action", null).show()
                updateMap()
            }

        }


        //val settings = getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)

        //downloadDate = settings.getString("lastDownloadDate","")

        //Log.d(tag, "[onStart] recalled lastDownloadDate is '$downloadDate'")

    }

    private fun updateMap(){
        val playerposition=LatLng(originLocation.latitude,originLocation.longitude)
        val newlist=Collect.collectingCoins(playerposition,coinList)
        addmarker(newlist)
    }



    private fun addmarker(coins:ArrayList<Coin>){
        var ic: com.mapbox.mapboxsdk.annotations.Icon
        map?.clear()
        for (coin in coins){

            ic = when (coin.colour) {
                "\"#008000\"" -> IconFactory.getInstance (this).fromResource(R.drawable.green)
                "\"#ffdf00\"" -> IconFactory.getInstance (this).fromResource(R.drawable.yellow)
                "\"#0000ff\"" -> IconFactory.getInstance (this).fromResource(R.drawable.blue)

                else -> IconFactory.getInstance (this).fromResource(R.drawable.red)
            }

            map?.addMarker(MarkerOptions().position(coin.position).title(coin.id).snippet(coin.currency+coin.value).icon(ic))

        }
        coinList=coins
    }




   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_coinz, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}
