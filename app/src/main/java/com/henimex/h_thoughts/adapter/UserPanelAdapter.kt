package com.henimex.h_thoughts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.henimex.h_thoughts.R
import com.henimex.h_thoughts.model.PostModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

class UserPanelAdapter(val downloadedPostList: ArrayList<PostModel>) :
    RecyclerView.Adapter<UserPanelAdapter.PostHolder>() {
    class PostHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view = inflater.inflate(R.layout.recycler_row, parent, false);
        return PostHolder(view);
    }

    override fun onBindViewHolder(holder: PostHolder, position: Int) {
        holder.itemView.recycler_row_user_name.text = downloadedPostList[position].user;
        holder.itemView.recycler_row_post_msg.text = downloadedPostList[position].thoughts;

        if (downloadedPostList[position].pictureUrl != null){
            holder.itemView.rc_imageView.visibility = View.VISIBLE
            Picasso.get().load(downloadedPostList[position].pictureUrl).into(holder.itemView.rc_imageView)
            println(downloadedPostList[position].pictureUrl)
        }
    }

    override fun getItemCount(): Int {
        return downloadedPostList.size;
    }
}