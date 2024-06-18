package com.groupfour.tupmobileapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class GradesFragment : Fragment() {

    private lateinit var database: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_grades, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"


        val tableLayout = view.findViewById<TableLayout>(R.id.gradesTable) // fetching the grades
        val tableGwa = view.findViewById<TableLayout>(R.id.gwaTable) // fetching the GWA
        val studentID = arguments?.getString("student_id")
        // Fetching the data
        if (studentID != null) {
            fetchGrades(studentID, tableLayout)
            fetchGwa(studentID, tableGwa)
        } else {
            Toast.makeText(requireContext(),"Failed to fetch student ID.", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    // to fetch le grades
    private fun fetchGrades(studentID: String, tableLayout: TableLayout) {
        database = FirebaseDatabase.getInstance().reference.child("grades").child(studentID)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = tableLayout.childCount
                if (childCount > 1) {
                    tableLayout.removeViews(1, childCount - 1)
                }

                for (gradeSnapshot in snapshot.children) {
                    val course = gradeSnapshot.key
                    val units = gradeSnapshot.child("units").value
                    val grades = gradeSnapshot.child("grades").value

                    // convert to string
                    val unit = units.toString()
                    val grade = grades.toString()

                    if (course != null && units != null && grades != null) {
                        val tableRow = LayoutInflater.from(requireContext()).inflate(R.layout.table_row, null) as TableRow
                        tableRow.findViewById<TextView>(R.id.courseView).text = course
                        tableRow.findViewById<TextView>(R.id.unitsView).text = unit
                        tableRow.findViewById<TextView>(R.id.gradesView).text = grade
                        tableLayout.addView(tableRow)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch grades: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // to fetch le gwa
    private fun fetchGwa(studentID: String, tableLayout: TableLayout) {
        var totalUnits = 0
        var totalGrades = 0.0
        var average = 0.0
        database = FirebaseDatabase.getInstance().reference.child("grades").child(studentID)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val childCount = tableLayout.childCount
                if (childCount > 1) {
                    tableLayout.removeViews(1, childCount - 1)
                }

                for (gradeSnapshot in snapshot.children) {
                    val gwaUnits = gradeSnapshot.child("units").value.toString().toInt()
                    val gwaGrades = gradeSnapshot.child("grades").value.toString().toDouble()

                    val gradePoints = gwaUnits * gwaGrades

                    totalUnits += gwaUnits
                    totalGrades += gradePoints
                    average = totalGrades / totalUnits
                }

                // formatting to two decimal places
                val formattedAverage = "%.2f".format(average)
                val formattedTotalGrades = "%.2f".format(totalGrades)

                val tableData = arrayOf(
                    arrayOf("Total Units", totalUnits.toString()),
                    arrayOf("Total Grade Points", formattedTotalGrades),
                    arrayOf("GWA", formattedAverage)
                )

                for (row in tableData) {
                    val gwaRow = LayoutInflater.from(requireContext()).inflate(R.layout.gwa_row, null) as TableRow
                    gwaRow.findViewById<TextView>(R.id.totalView).text = row[0]
                    gwaRow.findViewById<TextView>(R.id.noTotalView).text = row[1]
                    tableLayout.addView(gwaRow)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to fetch grades: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
