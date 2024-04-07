package com.example.mindleaf.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mindleaf.R
import com.example.mindleaf.api.QuoteApi
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.Calendar

class QuoteFragment : Fragment() {
    private val backgroundImages = listOf(
        R.drawable.background_1,
        R.drawable.background_2,
        R.drawable.background_3,
        R.drawable.background_4,
        R.drawable.background_5,
        R.drawable.background_6,
        R.drawable.background_7,
    )
    private val fontColors = listOf(
        R.color.font_color_for_background_1,
        R.color.font_color_for_background_2,
        R.color.font_color_for_background_3,
        R.color.font_color_for_background_4,
        R.color.font_color_for_background_5,
        R.color.font_color_for_background_6,
        R.color.font_color_for_background_7,
        )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_quote, container, false)

        val quoteTextView: TextView = view.findViewById(R.id.quoteTextView)
        val dayTextView: TextView = view.findViewById(R.id.dayTextView)
        val dateTextView: TextView = view.findViewById(R.id.dateTextView)

        // Fetch a random quote from the API
        lifecycleScope.launch {
            try {
                val quote = QuoteApi.QuoteApiService.quoteApi.getRandomQuote()
                quoteTextView.text = quote.content

                // Set random background image and corresponding font color
                val randomIndex = (0 until backgroundImages.size).random()
                view.setBackgroundResource(backgroundImages[randomIndex])
                quoteTextView.setTextColor(ContextCompat.getColor(requireContext(), fontColors[randomIndex]))
                dayTextView.setTextColor(ContextCompat.getColor(requireContext(), fontColors[randomIndex]))
                dateTextView.setTextColor(ContextCompat.getColor(requireContext(), fontColors[randomIndex]))
            } catch (e: Exception) {
                // Handle any errors that occur during the API request
                val backupQuotes = resources.getStringArray(R.array.backup_quotes)
                val randomBackupQuote = backupQuotes.random()
                quoteTextView.text = randomBackupQuote
            }
        }

        val calendar = Calendar.getInstance()
        val dayFormat = SimpleDateFormat("EEEE", Locale.getDefault())
        val dateFormat = SimpleDateFormat("MMMM d", Locale.getDefault())
        val currentDay = dayFormat.format(calendar.time)
        val currentDate = dateFormat.format(calendar.time)
        dayTextView.text = currentDay
        dateTextView.text = currentDate

        return view
    }
}