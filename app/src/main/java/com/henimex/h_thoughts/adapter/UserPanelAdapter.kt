package com.henimex.h_thoughts.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.henimex.h_thoughts.R
import com.henimex.h_thoughts.model.PostModel
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
    }

    override fun getItemCount(): Int {
        return downloadedPostList.size;
    }
}