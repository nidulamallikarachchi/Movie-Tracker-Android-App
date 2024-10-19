package com.example.movietracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.movietracker.R
import com.example.movietracker.models.CastMember

class CastMemberAdapter(private val castList: List<CastMember>) :
    RecyclerView.Adapter<CastMemberAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cast_member, parent, false)
        return CastViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castList[position]
        holder.castMemberName.text = castMember.name

        // Load image with Coil and apply rounded corners
        holder.castMemberImage.load(castMember.profilePath) {
            transformations(RoundedCornersTransformation(30f)) // Set the corner radius
        }
    }

    override fun getItemCount(): Int = castList.size

    class CastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val castMemberImage: ImageView = itemView.findViewById(R.id.castMemberImage)
        val castMemberName: TextView = itemView.findViewById(R.id.castMemberName)
    }
}
