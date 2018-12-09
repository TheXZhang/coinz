package com.example.s1604556.coinz.bankadpaters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.activitypage.Coin
import com.example.s1604556.coinz.R


class BankShilAdapter(val coins: ArrayList<Coin>) :
        RecyclerView.Adapter<BankShilAdapter.ViewHolder>() {
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
        val cellForRow=layoutInflater.inflate(R.layout.list_layout_bank, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCoin = coins.get(position)

        holder.bindItems(coins[position])
        val button =holder.itemView.findViewById(R.id.toGold) as Button
        button.setOnClickListener{
            removeitem(currentCoin)
        }


    }


    private fun removeitem(coin: Coin) {
        val position=coins.indexOf(coin)
        coins.removeAt(position)
        notifyItemRemoved(position)
        BankObject.convertGold(coin)
        BankObject.bank.coinlist.remove(coin)

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

