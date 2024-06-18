package com.groupfour.tupmobileapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.groupfour.tupmobileapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var studentInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginBtn: Button
    private lateinit var binding: ActivityMainBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        studentInput = findViewById(R.id.student_id)
        passwordInput = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login_btn)

        loginBtn.setOnClickListener {
            val studentID = studentInput.text.toString()
            val password = passwordInput.text.toString()

            if (studentID.isEmpty()) {
                Toast.makeText(this,"Please enter your student ID.", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(this,"Please enter your password.", Toast.LENGTH_SHORT).show()
            } else {
                readData(studentID, password)
            }
        }
    }

    private fun readData(studentID: String, password: String){
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(studentID).get().addOnSuccessListener {
            if (it.exists()){
                val dbPassword = it.child("password").value.toString()
                if (dbPassword == password) {
                    Toast.makeText(this,"Login Successful.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, NavActivity::class.java).apply {
                        putExtra("student_id", studentID) // passing le data
                    }
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Invalid student ID or password.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"Invalid student ID or password.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this,"Something went wrong.", Toast.LENGTH_SHORT).show()
        }
    }
}