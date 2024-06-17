package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference


class ScheduleFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var fullNameTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        // To fetch the full name
        fullNameTextView = view.findViewById(R.id.full_name_text_view)
        val studentID = arguments?.getString("student_id")
        if (studentID != null) {
                fetchFullName(studentID)
            } else {
                fullNameTextView.text = "Student ID is missing"
            }
        return view
    }

    private fun fetchFullName(studentID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(studentID).get().addOnSuccessListener {
            if (it.exists()) {
                val fullName = it.child("fullname").value.toString()
                val welcomeText = getString(R.string.welcome_text)
                fullNameTextView.text = "$welcomeText $fullName"
            } else {
                fullNameTextView.text = "Name not found"
            }
        }.addOnFailureListener {
            fullNameTextView.text = "Failed to fetch name"
        }
    }
}