package com.example.s1604556.coinz

data class User(
        var email: String? = "",
        var coinLeftToday: ArrayList<Coin> = ArrayList()

)