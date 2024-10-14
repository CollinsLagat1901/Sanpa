package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityUserBinding

class DashBoard : AppCompatActivity() {
    private lateinit var binding: ActivityUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo_color)
        enableEdgeToEdge()

        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(R.layout.activity_dash_board)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
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