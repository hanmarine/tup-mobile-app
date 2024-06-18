package com.groupfour.tupmobileapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.util.ArrayList


class SchedAdapter(
    private val context: Fragment,
    private val arrayList: ArrayList<Schedule>
) : ArrayAdapter<Schedule>(context.requireContext(), R.layout.list_schedule, arrayList) {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater: LayoutInflater = LayoutInflater.from(context.requireContext())
        val view: View = inflater.inflate(R.layout.list_schedule, parent, false)

        val subjectCode : TextView = view.findViewById(R.id.subject_code)
        val subjectDesc : TextView = view.findViewById(R.id.subject_desc)
        val schedTime : TextView = view.findViewById(R.id.sched_time)
        val units : TextView = view.findViewById(R.id.unit)
        val rooms : TextView = view.findViewById(R.id.room)
        val facultyName : TextView = view.findViewById(R.id.faculty)

        subjectCode.text = arrayList[position].subjectCode
        subjectDesc.text = arrayList[position].subjectDesc
        schedTime.text = "Schedule: " + arrayList[position].schedTime
        units.text = "Units: " + arrayList[position].units
        rooms.text = arrayList[position].rooms
        facultyName.text = "Faculty: " + arrayList[position].facultyName

        return view
    }
}
