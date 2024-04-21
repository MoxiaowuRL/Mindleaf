package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mindleaf.R
import com.example.mindleaf.data.FavoriteQuotesRepository

class TopQuotesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var topQuotesAdapter: TopQuotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_top_quotes, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewTopQuotes)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateTopQuotesList()
    }

    private fun updateTopQuotesList() {
        val topQuotes = FavoriteQuotesRepository.getTopLikedQuotes()
        topQuotesAdapter = TopQuotesAdapter(topQuotes)
        recyclerView.adapter = topQuotesAdapter
    }
}

