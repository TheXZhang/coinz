package com.example.s1604556.coinz

import com.example.s1604556.coinz.activitypage.Coin

data class User(
        var email: String? = "",
        var coinLeftToday: ArrayList<Coin> = ArrayList()

)