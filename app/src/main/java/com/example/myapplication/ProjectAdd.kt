package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.myapplication.databinding.ActivityProjectAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProjectAdd : AppCompatActivity() {

    private lateinit var binding: ActivityProjectAddBinding
    private lateinit var progressDialog: ProgressDialog
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityProjectAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firestore = FirebaseFirestore.getInstance()

        // Configure progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait...")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle back button click
        binding.backBtn.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Handle submit button click
        binding.submitBtn.setOnClickListener {
            validateData()
        }
    }

    private var projectName = ""
    private var projectId = ""
    private var projectBudget = ""

    private fun validateData() {
        // Get data from input fields
        projectName = binding.projectNameEt.text.toString().trim()
        projectId = binding.projectIdEt.text.toString().trim()
        projectBudget = binding.budgetEt.text.toString().trim()

        // Validate the inputs
        if (projectName.isEmpty()) {
            Toast.makeText(this, "Please enter project name.", Toast.LENGTH_SHORT).show()
        } else if (projectId.isEmpty()) {
            Toast.makeText(this, "Please enter project ID.", Toast.LENGTH_SHORT).show()
        } else if (projectBudget.isEmpty()) {
            Toast.makeText(this, "Please enter project budget.", Toast.LENGTH_SHORT).show()
        } else {
            addProjectToFirebase()
        }
    }

    private fun addProjectToFirebase() {
        // Show progress dialog
        progressDialog.show()

        // Prepare project data
        val projectID = projectId
        val projectName = projectName
        val budget = projectBudget.toDouble()  // Convert string budget to double

        // Create a project object
        val project = hashMapOf(
            "projectId" to projectID,
            "projectName" to projectName,
            "Budget" to budget,
            "Enginees" to arrayListOf<Any>(),  // Empty for now
            "laborerNeeded" to 0,  // You can adjust this if needed
            "laborerAssigned" to arrayListOf<Any>()  // Empty for now
        )

        // Add to Firestore
        firestore.collection("Projects").document(projectID)
            .set(project)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Project added successfully!", Toast.LENGTH_SHORT).show()

                // Return to AdminActivity after successful addition
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to add project: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
