package com.example.mindleaf.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R

class FavoriteQuotesAdapter(
    private var favoriteQuotes: List<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteQuote = favoriteQuotes[position]
        holder.bind(favoriteQuote)
    }

    override fun getItemCount(): Int {
        return favoriteQuotes.size
    }

    fun updateFavoriteQuotes(favoriteQuotes: List<String>) {
        this.favoriteQuotes = favoriteQuotes
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val removeFavoriteButton: ImageButton = itemView.findViewById(R.id.removeFavoriteButton)

        init {
            removeFavoriteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val favoriteQuote = favoriteQuotes[position]
                    onItemClick(favoriteQuote)
                }
            }
        }

        fun bind(favoriteQuote: String) {
            quoteTextView.text = favoriteQuote
        }
    }
}