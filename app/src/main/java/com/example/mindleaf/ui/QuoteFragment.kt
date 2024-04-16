package com.example.mindleaf.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mindleaf.R
import com.example.mindleaf.api.QuoteApi
import com.example.mindleaf.data.FavoriteQuotesRepository
import com.example.mindleaf.data.Quote
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class QuoteFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView
    private lateinit var favoriteButton: ImageButton
    private lateinit var quoteTextView: TextView
    private lateinit var authorTextView: TextView
    private var isFavorite = false
    private lateinit var backgroundImageView: ImageView
    private lateinit var dailyTipTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var signOutButton: Button


    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val backgroundImages = listOf(
        R.drawable.background_1,
        R.drawable.background_2,
        R.drawable.background_3,
        R.drawable.background_4,
        R.drawable.background_5,
        R.drawable.background_6,
        R.drawable.background_7
    )
    private val fontColors = listOf(
        R.color.font_color_for_background_1,
        R.color.font_color_for_background_2,
        R.color.font_color_for_background_3,
        R.color.font_color_for_background_4,
        R.color.font_color_for_background_5,
        R.color.font_color_for_background_6,
        R.color.font_color_for_background_7
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // ...
        FavoriteQuotesRepository.initialize(requireContext().applicationContext)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quote, container, false)

        auth = FirebaseAuth.getInstance()
        userNameTextView = view.findViewById(R.id.userNameTextView)
        favoriteButton = view.findViewById(R.id.favoriteButton)
        quoteTextView = view.findViewById(R.id.quoteTextView)
        authorTextView = view.findViewById(R.id.authorTextView)
        backgroundImageView = view.findViewById(R.id.backgroundImageView)
        dailyTipTextView = view.findViewById(R.id.dailyTipTextView)
        loginButton = view.findViewById(R.id.loginButton)
        signOutButton = view.findViewById(R.id.signOutButton)

        val backArrow: ImageButton = view.findViewById(R.id.backArrow)
        backArrow.visibility = View.GONE

        // Display the user's name if signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userNameTextView.text = currentUser.displayName
            userNameTextView.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        } else {
            userNameTextView.visibility = View.GONE
            signOutButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        }
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

        loginButton.setOnClickListener {
            // Navigate to the login screen
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }

        signOutButton.setOnClickListener {
            // Sign out the user
            signOut()
        }

        val favoriteListButton: Button = view.findViewById(R.id.favoriteListButton)
        favoriteListButton.setOnClickListener {
            try{
                findNavController().navigate(R.id.action_quoteFragment_to_favoriteFragment)
            } catch(e:Exception){
                Log.e("QuoteFragment", "Failed to navigate to Favorite Fragment", e);
            }
        }

        view.setOnClickListener {
            refreshQuote()
        }

        return view
    }

    private fun refreshQuote() {
        lifecycleScope.launch {
            val quote = fetchQuote()
            quoteTextView.text = quote.content
            authorTextView.text = quote.author

            val dailyTip = fetchDailyTip()
            dailyTipTextView.text = dailyTip

            val currentUser = auth.currentUser
            val userId = currentUser?.uid ?: ""

            // Check if the quote is a favorite and update the button state accordingly
            isFavorite = FavoriteQuotesRepository.isFavoriteQuote(quote.content, userId)
            updateFavoriteButtonState()

            // Set random background image and corresponding font color
            val randomIndex = (0 until backgroundImages.size).random()
            backgroundImageView.setImageResource(backgroundImages[randomIndex])
            quoteTextView.setTextColor(ContextCompat.getColor(requireContext(), fontColors[randomIndex]))
            authorTextView.setTextColor(ContextCompat.getColor(requireContext(), fontColors[randomIndex]))
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

    private suspend fun fetchDailyTip(): String {
        // Implement the logic to fetch the daily tip from an API or local data source
        // Return the fetched daily tip as a string
        // For example:
        return "Daily Tip: Start your day with a positive affirmation."
    }

    private fun toggleFavoriteStatus(quote: Quote) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid ?: ""

        val quoteContent = quote.content
        isFavorite = FavoriteQuotesRepository.isFavoriteQuote(quoteContent, userId)
        if (isFavorite) {
            FavoriteQuotesRepository.removeFavoriteQuote(quoteContent, userId)
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            FavoriteQuotesRepository.addFavoriteQuote(quoteContent, userId)
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
    private fun signOut() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_quoteFragment_to_welcomeFragment)
    }
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
            userNameTextView.text = user?.displayName
            userNameTextView.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        } else {
            // Sign in failed
            Toast.makeText(requireContext(), "Sign-in failed", Toast.LENGTH_SHORT).show()
        }
    }
}