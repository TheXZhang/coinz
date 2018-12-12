package com.example.s1604556.coinz.wallet

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.activitypage.Bank
import com.example.s1604556.coinz.bank.BankObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.wallet_activity.*

class WalletScreen : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_activity)
        curlimit.text = WalletObject.wallet.coinlist.size.toString()+"/"+WalletObject.wallet.limit.toString()
        depositLeft.text=(25-BankObject.depositedToday).toString()
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
        if (BankObject.bank.gold<1000){
            Toast.makeText(baseContext, "No enough Gold in you bank, make sure you have at least 1000 gold",
                    Toast.LENGTH_SHORT).show()
        }else {
            view.isClickable = false
            Handler().postDelayed({
                view.isClickable = true
            }, 2000)
            WalletObject.wallet.limit = WalletObject.wallet.limit + 10
            BankObject.bank.gold=BankObject.bank.gold-1000
            val auth = FirebaseAuth.getInstance()
            val limitRef = FirebaseDatabase.getInstance().reference
                    .child("users").child(auth.currentUser?.uid!!).child("wallet").child("limit")
            val goldRef = FirebaseDatabase.getInstance().reference
                    .child("users").child(auth.currentUser?.uid!!).child("bank").child("gold")
            limitRef.setValue(WalletObject.wallet.limit)
            goldRef.setValue(BankObject.bank.gold)
            curlimit.text = WalletObject.wallet.coinlist.size.toString() + "/" + WalletObject.wallet.limit.toString()
        }
    }

    override fun onResume() {
        super.onResume()
        depositLeft.text=(25-BankObject.depositedToday).toString()
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

}