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


class EvaluationFragment : Fragment() {
    private val schoolyears = arrayOf("2324","2223","2122")
    private val sems = arrayOf("Third","Second","First","Summer")
    private lateinit var printBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_evaluation, container, false)
        val dateN = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date())
        val date = view.findViewById<TextView>(R.id.curr_date)
        val currentDateText = getString(R.string.curr_date_string)
        date.text = "$currentDateText $dateN"

        // Semester
        val selectSY = view.findViewById<Spinner>(R.id.select_sy)
        val arrayAdapter1 = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, schoolyears)
        selectSY.adapter = arrayAdapter1

        // School Year
        val selectSem = view.findViewById<Spinner>(R.id.select_sem)
        val arrayAdapter2 = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_dropdown_item, sems)
        selectSem.adapter = arrayAdapter2

        return view
    }
}