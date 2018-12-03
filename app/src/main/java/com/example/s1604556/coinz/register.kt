package com.example.s1604556.coinz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.view.View


class Register : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)
    }

    fun register(view: View) {

        val intent = Intent(this, Auth::class.java)
        startActivity(intent)

    }

}
