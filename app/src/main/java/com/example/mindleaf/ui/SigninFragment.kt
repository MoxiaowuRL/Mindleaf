package com.example.mindleaf.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.mindleaf.R
import com.google.firebase.auth.FirebaseAuth

class SigninFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_signin, container, false)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = view.findViewById(R.id.emailEditText)
        val passwordEditText: EditText = view.findViewById(R.id.passwordEditText)
        val signinButton: Button = view.findViewById(R.id.signinButton)
        val signupButton: Button = view.findViewById(R.id.signupButton)

        signinButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, navigate to the quote fragment
                        findNavController().navigate(R.id.action_signinFragment_to_quoteFragment)
                    } else {
                        // Sign in failed, display an error message
                        Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Check if the email and password fields are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Attempt to create a new user with the provided email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-in user's information
                            Log.d("SignUp", "createUserWithEmail:success")
                            val user = auth.currentUser
                            // Navigate to the QuoteFragment
                            findNavController().navigate(R.id.action_signinFragment_to_quoteFragment)
                        } else {
                            // If sign up fails, display a message to the user.
                            Log.w("SignUp", "createUserWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Authentication failed: ${task.exception?.message}",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Prompt the user to fill in both email and password fields
                Toast.makeText(requireContext(), "Please enter both email and password.", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}