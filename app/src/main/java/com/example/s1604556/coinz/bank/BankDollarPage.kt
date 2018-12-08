package com.example.s1604556.coinz.bank

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.bankadpaters.BankDollarAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.bank_recycler_view.*


class BankDollarPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bank_recycler_view)

        whichrate.text = "The Dollar rate today is"
        whatrate.text="1 Dollar to "+BankObject.dollarRate.toString()+" Gold"

        val showList = ArrayList<Coin>()

        viewManager = LinearLayoutManager(this)


        for (coin in BankObject.bank.coinlist){
            if (coin.currency=="DOLR")
                showList.add(coin)
        }

        viewAdapter = BankDollarAdapter(showList)


        recyclerView = findViewById<RecyclerView>(R.id.bankrecyclerView).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }


    override fun onStop() {
        super.onStop()
        val auth = FirebaseAuth.getInstance()
        val bankReference = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("bank")
        bankReference.setValue(BankObject.bank)
    }
}