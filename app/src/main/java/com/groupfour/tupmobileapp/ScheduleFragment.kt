package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference


class ScheduleFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var fullNameTextView: TextView
    private lateinit var scheduleListView: ListView
    private lateinit var schedAdapter: SchedAdapter
    private var scheduleList = ArrayList<Schedule>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        fullNameTextView = view.findViewById(R.id.full_name_text_view)
        val studentID = arguments?.getString("student_id")
        if (studentID != null) {
            fetchFullName(studentID)
            fetchSchedule(studentID)
        } else {
            fullNameTextView.text = "Student ID is missing"
        }

        scheduleListView = view.findViewById(R.id.list_view)
        schedAdapter = SchedAdapter(this, scheduleList)
        scheduleListView.adapter = schedAdapter

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

    private fun fetchSchedule(studentID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(studentID).get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                val courseID = userSnapshot.child("course").value.toString()
                fetchCourseDetails(courseID)
            } else {
                Toast.makeText(requireContext(),"Course ID not found.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed to get course ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCourseDetails(courseID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("courses")
        databaseReference.child(courseID).get().addOnSuccessListener { courseSnapshot ->
            if (courseSnapshot.exists()) {
                scheduleList.clear()
                courseSnapshot.children.forEach { subjectSnapshot ->
                    val subjectCode = subjectSnapshot.key.toString()
                    val subjectDesc = subjectSnapshot.child("desc").value.toString()
                    val facultyName = subjectSnapshot.child("faculty").value.toString()
                    val room = subjectSnapshot.child("room").value.toString()
                    val schedTime = subjectSnapshot.child("schedule").value.toString()
                    val units = subjectSnapshot.child("units").value.toString()
                    val schedule = Schedule(subjectCode, subjectDesc, schedTime, units, room, facultyName)
                    scheduleList.add(schedule)
                }
                schedAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(),"Course data not found.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed to fetch courses.", Toast.LENGTH_SHORT).show()
        }
    }
}
