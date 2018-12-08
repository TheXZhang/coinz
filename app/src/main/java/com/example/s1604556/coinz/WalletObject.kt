package com.example.s1604556.coinz

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mapbox.mapboxsdk.geometry.LatLng

object WalletObject {
    val radius= 7100
    var wallet=Wallet(coinlist = ArrayList(),limit=1000,currentNo = 0)
    private lateinit var walletReference : DatabaseReference
    private lateinit var coinIDToday : DatabaseReference
    var collectedID = ArrayList<String>()



    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val removelist = ArrayList<Coin>()

        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance<=radius)  {
                removelist.add(coin)

                collectedID.add(coin.id)
            }
        }

        if(wallet.currentNo< wallet.limit) {
            for (coin in removelist) {
                coinList.remove(coin)
                wallet.addcoin(coin)
            }
        }
        val auth= FirebaseAuth.getInstance()

        walletReference = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("wallet")
        walletReference.setValue(wallet)


        coinIDToday= FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("CoinCollectedToday")
        coinIDToday.setValue(collectedID)



        return coinList
    }



}