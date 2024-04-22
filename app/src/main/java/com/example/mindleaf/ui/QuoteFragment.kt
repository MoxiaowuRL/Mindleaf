package com.example.mindleaf.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController

import com.example.mindleaf.R
import com.example.mindleaf.api.QuoteApi
import com.example.mindleaf.data.FavoriteQuotesRepository
import com.example.mindleaf.data.Quote
import com.example.mindleaf.data.UserQuotesRepository
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuoteFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView
    private lateinit var favoriteButton: ImageButton
    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private var isFavorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        FavoriteQuotesRepository.initialize(requireContext().applicationContext)
        UserQuotesRepository.initialize(requireContext().applicationContext)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quote, container, false)

        auth = FirebaseAuth.getInstance()

        favoriteButton = view.findViewById(R.id.favoriteButton)
        quoteTextView = view.findViewById(R.id.quoteTextView)
        authorTextView = view.findViewById(R.id.authorTextView)

        val currentUser = auth.currentUser
        refreshQuote()
        favoriteButton.setOnClickListener {
            if (currentUser != null) {
                // User is signed in, toggle the favorite status
                toggleFavoriteStatus(Quote(quoteTextView.text.toString(), authorTextView.text.toString()))
            } else {
                // Guest user, show a message to sign up first
                showSignUpMessage()
            }
        }

        view.setOnClickListener {
            refreshQuote()
        }

        return view
    }

    private fun refreshQuote() {
        lifecycleScope.launch {
            val userQuote = UserQuotesRepository.get().getRandomUserQuote()
            val apiQuote = fetchQuote()

            val quote = if (userQuote != null && Random.nextBoolean()) {
                userQuote
            } else {
                apiQuote
            }

            quoteTextView.text = quote.content
            authorTextView.text = quote.author

            val currentUser = auth.currentUser
            val userId = currentUser?.uid ?: ""

            isFavorite = FavoriteQuotesRepository.isFavoriteQuote(quote.content, userId)
            updateFavoriteButtonState()
        }
    }
    private suspend fun fetchQuote(): Quote {
        return try {
            QuoteApi.instance.getRandomQuote()
        } catch (e: Exception) {
            val backupQuotes = resources.getStringArray(R.array.backup_quotes)
            val randomQuote = backupQuotes.random()
            Quote(randomQuote, "Unknown")
        }
    }

    private fun toggleFavoriteStatus(quote: Quote) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""

        val quoteContent = quote.content
        isFavorite = FavoriteQuotesRepository.isFavoriteQuote(quoteContent, userId)
        if (isFavorite) {
            FavoriteQuotesRepository.removeFavoriteQuote(quoteContent, quote.author, userId)
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            FavoriteQuotesRepository.addFavoriteQuote(quote, userId)
            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
        }
        isFavorite = !isFavorite // Update the favorite status
        updateFavoriteButtonState() // Update the favorite button state
    }

    private fun updateFavoriteButtonState() {
        val favoriteIcon = if (isFavorite) R.drawable.ic_favorite_black_24dp else R.drawable.ic_favorite_border_black_24dp
        favoriteButton.setImageResource(favoriteIcon)
    }

    private fun showSignUpMessage() {
        // Show a message to the guest user to sign up first
        Toast.makeText(requireContext(), "Please sign in to save favorites", Toast.LENGTH_SHORT).show()
    }
    private fun showSignInMessage() {
        // Show a message to the user indicating they need to sign in
        Toast.makeText(requireContext(), "Please sign in to access favorites", Toast.LENGTH_SHORT).show()
    }

}