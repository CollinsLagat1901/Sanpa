package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityLoginBinding
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize View Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth and Database Reference
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("Users")

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // Handle login button click
        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show progress dialog before initiating login
                progressDialog.setMessage("Logging in...")
                progressDialog.show()

                // Firebase login authentication
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        progressDialog.dismiss()  // Dismiss progress dialog after task completion
                        if (task.isSuccessful) {
                            // Get the current user's UID
                            val userId = auth.currentUser?.uid
                            if (userId != null) {
                                // Redirect based on role
                                redirectToAppropriateActivity(userId)
                            }
                        } else {
                            // Show error if login fails
                            Toast.makeText(this, task.exception?.localizedMessage ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                // Show error if fields are empty
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            }
        }

        // Handle Sign Up text click
        binding.registerText.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
    }

    private fun redirectToAppropriateActivity(userId: String) {
        // Fetch user data from Firebase based on UID
        database.child(userId).get().addOnSuccessListener { dataSnapshot ->
            val role = dataSnapshot.child("role").getValue(String::class.java)
            when (role) {
                "Admin" -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                    finish()
                }
                "Engineer" -> {
                    startActivity(Intent(this, EngineerActivity::class.java))
                    finish()
                }
                "Laborer" -> {
                    startActivity(Intent(this, LaborerActivity::class.java))
                    finish()
                }
                "Driver" -> {
                    startActivity(Intent(this, DriverActivity::class.java))
                    finish()
                }
                "User" -> {
                    startActivity(Intent(this, UserActivity::class.java))
                    finish()
                }
                else -> {
                    // Handle case where role is not found
                    Toast.makeText(this, "No role assigned. Please contact admin.", Toast.LENGTH_SHORT).show()
                    auth.signOut() // Sign out the user
                }
            }
        }.addOnFailureListener {
            progressDialog.dismiss()  // Dismiss progress dialog on failure
            Toast.makeText(this, "Failed to retrieve user data", Toast.LENGTH_SHORT).show()
        }
    }
}
