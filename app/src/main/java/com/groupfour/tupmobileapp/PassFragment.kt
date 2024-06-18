package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PassFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var newPass: EditText
    private lateinit var changePass: EditText
    private lateinit var oldPass: EditText
    private lateinit var changeBtn: Button
    private lateinit var clearBtn: Button
    private var studentID: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Display the current date
        val view = inflater.inflate(R.layout.fragment_pass, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        newPass = view.findViewById(R.id.new_pass)
        changePass = view.findViewById(R.id.confirm_pass)
        oldPass = view.findViewById(R.id.old_pass)
        changeBtn = view.findViewById(R.id.change_pass)
        clearBtn = view.findViewById(R.id.clear_pass)
        studentID = arguments?.getString("student_id")
        databaseReference = FirebaseDatabase.getInstance().getReference("users")

        changeBtn.setOnClickListener {
            if (studentID != null) {
                updatePassword(studentID!!)
            } else {
                Toast.makeText(requireContext(), "Student ID not found.", Toast.LENGTH_SHORT).show()
            }
        }

        clearBtn.setOnClickListener {
            if (studentID != null) {
                newPass.text?.clear()
                changePass.text?.clear()
                oldPass.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Student ID not found.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    private fun updatePassword(studentID: String) {
        val newPassword = newPass.text.toString()
        val confirmPassword = changePass.text.toString()
        val oldPassword = oldPass.text.toString()

        if (newPassword.isEmpty() || confirmPassword.isEmpty() || oldPassword.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill out all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if (newPassword != confirmPassword) {
            Toast.makeText(requireContext(), "Passwords do not match.", Toast.LENGTH_SHORT).show()
            return
        }

        databaseReference.child(studentID).child("password").get().addOnSuccessListener { dataSnapshot ->
            val currentPassword = dataSnapshot.value.toString()
            if (currentPassword == oldPassword) {
                databaseReference.child(studentID).child("password").setValue(newPassword).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Password updated successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Password update failed. Try again.", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Old password is incorrect.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to retrieve data. Try again.", Toast.LENGTH_SHORT).show()
        }
    }
}