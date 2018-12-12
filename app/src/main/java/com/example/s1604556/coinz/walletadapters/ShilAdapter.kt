package com.example.s1604556.coinz.walletadapters

import android.content.Context
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.wallet.WalletObject
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ShilAdapter(val coins: ArrayList<Coin>) :
        RecyclerView.Adapter<ShilAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    override fun getItemCount() : Int{
        return coins.size
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow=layoutInflater.inflate(R.layout.list_layout, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cellForRow)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCoin = coins.get(position)
        holder.bindItems(coins[position])
        val button =holder.itemView.findViewById(R.id.toBank) as Button
        button.setOnClickListener{
            button.isClickable=false
            Handler().postDelayed({
                button.isClickable=true
            },2000)
            if(BankObject.depositedToday<25){
                removeitem(currentCoin)
                BankObject.depositedToday=BankObject.depositedToday+1
            }else{
                val context =holder.itemView.context
                Toast.makeText(context,"You have reached your 25 coins daily deposit limit", Toast.LENGTH_SHORT).show()
            }
        }
        val sendbutton =holder.itemView.findViewById(R.id.toFriend) as Button

        sendbutton.setOnClickListener{
            sendbutton.isClickable=false
            Handler().postDelayed({
                sendbutton.isClickable=true
            },2000)
            val targetEmail = holder.itemView.findViewById(R.id.targetEmail) as EditText
            Log.d("sometest","${targetEmail.text}")
            val context =holder.itemView.context
            sendcoin(currentCoin,targetEmail.text.toString(),context)

        }
    }
    private fun sendcoin(coin:Coin,email:String, context: Context){
        var targetUid: String


        val friendlistener=object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                for (d in dataSnapshot.children){
                    if(d.child("Email").getValue(String::class.java)==email){
                        targetUid= d.ref.key!!
                        val limit=d.child("wallet").child("limit").getValue(Int::class.java)
                        val currentNo =d.child("wallet").child("currentNo").getValue(Int::class.java)
                        val playerReference = FirebaseDatabase.getInstance().reference.child("users").child(targetUid).child("wallet")
                        if(currentNo!! < limit!!) {
                            playerReference.child("coinlist").child(coin.id).setValue(coin)
                            val position=coins.indexOf(coin)
                            coins.removeAt(position)
                            notifyItemRemoved(position)
                            WalletObject.wallet.coinlist.remove(coin)
                        }else{
                            Toast.makeText(context,"The player's wallet limit has reached", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        }

        FirebaseDatabase.getInstance().reference.child("users").addListenerForSingleValueEvent(friendlistener)



    }

    private fun removeitem(coin: Coin) {
        val position=coins.indexOf(coin)
        coins.removeAt(position)
        notifyItemRemoved(position)
        WalletObject.wallet.coinlist.remove(coin)
        BankObject.bank.coinlist.add(coin)

    }
    // Return the size of your dataset (invoked by the layout manager)


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(coin: Coin) {
            val currency = itemView.findViewById(R.id.textViewCurrency) as TextView
            val value = itemView.findViewById(R.id.textViewValue) as TextView
            currency.text = coin.currency
            value.text = coin.value
        }

    }


}

