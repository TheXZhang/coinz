package com.example.s1604556.coinz.wallet

import com.example.s1604556.coinz.activitypage.Coin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mapbox.mapboxsdk.geometry.LatLng

object WalletObject {
    //radius for collecting is 25m
    val radius= 25
    var wallet= Wallet(coinlist = ArrayList(), limit = 0, currentNo = 0)
    private lateinit var walletReference : DatabaseReference
    private lateinit var coinIDToday : DatabaseReference
    var collectedID = ArrayList<String>()
    var Distance=0.0
    var claim1=false
    var claim2=false
    var claim3=false
    var claim4=false



    //coin collecting function
    fun collectingCoins(playerPosition:LatLng,coinList:ArrayList<Coin>): ArrayList<Coin> {
        val removelist = ArrayList<Coin>()

        //calculate all coins, if their are within the 25m radius of player, add to the remove list
        for (coin in coinList) {
            val distance = playerPosition.distanceTo(coin.position)
            if (distance<= radius)  {
                removelist.add(coin)
            }
        }

        //if wallet is not full, add this coin to wallet and update the coins list
        for (coin in removelist) {
            if (wallet.currentNo < wallet.limit) {
                coinList.remove(coin)
                wallet.coinlist.add(coin)
                wallet.currentNo = wallet.currentNo + 1

                //add this collected coin id to list
                collectedID.add(coin.id)

                //update firebase with these information
                val auth = FirebaseAuth.getInstance()
                walletReference = FirebaseDatabase.getInstance().reference
                        .child("users").child(auth.currentUser?.uid!!).child("wallet")
                walletReference.setValue(wallet)


                coinIDToday = FirebaseDatabase.getInstance().reference
                        .child("users").child(auth.currentUser?.uid!!).child("CoinCollectedToday")
                coinIDToday.setValue(collectedID)
            } else {
                return coinList
                //return a coinlist for updating map
            }

        }
        //return a coinlist for updating map
        return coinList
    }



}