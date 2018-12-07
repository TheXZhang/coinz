package com.example.s1604556.coinz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View

class WalletScreen : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_activity)
    }

    fun dollar(view: View){
        view.isClickable=false
        Handler().postDelayed({
            view.isClickable=true
        },2000)

        val intent = Intent(this, DollarPage::class.java)
        startActivity(intent)
    }

    fun peny(view: View){
        view.isClickable=false
        Handler().postDelayed({
            view.isClickable=true
        },2000)
        val intent = Intent(this, PennyPage::class.java)
        startActivity(intent)
    }

    fun shil(view: View){
        view.isClickable=false
        Handler().postDelayed({
            view.isClickable=true
        },2000)
        val intent = Intent(this, ShilPage::class.java)
        startActivity(intent)
    }

    fun quid(view: View){
        view.isClickable=false
        Handler().postDelayed({
            view.isClickable=true
        },2000)
        val intent = Intent(this, QuidPage::class.java)
        startActivity(intent)
    }

}