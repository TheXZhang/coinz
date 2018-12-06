package com.example.s1604556.coinz

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class QuidAdapter(val coins: ArrayList<Coin>) :
        RecyclerView.Adapter<QuidAdapter.ViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just a string in this case that is shown in a TextView.

    override fun getItemCount() : Int{
        return coins.size
    }


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuidAdapter.ViewHolder {
        // create a new view
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow=layoutInflater.inflate(R.layout.list_layout, parent, false)
        // set the view's size, margins, paddings and layout parameters
        return ViewHolder(cellForRow)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.bindItems(coins[position])

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