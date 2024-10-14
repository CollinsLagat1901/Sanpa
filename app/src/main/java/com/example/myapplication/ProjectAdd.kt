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
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                // User is signed in, proceed with Firestore operation
            } else {
                Toast.makeText(this, "Please log in to add a project.", Toast.LENGTH_SHORT).show()
            }

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

            // Setup data to add to Firestore
            val project = Project(projectId, projectName, projectBudget)

            firestore.collection("Projects")
                .add(project)
                .addOnSuccessListener {
                    progressDialog.dismiss()
                    Toast.makeText(this, "Project added successfully!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Log.e("Firestore Error", "Error adding project: ${e.message}")
                    Toast.makeText(this, "Failed to add project: ${e.message}", Toast.LENGTH_SHORT).show()
                }

        }
        private fun getUserRole() {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                val uid = user.uid
                firestore.collection("Users").document(uid).get().addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val role = document.getString("role") // Make sure this matches your Firestore document structure
                        if (role == "Admin" || role == "Engineer") {
                            // User has the required role, proceed to add project
                            addProjectToFirebase()
                        } else {
                            Toast.makeText(this, "You do not have permission to add a project.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "User role not found.", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to retrieve user role.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please log in to add a project.", Toast.LENGTH_SHORT).show()
            }
        }

    }
