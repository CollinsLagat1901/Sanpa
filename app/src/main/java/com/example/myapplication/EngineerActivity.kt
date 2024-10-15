package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityEngineerBinding
import com.google.firebase.firestore.FirebaseFirestore

class EngineerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEngineerBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var projectAdapter: ProjectAdapter
    private val projectList = ArrayList<Project>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo_color)
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

        // Set up RecyclerView and Firebase
        setupRecyclerView()
        fetchProjectsFromFirestore()

        // Laborer Requirement Submission
        binding.submitLaborers.setOnClickListener {
            submitLaborerRequirements()
        }

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

    private fun setupRecyclerView() {
        // Setup RecyclerView with adapter
        binding.projectsRecyclerView.layoutManager = LinearLayoutManager(this)
        projectAdapter = ProjectAdapter(projectList)
        binding.projectsRecyclerView.adapter = projectAdapter
    }

    private fun fetchProjectsFromFirestore() {
        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        // Fetch projects from Firestore and update the RecyclerView
        db.collection("projects").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val project = document.toObject(Project::class.java)
                    projectList.add(project)
                }
                projectAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching projects: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun submitLaborerRequirements() {
        val laborersNeeded = binding.laborersInput.text.toString().toIntOrNull()

        if (laborersNeeded != null && projectAdapter.selectedProjectId != null) {
            val projectId = projectAdapter.selectedProjectId // Get the selected project ID

            // Update laborer requirement in Firestore for the selected project
            if (projectId != null) {
                db.collection("projects").document(projectId)
                    .update("laborersNeeded", laborersNeeded)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Laborer requirements updated", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error updating laborers: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        } else {
            Toast.makeText(this, "Please enter a valid number and select a project", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu (mymenu.xml)
        menuInflater.inflate(R.menu.mymenu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_notifications -> {
                Toast.makeText(this, "Notifications selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_settings -> {
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.nav_logout -> {
                logout()
                true
            }
            R.id.nav_about -> {
                showAbout()
                true
            }
            R.id.nav_help -> {
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
        Toast.makeText(this, "About selected", Toast.LENGTH_SHORT).show()
        // Show about dialog or activity
    }

    private fun showHelp() {
        Toast.makeText(this, "Help selected", Toast.LENGTH_SHORT).show()
        // Show help dialog or activity
    }
}
