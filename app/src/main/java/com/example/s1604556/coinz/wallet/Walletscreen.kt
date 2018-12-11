package com.example.s1604556.coinz.wallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.s1604556.coinz.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.wallet_activity.*

class WalletScreen : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_activity)
        curlimit.text = WalletObject.wallet.coinlist.size.toString()+"/"+WalletObject.wallet.limit.toString()
        var shilNo=0
        var dolarNo=0
        var quidNo=0
        var penyNo=0

        for (coin in WalletObject.wallet.coinlist){
            when (coin.currency){
                "SHIL" -> shilNo=shilNo+1
                "QUID" -> quidNo=quidNo+1
                "PENY" -> penyNo=penyNo+1
                "DOLR" -> dolarNo=dolarNo+1
            }
        }
        totalD.text=dolarNo.toString()
        totalQ.text=quidNo.toString()
        totalS.text=shilNo.toString()
        totalP.text=penyNo.toString()


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

    fun upgradeLimit(view: View){
        view.isClickable=false
        Handler().postDelayed({
            view.isClickable=true
        },2000)
        WalletObject.wallet.limit=WalletObject.wallet.limit+10
        val auth = FirebaseAuth.getInstance()
        val limitRef = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("wallet").child("limit")
        limitRef.setValue(WalletObject.wallet.limit)
        curlimit.text = WalletObject.wallet.coinlist.size.toString()+"/"+WalletObject.wallet.limit.toString()
    }

}