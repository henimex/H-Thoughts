package com.henimex.h_thoughts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
    }

    fun login(view: View) {
        val email = idText.text.toString();
        val password = pwText.text.toString();

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "Logged In" + user.toString(), Toast.LENGTH_SHORT)
                        .show();

                    val intent = Intent(this, UserPanel::class.java);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(baseContext, "Login Failed", Toast.LENGTH_SHORT).show();
                    Toast.makeText(baseContext, task.exception.toString(), Toast.LENGTH_LONG)
                        .show();
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_SHORT).show();}
    }

    fun register(view: View) {
        val email = idText.text.toString();
        val password = pwText.text.toString();

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser;
                    Toast.makeText(baseContext, "User Registration Completed", Toast.LENGTH_SHORT)
                        .show();

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

}