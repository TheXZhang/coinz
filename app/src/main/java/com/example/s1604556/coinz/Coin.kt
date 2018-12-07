package com.example.s1604556.coinz

import com.mapbox.mapboxsdk.geometry.LatLng

class Coin(val id: String = "", val currency:String = "", val value:String="", val position:LatLng=LatLng(), val colour: String ="")

