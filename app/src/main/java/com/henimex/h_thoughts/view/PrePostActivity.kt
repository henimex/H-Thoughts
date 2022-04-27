package com.henimex.h_thoughts.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.henimex.h_thoughts.R
import kotlinx.android.synthetic.main.activity_pre_post.*
import java.util.*

class PrePostActivity : AppCompatActivity() {

    val colName = "SharedPosts"
    val db = Firebase.firestore
    val storage = Firebase.storage
    private lateinit var auth: FirebaseAuth
    var selectedPicture: Uri? = null;
    var selectedBitmap: Bitmap? = null;
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

        if (selectedPicture != null){
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val reference = storage.reference
            val imageRef = reference.child("images").child(imageName)
            imageRef.putFile(selectedPicture!!).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val takenUri = uri.toString()
                    val postData = hashMapOf<String, Any>()
                    postData["thoughts"] = thoughts
                    postData["user"] = user
                    postData["post_date"] = postDate
                    postData["pictureUrl"] = takenUri

                    db.collection(colName).add(postData).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()
                            finish();
                        }
                    }.addOnFailureListener { exception ->
                        Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            val postData = hashMapOf<String, Any>()
            postData["thoughts"] = thoughts
            postData["user"] = user
            postData["post_date"] = postDate
            postData["pictureUrl"] = "noUri"

            db.collection(colName).add(postData).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Post Added", Toast.LENGTH_LONG).show()
                    finish();
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun saveImageToStorage() {
        if (selectedPicture != null){
            val uuid = UUID.randomUUID()
            val imageName = "${uuid}.jpg"
            val reference = storage.reference
            val imageRef = reference.child("images").child(imageName)
            imageRef.putFile(selectedPicture!!).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val takenUri = uri.toString()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    fun uploadImage(view: View) {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //request perm
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        } else {
            //perm granted go gallery
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, 2)
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            selectedPicture = data.data;

            if (selectedPicture != null) {
                if (Build.VERSION.SDK_INT >= 28) {
                    val source = ImageDecoder.createSource(this.contentResolver, selectedPicture!!)
                    selectedBitmap = ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(selectedBitmap)
                } else {
                    selectedBitmap =
                        MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
                    imageView.setImageBitmap(selectedBitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}