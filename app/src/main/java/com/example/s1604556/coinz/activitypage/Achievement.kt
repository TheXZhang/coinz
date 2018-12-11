package com.example.s1604556.coinz.activitypage

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.wallet.WalletObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.achievement.*


class Achievement : AppCompatActivity(),View.OnClickListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.achievement)

        totaldistance.text = WalletObject.Distance.toString()

        oneKbutton.isClickable=false
        tenKbutton.isClickable=false
        onehkbutton.isClickable=false
        tenMbutton.isClickable=false

        oneKbutton.setOnClickListener(this)
        tenKbutton.setOnClickListener(this)
        onehkbutton.setOnClickListener(this)
        tenMbutton.setOnClickListener(this)

        if(WalletObject.Distance>=1000){
            oneKbutton.isClickable=true
        }
        if(WalletObject.Distance>=10000){
            tenKbutton.isClickable=true
        }
        if(WalletObject.Distance>=100000){
            onehkbutton.isClickable=true
        }
        if(WalletObject.Distance>=1000000){
            tenMbutton.isClickable=true
        }

    }

    override fun onClick (v : View){
        val i = v.id
        when (i){
            R.id.oneKbutton -> BankObject.bank.gold=BankObject.bank.gold+1000.0
            R.id.tenKbutton -> BankObject.bank.gold=BankObject.bank.gold+10000.0
            R.id.onehkbutton -> BankObject.bank.gold=BankObject.bank.gold+100000.0
            R.id.tenMbutton -> BankObject.bank.gold=BankObject.bank.gold+10000000.0
        }
    }

    override fun onStop() {
        super.onStop()
        val auth= FirebaseAuth.getInstance()
        val goldref = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("Bank").child("gold")
        goldref.setValue(BankObject.bank.gold)
    }

}