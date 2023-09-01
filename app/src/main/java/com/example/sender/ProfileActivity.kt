package com.example.sender

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var userReference: DatabaseReference
    private lateinit var getContentLauncher: ActivityResultLauncher<String>
    private lateinit var takePictureLauncher: ActivityResultLauncher<Intent>



    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("ProfileActivity", "ProfileActivity opened")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Authentication
        auth = Firebase.auth

        // Initialize Firebase Realtime Database
        userReference = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser?.uid ?: "")

        val sharedPreferencesHelper = SharedPreferencesHelper(this)

        // Read the authentication status from SharedPreferences
        val authStatus = sharedPreferencesHelper.getString("authStatus", "loggedOut")

        if (authStatus == "loggedIn") {
            val i = Intent(this@ProfileActivity, ContentOrderCreationActivity::class.java)
            startActivity(i)
            finish()
        } else {
            // Redirect to the login or registration activity
        }


        getContentLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                // Handle the selected content URI here
                // Upload the selected image to Firebase Storage
                uploadImageToStorage(uri)
            }
        }


        takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Handle the captured image
                val imageUri = currentImageUri
                if (imageUri != null) {
                    // Upload the captured image to Firebase Storage
                    uploadImageToStorage(imageUri)
                }
            }
        }

        // Query user details
        userReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val userSnapshot = task.result
                if (userSnapshot != null && userSnapshot.exists()) {
                    val user = userSnapshot.getValue(User::class.java)

                    // Display user details in UI
                    if (user != null) {
                        // Update the UI with the retrieved values
                        displayUserDetails(user, userSnapshot)
                    }
                }
            } else {
                // Handle error
                val exception = task.exception
                Log.e("com.example.sender.ProfileActivity", "Error getting user data: $exception")
                // Display error message or take appropriate action
            }
        }

        val changeImageButton = findViewById<Button>(R.id.changeImageButton)
        changeImageButton.setOnClickListener {
            showImagePickerDialog()
        }

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logoutUser()
        }
    }

    private fun logoutUser() {
        // Sign out the user using Firebase Authentication
        FirebaseAuth.getInstance().signOut()

        // Update shared preferences to indicate the user is logged out
        val sharedPreferencesHelper = SharedPreferencesHelper(this)
        sharedPreferencesHelper.saveString("authStatus", "loggedOut") // Corrected method

        // Redirect to the login or registration activity
        val intent = Intent(this@ProfileActivity, SigninActivity::class.java)
        startActivity(intent)
        finish() // Close the profile activity
    }

    private fun displayUserDetails(user: User, userSnapshot: DataSnapshot) {
        val firstNameTextView = findViewById<TextView>(R.id.firstName)
        val lastNameTextView = findViewById<TextView>(R.id.secondName)
        val emailTextView = findViewById<TextView>(R.id.emailTextView)
        val phoneNumberTextView = findViewById<TextView>(R.id.phoneNumberTextView)

        firstNameTextView.text = user.firstName
        lastNameTextView.text = user.lastName
        emailTextView.text = userSnapshot.child("emailAddress").getValue(String::class.java)
        phoneNumberTextView.text = user.phoneNumber
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun showImagePickerDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Change Profile Image")
            .setMessage("Select a new profile image")
            .setPositiveButton("Gallery") { _, _ ->
                getContentLauncher.launch("image/*")
            }
            .setNegativeButton("Camera") { _, _ ->
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (cameraIntent.resolveActivity(packageManager) != null) {
                    val imageUri = createImageUri()
                    if (imageUri != null) {
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        currentImageUri = imageUri // Store the URI for later use
                        takePictureLauncher.launch(cameraIntent)
                    }
                }
            }
            .setNeutralButton("Cancel", null)
        builder.create().show()
    }


    private fun createImageUri(): Uri? {
        val imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return storageDir?.let {
            FileProvider.getUriForFile(this, "com.example.sender.fileprovider", File(it, imageFileName))
        }
    }

    private fun uploadImageToStorage(imageUri: Uri) {
        val storageReference = FirebaseStorage.getInstance().reference
        val imageFileName = "profile_image.jpg" // Change this to the desired filename

        val imageRef = storageReference.child("profile_images/$imageFileName")

        imageRef.putFile(imageUri)
            .addOnSuccessListener {
                // Image upload was successful
                // Now you can retrieve the download URL and save it to the database
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    // Save the imageUrl to your database, like Firebase Realtime Database
                    // Update the user's profile with the imageUrl
                    updateProfileWithImageUrl(imageUrl)
                }
            }
            .addOnFailureListener {
                // Image upload failed
                // Handle the failure
            }
    }

    private fun updateProfileWithImageUrl(imageUrl: String) {
        // Update the user's profile in the database with the imageUrl
        userReference.child("profileImageUrl").setValue(imageUrl)
            .addOnSuccessListener {
                // Profile image URL updated successfully
            }
            .addOnFailureListener {
                // Profile image URL update failed

            }
    }

    private var currentImageUri: Uri? = null

}
