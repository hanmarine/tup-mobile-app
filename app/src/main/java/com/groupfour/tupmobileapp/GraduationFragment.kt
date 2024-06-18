package com.groupfour.tupmobileapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GraduationFragment : Fragment() {
    private val months = arrayOf("January","February","March","April","May","June","July","August","September","October","November","December")
    private lateinit var printBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_graduation, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        val selectMonths = view.findViewById<Spinner>(R.id.select_month)
        val arrayAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, months)
        selectMonths.adapter = arrayAdapter

        printBtn = view.findViewById(R.id.print_button)
        printBtn.setOnClickListener{
            Toast.makeText(requireContext(),"Your form has been printed.", Toast.LENGTH_SHORT).show()
        }
        return view
    }

}