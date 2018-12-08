package com.example.s1604556.coinz.bank

import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.activitypage.Bank

object BankObject{
    var bank= Bank(coinlist = ArrayList(), gold = 0.0)
    var dollarRate  =0.0
    var shilRate  =0.0
    var penyRate =0.0
    var quidRate =0.0

    fun convertGold(coin: Coin){
        val currency = coin.currency
        when (currency){
            "SHIL" -> bank.gold= bank.gold + shilRate.times(coin.value.toDouble())
            "DOLR" -> bank.gold= bank.gold + dollarRate.times(coin.value.toDouble())
            "QUID" -> bank.gold= bank.gold + quidRate.times(coin.value.toDouble())
            "PENY" -> bank.gold= bank.gold + penyRate.times(coin.value.toDouble())
        }

    }

}