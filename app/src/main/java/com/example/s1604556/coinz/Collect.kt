package com.example.s1604556.coinz

import com.mapbox.mapboxsdk.geometry.LatLng

object Collect{
    const val radius= 1000.0

    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val newcoinList= coinList
        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance <= radius) {
                newcoinList.remove(coin)
            }
        }
        return newcoinList
    }

}