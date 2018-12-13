package com.example.s1604556.coinz.activitypage

import com.mapbox.mapboxsdk.geometry.LatLng
// Coin constructor, coin will have id, currency, value, position and colour stored in a coin object constructed
class Coin(val id: String = "", val currency:String = "", val value:String="", val position:LatLng=LatLng(), val colour: String ="")

