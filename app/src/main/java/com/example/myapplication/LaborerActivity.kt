package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class LaborerActivity : AppCompatActivity() {

    private lateinit var projectsRecyclerView: RecyclerView
    private lateinit var submitLaborersButton: Button
    private lateinit var jobsAdapter: JobAdapter // Ensure this is your adapter class
    private lateinit var firestore: FirebaseFirestore
    private var selectedJob: String? = null

    // Define variables for expectedStartDate, duration, and comments
    private var expectedStartDate: String? = null // Adjust type as necessary
    private var duration: String? = null // Adjust type as necessary
    private var comments: String? = null // Adjust type as necessary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo_color)
        enableEdgeToEdge()
        setContentView(R.layout.activity_laborer)

        // Manage edge-to-edge insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        projectsRecyclerView = findViewById(R.id.projectsRecyclerView) // Correct ID
        submitLaborersButton = findViewById(R.id.submitLaborers) // Correct ID
        firestore = FirebaseFirestore.getInstance()

        // Setup RecyclerView
        jobsAdapter = JobAdapter { job -> selectedJob = job }
        projectsRecyclerView.adapter = jobsAdapter
        projectsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch jobs/projects
        fetchJobs()

        // Submit laborers button click listener
        submitLaborersButton.setOnClickListener {
            selectedJob?.let { jobId ->
                applyForJob(jobId)
            } ?: run {
                Toast.makeText(this, "Please select a job to apply for.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchJobs() {
        firestore.collection("projects")
            .whereGreaterThan("laborerRequirements", 0)
            .get()
            .addOnSuccessListener { result ->
                val jobsList = result.documents.map { document ->
                    Job(
                        id = document.id,
                        name = document.getString("name") ?: "",
                        description = document.getString("description") ?: "",
                        laborerRequirements = document.getLong("laborerRequirements")?.toInt() ?: 0
                    )
                }
                jobsAdapter.submitList(jobsList)
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error fetching jobs: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun applyForJob(jobId: String) {
        val laborerId = FirebaseAuth.getInstance().uid ?: return

        // Set up application data
        val applicationData = hashMapOf(
            "laborerId" to laborerId,
            "jobId" to jobId,
            "applicationDate" to FieldValue.serverTimestamp(),
            "status" to "Pending"
        )

        firestore.collection("projects").document(jobId)
            .collection("applications")
            .add(applicationData)
            .addOnSuccessListener {
                Toast.makeText(this, "Application successful", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error applying for job: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
