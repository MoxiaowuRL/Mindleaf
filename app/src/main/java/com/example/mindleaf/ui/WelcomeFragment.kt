package com.example.mindleaf.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindleaf.R
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        auth = FirebaseAuth.getInstance()

        val signinButton: Button = view.findViewById(R.id.signinButton)
        val guestButton: Button = view.findViewById(R.id.guestButton)

        signinButton.setOnClickListener {
            // Navigate to the signin fragment
            findNavController().navigate(R.id.action_welcomeFragment_to_signinFragment)
        }

        guestButton.setOnClickListener {
            // Navigate to the quote fragment as a guest
            findNavController().navigate(R.id.action_welcomeFragment_to_quoteFragment)
        }

        return view
    }
}