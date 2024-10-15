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

    // List and Adapter for Projects
    private lateinit var projectList: ArrayList<Project>
    private lateinit var projectAdapter: ProjectAdapter

    // List and Adapter for Roles
    private lateinit var roleList: ArrayList<Role>
    private lateinit var roleAdapter: RoleAdapter

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

        // Initialize Role List and Adapter
        roleList = ArrayList()
        roleAdapter = RoleAdapter(roleList, object : RoleAdapter.OnRoleClickListener {
            override fun onEdit(role: Role) {
                // Handle edit functionality here
                Toast.makeText(this@AdminActivity, "Edit feature not implemented yet", Toast.LENGTH_SHORT).show()
            }

            override fun onDelete(role: Role) {
                TODO("Not yet implemented")
            }
        })

        // Set up RecyclerView for Projects
        binding.projectRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.projectRecyclerView.adapter = projectAdapter

        // Set up RecyclerView for Roles
        binding.roleRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.roleRecyclerView.adapter = roleAdapter

        // Fetch Projects from Firestore
        loadProjects()

        // Load roles dynamically
        loadRoles()

        // Add Project Button click listener
        binding.addProjectButton.setOnClickListener {
            startActivity(Intent(this, ProjectAdd::class.java))
        }
    }

    // Load Projects from Firestore
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

    // Load Roles dynamically
    private fun loadRoles() {
        // Hardcode roles and their descriptions
        roleList.clear()
        roleList.add(Role("Admin", "Manages the application and oversees all operations."))
        roleList.add(Role("User", "A regular user of the application with limited access."))
        roleList.add(Role("Engineer", "Responsible for technical tasks and project management."))
        roleList.add(Role("Laborer", "Handles manual tasks and on-site work."))

        // Notify adapter of data change
        roleAdapter.notifyDataSetChanged()
    }

    // Add Role Button click listener
    private fun openAddRoleDialog() {
        // Implementation for adding a new role can be done here if needed.
        Toast.makeText(this, "Add Role feature not implemented yet", Toast.LENGTH_SHORT).show()
    }
}
