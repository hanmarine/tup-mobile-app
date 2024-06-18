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

        // Retrieve data
        val studentID = intent.getStringExtra("student_id")

        // Bundle le data
        val bundle = Bundle().apply {
            putString("student_id", studentID)
        }

        // Pass data to fragments
        val scheduleFragment = ScheduleFragment().apply {
            arguments = bundle
        }
        val enrollFragment = EnrollFragment().apply {
            arguments = bundle
        }
        val profileFragment = ProfileFragment().apply {
            arguments = bundle
        }
        val passFragment = PassFragment().apply {
            arguments = bundle
        }
        val gradesFragment = GradesFragment().apply {
            arguments = bundle
        }

        drawerLayout = findViewById(R.id.drawerLayout)
        val navView : NavigationView = findViewById(R.id.nav_view)

        toggle = ActionBarDrawerToggle(this, drawerLayout,R.string.open,R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        replaceFragment(scheduleFragment, getString(R.string.sched_title))

        navView.setNavigationItemSelectedListener {

            it.isChecked = true

            when(it.itemId){
                R.id.nav_schedule -> replaceFragment(scheduleFragment, getString(R.string.sched_title))
                R.id.nav_calendar -> replaceFragment(CalendarFragment(), getString(R.string.calendar_title))
                R.id.nav_graduation -> replaceFragment(GraduationFragment(), getString(R.string.grad_title))
                R.id.nav_evaluation -> replaceFragment(EvaluationFragment(), getString(R.string.faculty_title))
                R.id.nav_pass -> replaceFragment(passFragment, getString(R.string.password_title))
                R.id.nav_enroll -> replaceFragment(enrollFragment, getString(R.string.enroll_title))
                R.id.nav_profile -> replaceFragment(profileFragment, getString(R.string.profile_title))
                R.id.nav_message -> replaceFragment(MessageFragment(), getString(R.string.message_title))
                R.id.nav_grades -> replaceFragment(gradesFragment, getString(R.string.grades_title))
                R.id.nav_logout -> {
                    val logoutIntent = Intent(this, MainActivity::class.java)
                    startActivity(logoutIntent)
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, title: String){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
        setTitle(title)
        drawerLayout.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}