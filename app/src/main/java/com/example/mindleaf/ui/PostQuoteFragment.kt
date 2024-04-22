package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mindleaf.R
import com.example.mindleaf.data.Quote
import com.example.mindleaf.data.UserQuotesRepository
import com.google.firebase.auth.FirebaseAuth

class PostQuoteFragment : Fragment() {

    private lateinit var quoteEditText: EditText
    private lateinit var authorEditText: EditText
    private lateinit var postButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_quote, container, false)

        quoteEditText = view.findViewById(R.id.quoteEditText)
        authorEditText = view.findViewById(R.id.authorEditText)
        postButton = view.findViewById(R.id.postButton)

        postButton.setOnClickListener {
            hideKeyboard()
            val quoteText = quoteEditText.text.toString()
            val authorName = if(authorEditText.text.isNullOrEmpty()) {
                FirebaseAuth.getInstance().currentUser?.displayName ?: ""
            } else {
                authorEditText.text.toString()
            }

            if (FirebaseAuth.getInstance().currentUser != null) {
                // Post the quote with quoteText and authorName
                postQuote(quoteText, authorName)
            } else {
                // Prompt the user to log in
                Toast.makeText(context, "You must be logged in to post quotes.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }
    private fun postQuote(quoteText: String, authorName: String) {
        val quote = Quote(content = quoteText, author = authorName)
        val success = UserQuotesRepository.get().addUserQuote(quote)
        if (success) {
            onSuccess()
        } else {
            onError()
        }
    }

    private fun onSuccess() {
        Toast.makeText(context, "Quote posted successfully.", Toast.LENGTH_SHORT).show()
        quoteEditText.text.clear()
        authorEditText.text.clear()
    }

    private fun onError() {
        Toast.makeText(context, "Failed to post quote. Please try again.", Toast.LENGTH_SHORT).show()
    }
    private fun hideKeyboard() {
        val imm = context?.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}