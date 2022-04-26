package com.henimex.h_thoughts.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.henimex.h_thoughts.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val currentUser = auth.currentUser
        checkLoggedUser(currentUser)
    }

    private fun checkLoggedUser(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val intent = Intent(this, UserPanel::class.java);
            startActivity(intent)
            finish();
        }
    }

    fun register(view: View) {
        val email = txt_id.text.toString();
        val password = txt_pw.text.toString();
        val userName = txt_userName.text.toString();

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser;
                    Toast.makeText(baseContext, "User Registration Completed", Toast.LENGTH_SHORT)
                        .show();

                    val userProfile = userProfileChangeRequest {
                        displayName = userName;
                    }

                    user?.updateProfile(userProfile)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                baseContext,
                                "Display Name Updated " + user?.displayName,
                                Toast.LENGTH_SHORT
                            )
                                .show();
                        }
                    };

                    val intent = Intent(this, UserPanel::class.java);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(baseContext, "User Registration Failed", Toast.LENGTH_SHORT)
                        .show();
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG)
                        .show();
                }
            }
    }

    fun login(view: View) {
        val email = txt_id.text.toString();
        val password = txt_pw.text.toString();

        if (password != "" && email != "") {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(
                            baseContext,
                            "Logged In " + user?.email.toString(),
                            Toast.LENGTH_SHORT
                        ).show();

                        val intent = Intent(this, UserPanel::class.java);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show();
                        Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG)
                            .show();
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(
                        applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show();
                }
        }
    }
}