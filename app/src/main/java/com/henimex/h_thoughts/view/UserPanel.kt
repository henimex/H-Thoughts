package com.henimex.h_thoughts.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.henimex.h_thoughts.R
import com.henimex.h_thoughts.adapter.UserPanelAdapter
import com.henimex.h_thoughts.model.PostModel
import kotlinx.android.synthetic.main.activity_user_panel.*

class UserPanel : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerViewAdapter: UserPanelAdapter
    val db = Firebase.firestore;
    val colName = "SharedPosts";
    var downloadedPostList = ArrayList<PostModel>();


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.exit) {
            auth.signOut();
            val intent = Intent(this, MainActivity::class.java);
            startActivity(intent);
            finish();
        } else if (item.itemId == R.id.create_post) {
            startActivity(Intent(this, PrePostActivity::class.java));
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_panel)

        auth = Firebase.auth;

        getPostData();
        val layoutManager = LinearLayoutManager(this);
        recyclerView.layoutManager = layoutManager;

        recyclerViewAdapter = UserPanelAdapter(downloadedPostList);
        recyclerView.adapter = recyclerViewAdapter;
    }


    private fun getPostData() {
        //db.collection(colName).whereEqualTo("thoughts", "First Post").addSnapshotListener

        db.collection(colName).orderBy("post_date",Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(this, error.localizedMessage, Toast.LENGTH_LONG).show();
            } else if (snapshot != null && !snapshot.isEmpty) {
                val docs = snapshot.documents

                downloadedPostList.clear();
                for (doc in docs) {
                    val uName = doc.get("user") as String
                    val post = doc.get("thoughts") as String
                    val pictureUrl = doc.get("pictureUrl") as String?

                    var downloadedPost = PostModel(uName, post, pictureUrl);
                    downloadedPostList.add(downloadedPost);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }
        }
    }
}