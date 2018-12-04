package com.example.s1604556.coinz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class Auth : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)
    }

    fun register(view: View) {

        val intent = Intent(this, Register::class.java)
        startActivity(intent)

    }

    fun Login(view: View){

        val intent = Intent (this , coinz::class.java)
        startActivity(intent)
    }

}
