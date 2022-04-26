package com.henimex.h_thoughts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_pre_post.*

class PrePostActivity : AppCompatActivity() {

    val colName = "SharedPosts"
    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth
    //private lateinit var rdb : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_post)

        auth = Firebase.auth
        //rdb = Firebase.firestore
    }

    fun postText(view: View) {
        val thoughts = txt_postText.text.toString();
        val user = auth.currentUser!!.displayName.toString();
        val postDate = Timestamp.now();

        val postData = hashMapOf<String, Any>()
        postData.put("thoughts", thoughts)
        postData.put("user", thoughts)
        postData.put("post_date", postDate)

        db.collection(colName).add(postData).addOnCompleteListener { task ->
            if (task.isSuccessful){
                finish();
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this,exception.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}