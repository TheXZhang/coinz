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
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.wallet.WalletObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
//the following code will be exactly the same as in all other adapter pages for bank, with very slight differences will not repeat unnecessary comment
//and the structure is mostly from https://developer.android.com/guide/topics/ui/layout/recyclerview and some youtube videos

class DollarAdapter(val coins: ArrayList<Coin>) :
        RecyclerView.Adapter<DollarAdapter.ViewHolder>() {


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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCoin = coins.get(position)

        holder.bindItems(coins[position])
        val button =holder.itemView.findViewById(R.id.toBank) as Button
        //if to bank button is pressed, move the coin to bank, see more detail in remove item function below
        button.setOnClickListener{
            button.isClickable=false
            Handler().postDelayed({
                button.isClickable=true
            },2000)
            //if 25 deposit limit is not met, allow user to bank the coin
            if(BankObject.depositedToday<25){
                removeitem(currentCoin)
                BankObject.depositedToday=BankObject.depositedToday+1
            }else{
                //otherwise alert user they have reached limit
                val context =holder.itemView.context
                Toast.makeText(context,"You have reached your 25 coins daily deposit limit", Toast.LENGTH_SHORT).show()
            }
        }

        val sendbutton =holder.itemView.findViewById(R.id.toFriend) as Button
        //when send button is pressed, coin is send to another user, provided that email field is filled
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
                // looping the datasnapshot from firebase to get target users uid from target use's email
                for (d in dataSnapshot.children){
                    if(d.child("Email").getValue(String::class.java)==email){
                        targetUid= d.ref.key!!
                        val limit=d.child("wallet").child("limit").getValue(Int::class.java)
                        val currentNo =d.child("wallet").child("currentNo").getValue(Int::class.java)
                        val playerReference = FirebaseDatabase.getInstance().reference.child("users").child(targetUid).child("wallet")
                        //once the target user is found, check if their wallet is full, if not full, remove this coin from current user and add it to the target user at firebase
                        if(currentNo!! < limit!!) {
                            playerReference.child("coinlist").child(coin.id).setValue(coin)
                            val position=coins.indexOf(coin)
                            coins.removeAt(position)
                            notifyItemRemoved(position)
                            WalletObject.wallet.coinlist.remove(coin)
                        }else{
                            //if targer user's wallet is full, display a message for current user
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
        //remove item from recyler view
        val position=coins.indexOf(coin)
        coins.removeAt(position)
        notifyItemRemoved(position)
        //remove coin from the wallet coinlist and update current no of coins in the wallet, add this coin to the bank coin list
        WalletObject.wallet.coinlist.remove(coin)
        WalletObject.wallet.currentNo= WalletObject.wallet.coinlist.size
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

