package com.example.s1604556.coinz



class Wallet(val coinlist: ArrayList<Coin>, val limit: Int, var currentNo: Int){

    fun addcoin(coin:Coin){
        coinlist.add(coin)
        currentNo+=1
    }


}