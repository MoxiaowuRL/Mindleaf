package com.example.mindleaf.ui

import FavoriteQuotesAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.FavoriteQuotesRepository
import com.google.firebase.auth.FirebaseAuth

class FavoriteFragment : Fragment() {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)
        auth = FirebaseAuth.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""
        lateinit var adapter: FavoriteQuotesAdapter
        val favoriteQuotes = FavoriteQuotesRepository.getFavoriteQuotes(userId)
        adapter = FavoriteQuotesAdapter(favoriteQuotes.toMutableList()) { favoriteQuote ->
            FavoriteQuotesRepository.removeFavoriteQuote(favoriteQuote.content, userId)
            adapter.removeFavoriteQuote(favoriteQuote)
            updateFavoriteQuotesList(userId)
        }
        favoriteRecyclerView.adapter = adapter
        favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateFavoriteQuotesList(userId: String) {
        val favoriteQuotes = FavoriteQuotesRepository.getFavoriteQuotes(userId)
        (favoriteRecyclerView.adapter as FavoriteQuotesAdapter).updateFavoriteQuotes(favoriteQuotes)
    }
}