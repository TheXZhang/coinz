package com.example.s1604556.coinz.bankadpaters

import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R

//the following code will be exactly the same as in all other adapter pages for bank, with very slight differences will not repeat unnecessary comment
//and the structure is mostly from https://developer.android.com/guide/topics/ui/layout/recyclerview and some youtube videos
class BankDollarAdapter(val coins: ArrayList<Coin>) :
        RecyclerView.Adapter<BankDollarAdapter.ViewHolder>() {
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() : Int{
        return coins.size
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow=layoutInflater.inflate(R.layout.list_layout_bank, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCoin = coins.get(position)

        holder.bindItems(coins[position])
        val button =holder.itemView.findViewById(R.id.toGold) as Button
        //when togold button is pressed, remove this coin from the bank coinlist
        button.setOnClickListener{
            button.isClickable=false
            Handler().postDelayed({
                button.isClickable=true
            },2000)
            removeitem(currentCoin)
        }


    }

    private fun removeitem(coin: Coin) {
        //remove this item from recycler view
        val position=coins.indexOf(coin)
        coins.removeAt(position)
        notifyItemRemoved(position)
        //call the convert Gold funtion and remove coin from list
        BankObject.convertGold(coin)
        BankObject.bank.coinlist.remove(coin)


    }



    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bindItems(coin: Coin) {
            //display coin currency and value to designated field
            val currency = itemView.findViewById(R.id.textViewCurrency) as TextView
            val value = itemView.findViewById(R.id.textViewValue) as TextView
            currency.text = coin.currency
            value.text = coin.value


        }


    }


}

