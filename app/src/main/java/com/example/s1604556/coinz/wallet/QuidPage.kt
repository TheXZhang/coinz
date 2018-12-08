package com.example.s1604556.coinz.wallet

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.walletadapters.QuidAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class QuidPage : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wallet_recycler_view)

        val showList = ArrayList<Coin>()

        viewManager = LinearLayoutManager(this)


        for (coin in WalletObject.wallet.coinlist){
            if (coin.currency=="QUID")
                showList.add(coin)
        }

        viewAdapter = QuidAdapter(showList)


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
    override fun onStop() {
        super.onStop()

        val auth = FirebaseAuth.getInstance()
        val walletReference = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("wallet")
        walletReference.setValue(WalletObject.wallet)
        val bankReference = FirebaseDatabase.getInstance().reference
                .child("users").child(auth.currentUser?.uid!!).child("bank")
        bankReference.setValue(BankObject.bank)



    }
}
