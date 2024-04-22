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
    private lateinit var adapter: FavoriteQuotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FavoriteQuotesRepository.initialize(requireContext())
    }
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
        val userId = currentUser?.uid

        adapter = if (userId != null) {
            val favoriteQuotes = FavoriteQuotesRepository.getFavoriteQuotes(userId)
            FavoriteQuotesAdapter(favoriteQuotes.toMutableList()) { favoriteQuote ->
                FavoriteQuotesRepository.removeFavoriteQuote(favoriteQuote.content, favoriteQuote.author, userId)
                adapter.removeFavoriteQuote(favoriteQuote)
                updateFavoriteQuotesList(userId)
            }
        } else {
            FavoriteQuotesAdapter(mutableListOf()) { }
        }
        favoriteRecyclerView.adapter = adapter
        favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        if (userId != null) {
            updateFavoriteQuotesList(userId)
        }
    }
    private fun updateFavoriteQuotesList(userId: String) {
        val favoriteQuotes = FavoriteQuotesRepository.getFavoriteQuotes(userId)
        adapter.updateFavoriteQuotes(favoriteQuotes)
    }
}