package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.FavoriteQuotesRepository
import com.google.firebase.auth.FirebaseAuth

class FavoriteFragment : Fragment() {

    private lateinit var favoriteRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView
    private lateinit var logoImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        favoriteRecyclerView = view.findViewById(R.id.favoriteRecyclerView)
        auth = FirebaseAuth.getInstance()
        userNameTextView = view.findViewById(R.id.userNameTextView)
        logoImageView = view.findViewById(R.id.logoImageView)

        // Display the user's name if signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userNameTextView.text = currentUser.displayName
            userNameTextView.visibility = View.VISIBLE
        } else {
            userNameTextView.visibility = View.GONE
        }

        // Set up the back arrow
        val backArrow: ImageButton = view.findViewById(R.id.backArrow)
        backArrow.setOnClickListener {
            findNavController().navigate(R.id.action_favoriteFragment_to_quoteFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val favoriteQuotes = FavoriteQuotesRepository.favoriteQuotes
        val adapter = FavoriteQuotesAdapter(favoriteQuotes) { favoriteQuote ->
            // Handle item click
            FavoriteQuotesRepository.removeFavoriteQuote(favoriteQuote)
            updateFavoriteQuotesList()
        }
        favoriteRecyclerView.adapter = adapter
        favoriteRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun updateFavoriteQuotesList() {
        val favoriteQuotes = FavoriteQuotesRepository.favoriteQuotes
        (favoriteRecyclerView.adapter as FavoriteQuotesAdapter).updateFavoriteQuotes(favoriteQuotes)
    }
}