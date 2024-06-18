package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class EnrollFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var studentName: TextView
    private lateinit var courseYear: TextView
    private lateinit var studentNo: TextView
    private val semesters = arrayOf("Second","First","Third","Summer")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enroll, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        // Semester spinner
        val selectSem = view.findViewById<Spinner>(R.id.sem_enrollment)
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, semesters)
        selectSem.adapter = arrayAdapter

        // Get the ID
        studentName = view.findViewById(R.id.student_name)
        courseYear = view.findViewById(R.id.course_year)
        studentNo = view.findViewById(R.id.student_no)
        val studentID = arguments?.getString("student_id")

        // Fetching the data
        if (studentID != null) {
            studentNo.text = "Student No.: " + studentID // display the student ID
            fetchData(studentID)
        } else {
            Toast.makeText(requireContext(),"Failed to fetch student ID.", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun fetchData(studentID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(studentID).get().addOnSuccessListener {
            if (it.exists()) {
                studentName.text = "Student Name: " + it.child("fullname").value.toString()
                courseYear.text = "Course and Year Level: " + it.child("course").value.toString()
            } else {
                Toast.makeText(requireContext(),"Data not found.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed to fetch data.", Toast.LENGTH_SHORT).show()
        }
    }
}