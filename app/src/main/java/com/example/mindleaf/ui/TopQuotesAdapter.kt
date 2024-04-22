package com.example.mindleaf.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.Quote

class TopQuotesAdapter(private val quotes: List<Quote>) :
    RecyclerView.Adapter<TopQuotesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_top_quote, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(quotes[position])
    }

    override fun getItemCount(): Int = quotes.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
        private val authorTextView: TextView = itemView.findViewById(R.id.authorTextView)
        private val favoriteCountsTextView: TextView = itemView.findViewById(R.id.favoriteCounts)


        fun bind(quote: Quote) {
            quoteTextView.text = quote.content
            authorTextView.text = quote.author
            favoriteCountsTextView.text = quote.likesCount.toString()
        }
    }
}
