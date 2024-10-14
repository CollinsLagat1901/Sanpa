package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityAdminBinding
import com.google.firebase.firestore.FirebaseFirestore

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var projectList: ArrayList<Project>
    private lateinit var projectAdapter: ProjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firestore
        firestore = FirebaseFirestore.getInstance()

        // Initialize Project List and Adapter
        projectList = ArrayList()
        projectAdapter = ProjectAdapter(projectList)

        // Set up RecyclerView
        binding.projectRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.projectRecyclerView.adapter = projectAdapter

        // Fetch Projects from Firestore
        loadProjects()

        // Add Project Button
        binding.addProjectButton.setOnClickListener {
            startActivity(Intent(this, ProjectAdd::class.java))
        }
    }

    private fun loadProjects() {
        firestore.collection("Projects")
            .get()
            .addOnSuccessListener { querySnapshot ->
                // Clear old data
                projectList.clear()

                // Loop through the results and add them to the list
                for (document in querySnapshot) {
                    val project = document.toObject(Project::class.java)
                    projectList.add(project)
                }

                // Notify adapter of data change
                projectAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load projects: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
