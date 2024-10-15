package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        // Add Project Button click listener
        binding.addProjectButton.setOnClickListener {
            startActivity(Intent(this, ProjectAdd::class.java))
        }
    }

    // Load Projects from Firestore to display them in RecyclerView
    private fun loadProjects() {
        firestore.collection("Projects")
            .get()
            .addOnSuccessListener { querySnapshot ->
                projectList.clear()  // Clear old data

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

    // Assign roles to users (if needed)
    fun assignRole(userID: String?, roleID: String?) {
        val userRef = firestore.collection("Roles").document(userID!!)
        userRef.update("roleID", roleID)
            .addOnSuccessListener {
                Toast.makeText(this@AdminActivity, "Role assigned successfully!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@AdminActivity, "Error assigning role: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
