package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val quote = quoteEditText.text.toString().trim()
            val author = authorEditText.text.toString().trim()

            if (quote.isNotEmpty() && author.isNotEmpty()) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                val authorName = currentUser?.displayName ?: author

                UserQuotesRepository.get().addUserQuote(Quote(quote, authorName))
                quoteEditText.text.clear()
                authorEditText.text.clear()
                Toast.makeText(requireContext(), "Quote posted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter a quote and author", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}