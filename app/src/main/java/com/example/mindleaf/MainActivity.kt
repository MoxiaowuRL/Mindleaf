package com.example.mindleaf

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.FirebaseUiException
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var userNameTextView: TextView
    private lateinit var loginButton: Button
    private lateinit var signOutButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var bottomNavigationView: BottomNavigationView

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        navController = findNavController(R.id.nav_host_fragment)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Inflate the toolbar layout and set it as the action bar
        toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        auth = FirebaseAuth.getInstance()
        userNameTextView = toolbar.findViewById(R.id.userNameTextView)
        loginButton = toolbar.findViewById(R.id.loginButton)
        signOutButton = toolbar.findViewById(R.id.signOutButton)

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

        loginButton.setOnClickListener {
            val providers = arrayListOf(AuthUI.IdpConfig.EmailBuilder().build())
            val signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setIsSmartLockEnabled(false)
                .build()
            signInLauncher.launch(signInIntent)
        }

        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            updateUI(user)
        }
        signOutButton.setOnClickListener {
            signOut()
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            updateUI(auth.currentUser)
        }
    }
    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            // User is signed in
            userNameTextView.text = user.displayName
            Log.d("MainActivity", "User displayName: ${user.displayName}")
            userNameTextView.visibility = View.VISIBLE
            signOutButton.visibility = View.VISIBLE
            loginButton.visibility = View.GONE
        } else {
            // User is signed out
            userNameTextView.visibility = View.GONE
            signOutButton.visibility = View.GONE
            loginButton.visibility = View.VISIBLE
        }
        val currentDestination = navController.currentDestination
        if (currentDestination?.id == R.id.welcomeFragment) {
            toolbar.visibility = View.GONE
            bottomNavigationView.visibility = View.GONE
        } else {
            toolbar.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.VISIBLE
        }
    }

    private fun navigateToWelcomeFragment() {
        navController.navigate(R.id.welcomeFragment)
    }
    private fun navigateToQuoteFragment() {
        navController.navigate(R.id.quoteFragment)
    }
    private fun signOut() {
        AuthUI.getInstance().signOut(this)
            .addOnCompleteListener {
                // User is now signed out
                userNameTextView.visibility = View.GONE
                signOutButton.visibility = View.GONE
                loginButton.visibility = View.VISIBLE

                // Navigate to the WelcomeFragment
                navigateToWelcomeFragment()

                Toast.makeText(this, "Signed out successfully", Toast.LENGTH_SHORT).show()
            }
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val user = FirebaseAuth.getInstance().currentUser
            // Handle successful sign-in
        } else {
            val response = result.idpResponse
            if (response == null) {
                // User pressed the back button
                Toast.makeText(this, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
            } else {
                val error = response.error
                if (error is FirebaseUiException) {
                    if (error.errorCode == ErrorCodes.ANONYMOUS_UPGRADE_MERGE_CONFLICT) {
                        // Handle merge conflict error
                    } else {
                        // Handle other FirebaseUiException errors
                        Toast.makeText(this, "Sign-in error: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle other errors
                    Toast.makeText(this, "Sign-in error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}