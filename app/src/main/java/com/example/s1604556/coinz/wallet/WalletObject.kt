package com.example.s1604556.coinz.wallet

import com.example.s1604556.coinz.activitypage.Coin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mapbox.mapboxsdk.geometry.LatLng

object WalletObject {
    val radius= 6900
    var wallet= Wallet(coinlist = ArrayList(), limit = 0, currentNo = 0)
    private lateinit var walletReference : DatabaseReference
    private lateinit var coinIDToday : DatabaseReference
    var collectedID = ArrayList<String>()



    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val removelist = ArrayList<Coin>()

        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance<= radius)  {
                removelist.add(coin)
            }
        }


        for (coin in removelist) {
            if (wallet.currentNo < wallet.limit) {
                coinList.remove(coin)
                wallet.coinlist.add(coin)
                wallet.currentNo = wallet.currentNo + 1

                collectedID.add(coin.id)

                val auth = FirebaseAuth.getInstance()
                walletReference = FirebaseDatabase.getInstance().reference
                        .child("users").child(auth.currentUser?.uid!!).child("wallet")
                walletReference.setValue(wallet)


                coinIDToday = FirebaseDatabase.getInstance().reference
                        .child("users").child(auth.currentUser?.uid!!).child("CoinCollectedToday")
                coinIDToday.setValue(collectedID)
            } else {
                return ArrayList()
            }

        }

        return coinList
    }



}