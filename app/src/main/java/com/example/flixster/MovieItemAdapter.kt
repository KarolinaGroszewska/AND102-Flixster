package com.example.flixster

// ArtworkAdapter.kt
import com.squareup.picasso.Picasso
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ArtworkAdapter(private val artworkList: List<MovieItem>) :
    RecyclerView.Adapter<ArtworkAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val artwork = artworkList[position]
        holder.bind(artwork)
    }

    override fun getItemCount(): Int {
        return artworkList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val description: TextView = itemView.findViewById(R.id.description)
        private val image: ImageView = itemView.findViewById(R.id.image)

        fun bind(movie: MovieItem) {
            name.text = movie.name
            description.text = movie.description
            val imageUrl = "https://image.tmdb.org/t/p/w500" + movie.image
            Picasso.get().load(imageUrl).into(image)
        }
    }
}