package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.FavoriteQuote
import com.example.mindleaf.data.FavoriteQuotesRepository

class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FavoriteQuotesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerView = view.findViewById(R.id.favoriteRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = FavoriteQuotesAdapter(FavoriteQuotesRepository.favoriteQuotes)
        recyclerView.adapter = adapter

        return view
    }

    inner class FavoriteQuotesAdapter(private val favoriteQuotes: List<FavoriteQuote>) :
        RecyclerView.Adapter<FavoriteQuotesAdapter.ViewHolder>() {

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

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val quoteTextView: TextView = itemView.findViewById(R.id.quoteTextView)
            private val unfavoriteButton: Button = itemView.findViewById(R.id.unfavoriteButton)

            fun bind(favoriteQuote: FavoriteQuote) {
                quoteTextView.text = favoriteQuote.quote

                unfavoriteButton.setOnClickListener {
                    // Remove the quote from favorites
                    FavoriteQuotesRepository.removeFavoriteQuote(favoriteQuote)
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }
}