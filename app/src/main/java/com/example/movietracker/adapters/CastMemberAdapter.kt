package com.example.movietracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.movietracker.R
import com.example.movietracker.models.CastMember
import com.squareup.picasso.Picasso

class CastMemberAdapter(private val castList: List<CastMember>) :
    RecyclerView.Adapter<CastMemberAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_member, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.castMemberName.text = castMember.name
        Picasso.get().load(castMember.profilePath).into(holder.castMemberImage)
    }

    override fun getItemCount(): Int = castList.size

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val castMemberImage: ImageView = itemView.findViewById(R.id.castMemberImage)
        val castMemberName: TextView = itemView.findViewById(R.id.castMemberName)
    }


}
