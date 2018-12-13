package com.example.s1604556.coinz.activitypage

import android.os.Bundle
import android.os.Handler
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
        //Displaying the current total distance travelled
        totaldistance.text = WalletObject.Distance.toString()

        //set on click listener for all buttons in this page
        oneKbutton.setOnClickListener(this)
        tenKbutton.setOnClickListener(this)
        onehkbutton.setOnClickListener(this)
        tenMbutton.setOnClickListener(this)

        //the buttons are set to not clickable at the start and
        //will check if user have travelled for required distances
        oneKbutton.isClickable=false
        tenKbutton.isClickable=false
        onehkbutton.isClickable=false
        tenMbutton.isClickable=false

        claimed1.visibility=View.GONE
        claimed2.visibility=View.GONE
        claimed3.visibility=View.GONE
        claimed4.visibility=View.GONE
    }

    override fun onClick (v : View){
        val i = v.id
        when (i){
            //on 1k gold button click, add gold to player bank, and set claimed reward to true
            //same applied to all the buttons with the corresponding values
            R.id.oneKbutton -> {
                oneKbutton.isClickable=false
                Handler().postDelayed({
                    oneKbutton.isClickable=true
                },2000)
                BankObject.bank.gold=BankObject.bank.gold+1000.0
                WalletObject.claim1=true
            }
            R.id.tenKbutton -> {
                tenKbutton.isClickable=false
                Handler().postDelayed({
                    tenKbutton.isClickable=true
                },2000)
                BankObject.bank.gold=BankObject.bank.gold+10000.0
                WalletObject.claim2=true
            }
            R.id.onehkbutton -> {
                onehkbutton.isClickable=false
                Handler().postDelayed({
                    onehkbutton.isClickable=true
                },2000)
                BankObject.bank.gold=BankObject.bank.gold+100000.0
                WalletObject.claim3=true
            }
            R.id.tenMbutton -> {
                tenMbutton.isClickable=false
                Handler().postDelayed({
                    tenMbutton.isClickable=true
                },2000)
                BankObject.bank.gold=BankObject.bank.gold+10000000.0
                WalletObject.claim4=true
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // checking distance travelled by player, if more than 1000, make 1k gold button clickable, and same for 5km, 10km, 100km
        if(WalletObject.Distance>=1000){
            oneKbutton.isClickable=true
        }
        if(WalletObject.Distance>=5000){
            tenKbutton.isClickable=true
        }
        if(WalletObject.Distance>=10000){
            onehkbutton.isClickable=true
        }
        if(WalletObject.Distance>=100000){
            tenMbutton.isClickable=true
        }

        //checking the claimed value in WalletObject, if reward already claimed. make button invisible
        //and display a text telling player its claimed.

        if(WalletObject.claim1){
            claimed1.visibility=View.VISIBLE
            oneKbutton.visibility=View.GONE
        }
        if(WalletObject.claim2){
            claimed2.visibility=View.VISIBLE
            tenKbutton.visibility=View.GONE
        }
        if(WalletObject.claim3){
            claimed3.visibility=View.VISIBLE
            onehkbutton.visibility=View.GONE
        }
        if(WalletObject.claim4){
            claimed4.visibility=View.VISIBLE
            tenMbutton.visibility=View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        //when exiting this activity, update the gold value, claimed status to firebase
        val auth= FirebaseAuth.getInstance()
        val goldref = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("bank").child("gold")
        goldref.setValue(BankObject.bank.gold)

        val claimed=FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("achievementClaimed")

        claimed.child("claim1").setValue(WalletObject.claim1)
        claimed.child("claim2").setValue(WalletObject.claim2)
        claimed.child("claim3").setValue(WalletObject.claim3)
        claimed.child("claim4").setValue(WalletObject.claim4)
    }

}