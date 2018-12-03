package com.example.s1604556.coinz

import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng

object Collect{
    val radius= 7000

    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val removelist = ArrayList<Coin>()

        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance<=radius) {
                removelist.add(coin)
            }
        }

        for (coin in removelist){
            coinList.remove(coin)
        }

        return coinList
    }

}