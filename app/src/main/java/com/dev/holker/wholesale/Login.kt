package com.dev.holker.wholesale

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.parse.ParseException
import com.parse.ParseUser
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {

    fun toast(string: String) {
        Toast.makeText(applicationContext, string, Toast.LENGTH_LONG).show()
    }

    fun signUp() {
        val user = ParseUser()
        user.username = et_username.text.toString()
        user.setPassword(et_password.text.toString())
        user.signUpInBackground {
            if (it == null) {
                toast("Successful")
            } else {
                toast(it.message.toString())
            }
        }
    }

    fun logIn() {
        ParseUser.logInInBackground(
            et_username.text.toString(),
            et_password.text.toString()
        ) { _: ParseUser?, e: ParseException? ->
            run {
                if (e == null) {
                    toast("Successful")
                    val intent = Intent(applicationContext, List::class.java)
                    startActivity(intent)
                } else {
                    toast(e.message.toString())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btn_login.setOnClickListener {
            logIn()
        }

        btn_signup.setOnClickListener {
            signUp()
        }

    }
}
