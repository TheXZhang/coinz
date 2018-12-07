package com.example.s1604556.coinz

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


import kotlinx.android.synthetic.main.auth_activity.*
import java.time.LocalDate

class Auth : AppCompatActivity(), View.OnClickListener{

    private lateinit var auth: FirebaseAuth
    private lateinit var walletReference : DatabaseReference
    private lateinit var databaseReference: DatabaseReference

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
            R.id.emailCreateAccountButton -> createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.emailSignInButton -> signIn(fieldEmail.text.toString(), fieldPassword.text.toString())
            R.id.signOutButton -> signOut()
            R.id.entergame ->{
                val intent = Intent(this, coinz::class.java)
                startActivity(intent)
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

                    for (dataSS in dataSnapshot.children){
                        WalletObject.wallet.coinlist.add(dataSS.getValue(Coin::class.java)!!)

                    }
                    Log.d("testing","{${WalletObject.wallet}}")
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }
            }

            walletReference= databaseReference.child("users").child(user.uid).child("wallet").child("coinlist")
            walletReference.addValueEventListener(walletListener)
        }
    }





}




