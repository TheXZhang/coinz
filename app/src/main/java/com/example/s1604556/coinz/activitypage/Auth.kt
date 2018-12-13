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
import java.time.LocalTime

class Auth : AppCompatActivity(), View.OnClickListener{
    //The structure for this class is from Firebase doc
    // https://firebase.google.com/docs/auth/android/password-auth

    private lateinit var auth: FirebaseAuth
    private lateinit var walletReference : DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var coinIDToday : DatabaseReference
    private lateinit var bankReference : DatabaseReference
    private lateinit var distanceTravelled: DatabaseReference
    private lateinit var rewardClaimed: DatabaseReference
    private var coinIDlist=ArrayList<String>()
    //create a variable to checking if player data is loaded properly
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
        //on start call the retrieve function, which will retrieve all data from firebase for current user
        retrieve()



    }

    //creating account for login
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
                        //write new user info to firebase, setting up their profile for storing data in the future
                        writeNewUser(user!!.uid,user.email)
                        updateUI(user)
                        //this step is to ensure data is retrieved, in this case creating a new account will set all variable used in the app to default status
                        retrieve()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(baseContext, "This email is already used.",
                                Toast.LENGTH_SHORT).show()
                        updateUI(null)
                    }

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
                        //this step is to ensure data is retrieved, in case player log out and login again
                        retrieve()
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
        //validate if input meet the requirement
        var valid = true

        val email = fieldEmail.text.toString()
        if (TextUtils.isEmpty(email)) {
            fieldEmail.error = "Required."
            valid = false
            //check if input in the email field is in the email format
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            fieldEmail.error = "email form xx@xx.com required."
            valid = false
        }else{
            fieldEmail.error = null
        }

        val password = fieldPassword.text.toString()
        //check if password is not empty and longer than 6
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
            // if user is logged in, make signin button, register button and field for inputting email, passwords invisible
            // then make signout, current user info and enter game button visible

            emailSignInButton.visibility = View.GONE
            emailCreateAccountButton.visibility = View.GONE
            fieldEmail.visibility = View.GONE
            fieldPassword.visibility = View.GONE
            signedIn.visibility = View.VISIBLE
            signedIn2.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            entergame.visibility = View.VISIBLE

        } else {
            // the opposite happens if no user is logged in
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
        // write user to firebase with their uid as root parent
        databaseReference.child("users").child(userId).child("Email").setValue(email)
    }

    override fun onClick(v: View) {
        val i = v.id
        when (i) {
            //when create button is clicked, run corresponding function for creating account
            R.id.emailCreateAccountButton -> {
                emailCreateAccountButton.isClickable=false
                Handler().postDelayed({
                    emailCreateAccountButton.isClickable=true
                },2000)
                createAccount(fieldEmail.text.toString(), fieldPassword.text.toString())}
            //when create button is clicked, run corresponding function for sign in
            R.id.emailSignInButton -> {
                emailSignInButton.isClickable=false
                Handler().postDelayed({
                    emailSignInButton.isClickable=true
                },2000)
                signIn(fieldEmail.text.toString(), fieldPassword.text.toString())}
            //when create button is clicked, run corresponding function for sign out
            R.id.signOutButton -> {
                signOutButton.isClickable=false
                Handler().postDelayed({
                    signOutButton.isClickable=true
                },2000)
                signOut()}
            //when enter game is clicked, enter to the main page of the coinz game, the map view page
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

    private fun retrieve(){
        val user= auth.currentUser
        Log.d("testing","'$user'")
        if (user!=null){

            val walletListener = object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // always clear data in Wallet Object to default status before assigning current player data to the wallet Object
                    WalletObject.wallet.coinlist.clear()
                    WalletObject.wallet.currentNo=0
                    WalletObject.wallet.limit=150

                    // then in the next few lines, get data from firebase and assign value to variable in the WalletObject
                    for (dataSS in dataSnapshot.child("coinlist").children){
                        WalletObject.wallet.coinlist.add(dataSS.getValue(Coin::class.java)!!)

                    }
                    if(dataSnapshot.child("currentNo").getValue(Int::class.java)!=null){
                        WalletObject.wallet.currentNo=WalletObject.wallet.coinlist.size
                    }

                    if (dataSnapshot.child("limit").getValue(Int::class.java)!=null) {
                        WalletObject.wallet.limit = dataSnapshot.child("limit").getValue(Int::class.java)!!
                    }
                    Log.d("testing","${WalletObject.wallet}")
                    //set loading data to true as this process is complete
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
                    //again, clear the variable before assigning value
                    WalletObject.collectedID.clear()
                    for (d in dataSnapshot.children){
                        //retrieve collected coin id information so collected coins will not be displayed on map
                        WalletObject.collectedID.add(d.getValue(String::class.java)!!)
                    }
                    //set loading data to true as this process is complete
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
                    //clear variables
                    BankObject.bank.coinlist.clear()
                    BankObject.bank.gold=0.0
                    BankObject.depositedToday=0
                    //assign values from firebase
                    for (d in dataSnapshot.child("coinlist").children){
                        BankObject.bank.coinlist.add(d.getValue(Coin::class.java)!!)
                    }
                    if (dataSnapshot.child("gold").getValue(Int::class.java)!=null) {
                        BankObject.bank.gold = dataSnapshot.child("gold").getValue(Double::class.java)!!
                    }

                    if(dataSnapshot.child("dailyDeposit").getValue(Int::class.java)!=null){
                        val current = LocalTime.now()
                        //if current hour and minute are 0, meaning its midnight, we reset deposited coin number to 0 as required in the spec
                        if(current.hour==0 && current.minute==0){
                            BankObject.depositedToday=0
                        }else {
                            BankObject.depositedToday = dataSnapshot.child("dailyDeposit").getValue(Int::class.java)!!
                        }
                    }
                    //set loading data to true as this process is complete
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }

            }

            val distanceListener=object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    //reset variable
                    WalletObject.Distance=0.0
                    //assign values from firebase
                    if (dataSnapshot.getValue(Double::class.java)!=null) {
                        WalletObject.Distance = dataSnapshot.getValue(Double::class.java)!!
                    }
                    //set loading data to true as this process is complete
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }

            }

            val rewardListener=object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // reset variables
                    WalletObject.claim1=false
                    WalletObject.claim2=false
                    WalletObject.claim3=false
                    WalletObject.claim4=false
                    //assign values from firebase
                    if(dataSnapshot.child("claim1").getValue(Boolean::class.java)!=null) {
                        WalletObject.claim1 = dataSnapshot.child("claim1").getValue(Boolean::class.java)!!
                    }
                    if(dataSnapshot.child("claim2").getValue(Boolean::class.java)!=null) {
                        WalletObject.claim2 = dataSnapshot.child("claim2").getValue(Boolean::class.java)!!
                    }

                    if(dataSnapshot.child("claim3").getValue(Boolean::class.java)!=null) {
                        WalletObject.claim3 = dataSnapshot.child("claim3").getValue(Boolean::class.java)!!
                    }

                    if(dataSnapshot.child("claim4").getValue(Boolean::class.java)!=null) {
                        WalletObject.claim4 = dataSnapshot.child("claim4").getValue(Boolean::class.java)!!
                    }
                    //set loading data to true as this process is complete
                    loading = true
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Getting Post failed, log a message
                    Toast.makeText(baseContext, "Failed to load data.",
                            Toast.LENGTH_SHORT).show()
                }

            }

            //before retrieving data from firebase, set correct reference to each variable and call corrcet listeners

            coinIDToday=databaseReference.child("users").child(user.uid).child("CoinCollectedToday")
            coinIDToday.addValueEventListener(coinLeftListener)

            walletReference= databaseReference.child("users").child(user.uid).child("wallet")
            walletReference.addValueEventListener(walletListener)

            bankReference=databaseReference.child("users").child(user.uid).child("bank")
            bankReference.addValueEventListener(bankListener)

            distanceTravelled=databaseReference.child("users").child(user.uid).child("distanceTravelled")
            distanceTravelled.addValueEventListener(distanceListener)

            rewardClaimed=databaseReference.child("users").child(user.uid).child("achievementClaimed")
            rewardClaimed.addValueEventListener(rewardListener)


        }
    }





}




