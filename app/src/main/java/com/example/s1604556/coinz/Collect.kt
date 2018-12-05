package com.example.s1604556.coinz

import android.util.Log
import com.mapbox.mapboxsdk.geometry.LatLng

object Collect{
    val radius= 10000
    var walletCoins= ArrayList<Coin>()
    var wallet=Wallet(walletCoins,limit=1000,currentNo = 0)

    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val removelist = ArrayList<Coin>()

        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance<=radius)  {
                removelist.add(coin)
            }
        }

        if(wallet.currentNo< wallet.limit) {
            for (coin in removelist) {
                coinList.remove(coin)
                wallet.addcoin(coin)
            }
        }else {
            return coinList
        }

        return coinList
    }


}