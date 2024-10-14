package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityEngineerBinding


class EngineerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEngineerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize ViewBinding
        binding = ActivityEngineerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Load and set the username
        loadUserInfo()

        // Inflate the menu
        setSupportActionBar(binding.toolbar)
    }

    private fun loadUserInfo() {
        // Retrieve user information from SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Default User") ?: "Default User"

        // Set the username in the TextView
        binding.username.text = "Hi, $username"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.mymenu, menu) // Ensure this matches your menu resource name
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_notifications -> {
                // Handle notifications action
                true
            }
            R.id.nav_settings -> {
                // Handle settings action
                true
            }
            R.id.nav_logout -> {
                // Handle logout action
                logout()
                true
            }
            R.id.nav_about -> {
                // Handle about action
                showAbout()
                true
            }
            R.id.nav_help -> {
                // Handle help action
                showHelp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        // Logic for logging out the user, such as clearing SharedPreferences
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()

        // Redirect to login activity
        startActivity(Intent(this, Login::class.java))
        finish() // Close this activity
    }

    private fun showAbout() {
        // Logic to show about information
        // You can create a dialog or start a new activity
    }

    private fun showHelp() {
        // Logic to show help information
        // You can create a dialog or start a new activity
    }
}
