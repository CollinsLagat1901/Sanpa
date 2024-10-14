package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view binding
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Get the current user
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Get the user ID
            val userId = currentUser.uid

            // Fetch the user's profile information from Firestore
            val userDocRef = firestore.collection("users").document(userId)
            userDocRef.get().addOnSuccessListener { document ->
                if (document != null) {
                    // Retrieve and display the username from Firestore
                    val username = document.getString("username") ?: "Guest"
                    binding.userRole.text = username
                }
            }.addOnFailureListener { exception ->
                Log.e("UserActivity", "Error fetching user profile", exception)
            }
        }

        // Set click listeners for the CardViews
        binding.engineerCard.setOnClickListener {
            val intent = Intent(this, EngineerActivity::class.java)
            startActivity(intent)
        }

        binding.driverCard.setOnClickListener {
            val intent = Intent(this, DriverActivity::class.java)
            startActivity(intent)
        }

        binding.laborerCard.setOnClickListener {
            val intent = Intent(this, LaborerActivity::class.java)
            startActivity(intent)
        }

        binding.shopCard.setOnClickListener {
            val intent = Intent(this, ShopActivity::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView with data
        setupRecyclerView()

        // Handle bottom navigation bar click events
        setupBottomAppBarClickListeners()
    }

    private fun setupBottomAppBarClickListeners() {
        // Set click listeners for the BottomAppBar icons
        binding.appBar.findViewById<ImageView>(R.id.home).setOnClickListener {
            // Handle home click
        }

        binding.appBar.findViewById<ImageView>(R.id.contract).setOnClickListener {
            val intent = Intent(this, ContractsActivity::class.java)
            startActivity(intent)
        }

        binding.appBar.findViewById<ImageView>(R.id.chat).setOnClickListener {
            val intent = Intent(this, ChatsActivity::class.java)
            startActivity(intent)
        }

        binding.appBar.findViewById<ImageView>(R.id.profile).setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        // Sample data for the RecyclerView
        val items = listOf(
            Item(R.drawable.img_constructors, "Engineer Tools", "Find the best tools for engineers"),
            Item(R.drawable.img_tools, "Construction Materials", "Top quality materials for construction"),
            Item(R.drawable.img_vehcles, "Vehicles", "Rent or purchase vehicles for construction"),
            Item(R.drawable.img_designs, "Shop Items", "Buy materials directly from our partners"),
            Item(R.drawable.img_sites, "Amazing Building Sites", "Get the best construction site the is approve by NEEMA")
        )

        // Initialize the adapter
        val adapter = ItemAdapter(items)

        // Set up the RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }
}
