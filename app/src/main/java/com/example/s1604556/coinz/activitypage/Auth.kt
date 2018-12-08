package com.example.s1604556.coinz.activitypage

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.s1604556.coinz.R
import com.example.s1604556.coinz.bank.BankObject
import com.example.s1604556.coinz.wallet.WalletObject
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


import kotlinx.android.synthetic.main.auth_activity.*

class Auth : AppCompatActivity(), View.OnClickListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var walletReference : DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var coinIDToday : DatabaseReference
    private lateinit var bankReference : DatabaseReference
    private var coinIDlist=ArrayList<String>()
    private var loading=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.auth_activity)

        emailSignInButton.setOnClickListener(this)
        emailCreateAccountButton.setOnClickListener(this)
        signOutButton.setOnClickListener(this)
        entergame.setOnClickListener(this)

        auth= FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference



    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
        retrive()



    }
    // [END on_start_check_user]

    private fun createAccount(email: String, password: String) {
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            return
        }



        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(baseContext, "Account Created.",
                                Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        writeNewUser(user!!.uid,user.email)
                        updateUI(user)
                        retrive()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "This email is already used.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

                    // [START_EXCLUDE]

                    // [END_EXCLUDE]
                }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }



        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        Toast.makeText(baseContext, "You have signed in.",
                                Toast.LENGTH_SHORT).show()
                        val user = auth.currentUser
                        updateUI(user)
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "Wrong email or password.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                        retrive()
                    }

                    // [START_EXCLUDE]
                    if (!task.isSuccessful) {
                        status.text = "auth_failed"
                    }

                    // [END_EXCLUDE]
                }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        auth.signOut()
        updateUI(null)
    }


    private fun validateForm(): Boolean {
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fieldEmail.error = "email form xx@xx.com required."
            valid = false
        }else{
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        if (TextUtils.isEmpty(password) || (password.length<6)) {
            fieldPassword.error = "requires at least 6 characters."
            valid = false
        } else {
            fieldPassword.error = null
        }

        return valid
    }

    private fun updateUI(user: FirebaseUser?) {

        if (user != null) {
            status.text = user.email
            detail.text = user.uid

            emailSignInButton.visibility = View.GONE
            emailCreateAccountButton.visibility = View.GONE
            fieldEmail.visibility = View.GONE
            fieldPassword.visibility = View.GONE
            signedIn.visibility = View.VISIBLE
            signedIn2.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            entergame.visibility = View.VISIBLE

        } else {
            status.setText(R.string.signed_out)
            detail.text = null
            emailSignInButton.visibility = View.VISIBLE
            emailCreateAccountButton.visibility = View.VISIBLE
            fieldEmail.visibility = View.VISIBLE
            fieldPassword.visibility = View.VISIBLE

            signedIn.visibility = View.GONE
            signedIn2.visibility = View.GONE
            signOutButton.visibility = View.GONE
            entergame.visibility= View.GONE
        }
    }

    private fun writeNewUser(userId: String,email: String?) {
        databaseReference.child("users").child(userId).child("Email").setValue(email)
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            R.id.emailCreateAccountButton -> {
                emailCreateAccountButton.isClickable=false
                Handler().postDelayed({
                    emailCreateAccountButton.isClickable=true
                },2000)
                createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())}
            R.id.emailSignInButton -> {
                emailSignInButton.isClickable=false
                Handler().postDelayed({
                    emailSignInButton.isClickable=true
                },2000)
                signIn(fieldEmail.text.toString(), fieldPassword.text.toString())}
            R.id.signOutButton -> {
                signOutButton.isClickable=false
                Handler().postDelayed({
                    signOutButton.isClickable=true
                },2000)
                signOut()}
            R.id.entergame ->{
                entergame.isClickable=false
                Handler().postDelayed({
                    entergame.isClickable=true
                },2000)
                if (loading) {
                    val intent = Intent(this, coinz::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(baseContext, "Slow down, still waiting for player data, please try again after couple seconds",
                            Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    companion object {
        private const val TAG = "EmailPassword"
    }

    private fun retrive(){
        val user= auth.currentUser
        Log.d("testing","'$user'")
        if (user!=null){

            val walletListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    WalletObject.wallet.coinlist.clear()
                    WalletObject.wallet.currentNo=0
                    WalletObject.wallet.limit=150

                    for (dataSS in dataSnapshot.child("coinlist").children){
                        WalletObject.wallet.coinlist.add(dataSS.getValue(Coin::class.java)!!)

                    }
                    if(dataSnapshot.child("currentNo").getValue(Int::class.java)!=null){
                        WalletObject.wallet.currentNo=dataSnapshot.child("currentNo").getValue(Int::class.java)!!
                    }

                    if (dataSnapshot.child("limit").getValue(Int::class.java)!=null) {
                        WalletObject.wallet.limit = dataSnapshot.child("limit").getValue(Int::class.java)!!
                    }
                    Log.d("testing","${WalletObject.wallet}")
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }
            }


            val coinLeftListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    WalletObject.collectedID.clear()
                    for (d in dataSnapshot.children){
                        WalletObject.collectedID.add(d.getValue(String::class.java)!!)
                    }
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }

            }

            val bankListener=object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // Get Post object and use the values to update the UI
                    BankObject.bank.coinlist.clear()
                    BankObject.bank.gold=0.0
                    for (d in dataSnapshot.child("coinlist").children){
                        BankObject.bank.coinlist.add(d.getValue(Coin::class.java)!!)
                    }
                    if (dataSnapshot.child("gold").getValue(Int::class.java)!=null) {
                        BankObject.bank.gold = dataSnapshot.child("gold").getValue(Double::class.java)!!
                    }
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }

            }

            coinIDToday=databaseReference.child("users").child(user.uid).child("").child("CoinCollectedToday")
            coinIDToday.addValueEventListener(coinLeftListener)
            walletReference= databaseReference.child("users").child(user.uid).child("wallet")
            walletReference.addValueEventListener(walletListener)
            bankReference=databaseReference.child("users").child(user.uid).child("bank")
            bankReference.addValueEventListener(bankListener)


        }
    }





}




