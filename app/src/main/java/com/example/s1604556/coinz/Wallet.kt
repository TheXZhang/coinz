package com.example.s1604556.coinz



class Wallet(var coinlist: ArrayList<Coin>, var limit: Int, var currentNo: Int){

    fun addcoin(coin:Coin){
        coinlist.add(coin)
        currentNo+=1
    }


}