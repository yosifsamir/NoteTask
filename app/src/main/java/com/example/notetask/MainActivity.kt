package com.example.notetask

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.notetask.common.Constants
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val mAuth= FirebaseAuth.getInstance()
    private var mCurrentUser=mAuth.currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (mCurrentUser==null){
            mAuth.signInAnonymously().addOnCompleteListener({
                if (it.isSuccessful){
                    mCurrentUser=it.result!!.user
                    updateUI()
                }
            })
        }
        else{
            updateUI()
        }
    }

    fun updateUI(){
        Constants.userUid=mCurrentUser.uid
        Handler(Looper.getMainLooper()).postDelayed({
            var intent= Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        },2700)
    }
}