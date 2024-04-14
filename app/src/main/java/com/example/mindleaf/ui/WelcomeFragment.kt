package com.example.mindleaf.ui

import android.app.Activity.RESULT_OK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindleaf.R
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class WelcomeFragment : Fragment() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_welcome, container, false)

        val signinButton: Button = view.findViewById(R.id.signinButton)
        val guestButton: Button = view.findViewById(R.id.guestButton)

        signinButton.setOnClickListener {
            // Launch the FirebaseUI sign-in flow
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build()
            signInLauncher.launch(signInIntent)
        }

        guestButton.setOnClickListener {
            // Navigate to the quote fragment as a guest
            findNavController().navigate(R.id.action_welcomeFragment_to_quoteFragment)
        }

        authStateListener = FirebaseAuth.AuthStateListener { auth ->
            val user = auth.currentUser
            if (user != null) {
                // User is signed in, navigate to QuoteFragment
                findNavController().navigate(R.id.action_welcomeFragment_to_quoteFragment)
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener)

        return view
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser
        } else {
            // Sign in failed
            if (result.idpResponse == null) {
                // User pressed back button
                Toast.makeText(context, "Sign-in cancelled.", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Sign-in failed.", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove AuthStateListener to avoid memory leaks
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener)
    }
}