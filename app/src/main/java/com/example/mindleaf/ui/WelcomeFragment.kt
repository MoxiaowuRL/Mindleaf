package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindleaf.R

class WelcomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val getStartedButton: Button = view.findViewById(R.id.getStartedButton)
        getStartedButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_quoteFragment)
        }

        return view
    }
}