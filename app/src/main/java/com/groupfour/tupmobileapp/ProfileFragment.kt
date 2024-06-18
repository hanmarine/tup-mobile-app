package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class ProfileFragment : Fragment() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var fullName : TextView
    private lateinit var email : TextView
    private lateinit var birthDate : TextView
    private lateinit var birthPlace : TextView
    private lateinit var citizen : TextView
    private lateinit var gender : TextView
    private lateinit var age : TextView
    private lateinit var height : TextView
    private lateinit var weight : TextView
    private lateinit var civilStatus : TextView
    private lateinit var learnerRef : TextView
    private lateinit var cellphoneNo : TextView
    private lateinit var fbAccount : TextView
    private lateinit var personsDis : TextView
    private lateinit var indigenousPeople : TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // display the current date
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        // Get the ID
        fullName = view.findViewById(R.id.full_name)
        email = view.findViewById(R.id.email)
        birthDate = view.findViewById(R.id.birthday)
        birthPlace = view.findViewById(R.id.birthplace)
        citizen = view.findViewById(R.id.citizenship)
        gender = view.findViewById(R.id.gender)
        age = view.findViewById(R.id.age)
        height = view.findViewById(R.id.height)
        weight = view.findViewById(R.id.weight)
        civilStatus = view.findViewById(R.id.civ_status)
        learnerRef = view.findViewById(R.id.lrn)
        cellphoneNo = view.findViewById(R.id.cell_no)
        fbAccount = view.findViewById(R.id.fb_acc)
        personsDis = view.findViewById(R.id.pwd_status)
        indigenousPeople = view.findViewById(R.id.indigenous)
        val studentID = arguments?.getString("student_id")

        if (studentID != null) {
            fetchData(studentID)
        } else {
            Toast.makeText(requireContext(),"StudentID not found.", Toast.LENGTH_SHORT).show()
        }
        return view
    }

    private fun fetchData(studentID: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("users")
        databaseReference.child(studentID).get().addOnSuccessListener {
            if (it.exists()) {
                fullName.text = it.child("fullname").value.toString()
                email.text = it.child("email").value.toString()
                birthDate.text = it.child("birthday").value.toString()
                birthPlace.text = it.child("birthplace").value.toString()
                citizen.text = it.child("citizen").value.toString()
                gender.text = it.child("gender").value.toString()
                age.text = it.child("age").value.toString()
                height.text = it.child("height").value.toString()
                weight.text = it.child("weight").value.toString()
                civilStatus.text = it.child("civil").value.toString()
                learnerRef.text = it.child("lrn").value.toString()
                cellphoneNo.text = it.child("contact").value.toString()
                fbAccount.text = it.child("fb").value.toString()
                personsDis.text = it.child("pwd").value.toString()
                indigenousPeople.text = it.child("indig").value.toString()
            } else {
                Toast.makeText(requireContext(),"Data not found.", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(),"Failed to fetch data.", Toast.LENGTH_SHORT).show()
        }
    }
}



