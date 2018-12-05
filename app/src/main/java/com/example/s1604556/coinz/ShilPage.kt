package com.example.s1604556.coinz

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

class ShilPage : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_recycler_view)

        val showList = ArrayList<Coin>()

        viewManager = LinearLayoutManager(this)


        for (coin in Collect.wallet.coinlist){
            if (coin.currency=="SHIL")
                showList.add(coin)
        }

        viewAdapter = ShilAdapter(showList)


        recyclerView = findViewById<RecyclerView>(R.id.wallet_recycler_view).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter

        }
    }
}