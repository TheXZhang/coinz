package com.example.s1604556.coinz.wallet

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.os.Bundle
import com.example.s1604556.coinz.R

class WalletDialog : DialogFragment(){

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(layoutInflater.inflate(R.layout.dialog_signin, null))
                    // Add action buttons
                    .setPositiveButton("Send",
                            DialogInterface.OnClickListener { dialog, id ->
                                // sign in the user ...
                            })
                    .setNegativeButton("Cancle",
                            DialogInterface.OnClickListener { dialog, id ->
                                getDialog().cancel()
                            })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}