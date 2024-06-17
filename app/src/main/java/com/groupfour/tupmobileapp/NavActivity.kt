package com.groupfour.tupmobileapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import com.google.android.material.navigation.NavigationView

class NavActivity : AppCompatActivity() {

    // Navigation Pane
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_nav)

        // Retrieve data from Intent
        val studentID = intent.getStringExtra("student_id")
        val password = intent.getStringExtra("password")

        // Create a Bundle to pass data to the fragment
        val bundle = Bundle().apply {
            putString("student_id", studentID)
            putString("password", password)
        }

        // Pass data to ScheduleFragment
        val scheduleFragment = ScheduleFragment().apply {
            arguments = bundle
        }
//        val enrollFragment = EnrollFragment().apply {
//            arguments = bundle
//        }
//        val profileFragment = ProfileFragment().apply {
//            arguments = bundle
//        }

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(scheduleFragment)

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){
                R.id.nav_schedule -> replaceFragment(scheduleFragment)          // Schedule
                R.id.nav_calendar -> replaceFragment(CalendarFragment())        // Calendar
                R.id.nav_graduation -> replaceFragment(GraduationFragment())    // Faculty Eval.
                R.id.nav_evaluation -> replaceFragment(EvaluationFragment())    // App. for Grad.
                R.id.nav_logout -> {
                    val logoutIntent = Intent(this, MainActivity::class.java)
                    startActivity(logoutIntent)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}