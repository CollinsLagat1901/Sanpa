package com.example.myapplication

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.myapplication.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor = ContextCompat.getColor(this, R.color.logo_color)

        // Initialize View Binding
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize ProgressDialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setCanceledOnTouchOutside(false)

        // Populate Spinner with roles
        setupRoleSpinner()

        // Register Button Click Listener
        binding.submitButton.setOnClickListener {
            // Validate data before creating an account
            validateData()
        }
    }

    // Function to setup role spinner with role options
    private fun setupRoleSpinner() {
        // List of roles
        val roles = listOf("Admin", "Engineer", "Laborer", "Driver","User")

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.roleSpinner.adapter = adapter

        // Log roles for debugging purposes
        Log.d("SignUpActivity", "Roles: $roles")
    }

    private fun validateData() {
        // Retrieve input values
        val name = binding.name.text.toString().trim()
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val confirmPassword = binding.rePassword.text.toString().trim()
        val role = binding.roleSpinner.selectedItem.toString().trim()  // Get selected role

        // Check if fields are valid
        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || role.isEmpty()) {
            showToast("Please fill in all fields")
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("Invalid Email Format")
        } else if (password != confirmPassword) {
            showToast("Passwords do not match")
        } else {
            // Proceed to create the user account
            createUserAccount(email, password, name, role)
        }
    }

    private fun createUserAccount(email: String, password: String, name: String, role: String) {
        // Show progress dialog during account creation
        progressDialog.setMessage("Creating Account...")
        progressDialog.show()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                // Account creation successful, update user information
                updateUserInfo(email, name, role)
            }
            .addOnFailureListener {
                // Handle account creation failure
                progressDialog.dismiss()
                showToast("Failed to create account: ${it.message}")
            }
    }

    private fun updateUserInfo(email: String, name: String, role: String) {
        // Update progress dialog message
        progressDialog.setMessage("Saving User Information...")

        // Get the current user's UID
        val uid = auth.uid
        if (uid != null) {
            // Prepare data to store in Firebase Realtime Database
            val timestamp = System.currentTimeMillis()
            val hashMap: HashMap<String, Any> = HashMap()
            hashMap["uid"] = uid
            hashMap["email"] = email
            hashMap["name"] = name
            hashMap["role"] = role  // Add role to the user data
            hashMap["profileImage"] = ""  // Placeholder for profile image
            hashMap["timeStamp"] = timestamp

            // Store the data under "Users" node in Firebase
            val ref = FirebaseDatabase.getInstance().getReference("Users")
            ref.child(uid)
                .setValue(hashMap)
                .addOnSuccessListener {
                    // Dismiss progress dialog and navigate to appropriate activity
                    progressDialog.dismiss()
                    showToast("Account created successfully")
                    redirectToDashboard(role)
                }
                .addOnFailureListener { e ->
                    // Handle failure during data saving
                    progressDialog.dismiss()
                    showToast("Failed to save user info: ${e.message}")
                }
        }
    }

    private fun redirectToDashboard(role: String) {
        // Redirect based on user role
        val intent = when (role) {
            "Admin" -> Intent(this, AdminActivity::class.java)
            "Engineer" -> Intent(this, EngineerActivity::class.java)
            "Laborer" -> Intent(this, LaborerActivity::class.java)
            "Driver" -> Intent(this, DriverActivity::class.java)
            "User" -> Intent(this, UserActivity::class.java)
            else -> Intent(this, UserActivity::class.java)  // Default to UserActivity
        }
        startActivity(intent)
        finish() // Close the sign-up activity
    }

    private fun showToast(message: String) {
        // Utility function to show a toast message
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
