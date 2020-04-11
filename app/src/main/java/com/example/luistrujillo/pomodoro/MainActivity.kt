package com.example.luistrujillo.pomodoro

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var finalList : List<UserProfile>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.setIcon(R.drawable.ic_timelapse)
        supportActionBar?.title = "Pomodoro"

        setupBarChartData()
        setupFireBase()

        button_pomodoro.setOnClickListener{
            openTimerActivity()
        }
        button_stats.setOnClickListener {
            openStatsActivity()
        }
        button_user.setOnClickListener {
            openUserActivity()
        }
        button_settings.setOnClickListener {
            openSettingsActivity()
        }
    }

    private fun openUserActivity(){
        val intent =  Intent(this, UserActivity::class.java)
        startActivity(intent)
    }

    private fun openSettingsActivity(){
        val intent =  Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun openTimerActivity(){
        val intent =  Intent(this, TimerActivity::class.java)
        startActivity(intent)
    }
    private fun openStatsActivity(){
        val intent =  Intent(this, StatsActivity::class.java)
        startActivity(intent)
    }

    private fun setupFireBase() {
        val user = FirebaseAuth.getInstance().currentUser
        val curUser : UserProfile?
        val database = FirebaseDatabase.getInstance()
        database.setPersistenceEnabled(true)  // Enable local persistence
        val userRef = database.getReference("UserProfiles")


        if (user != null) {
            userNameTextView.text = user.displayName

            // TODO: define this user according to profile on firebase
            curUser = user.displayName?.let {
                UserProfile(
                    it,
                    "d",
                    0f,
                    0f,
                    hashMapOf("ProductivityTracker" to true),
                    setupDataWeek(),
                    setupDataDay()
                )
            }
        }
        else {
            curUser = user?.displayName?.let {
                UserProfile(
                    it,
                    "d",
                    0f,
                    0f,
                    hashMapOf("ProductivityTracker" to true),
                    setupDataWeek(),
                    setupDataDay()
                )
            }
        }

        // Add as a new entry to the UserProfiles, using the name as the key
        if (curUser != null) {
            curUser.projectList["Midterm"] = true
            curUser.name?.let {
                userRef.child(it).setValue(curUser)
            }
        }

        // Set listener to be notified to any changes in UserProfiles
        userRef.addValueEventListener(object : ValueEventListener {

            override fun onCancelled(error: DatabaseError) {
                Log.i("main", "Failed to read from the database")
            }

            /* This method is called once with the initial value and again
             whenever data at this location is updated. */
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // Break down dataSnapshot and convert to model objects
                dataSnapshot.run {

                    // Iterate through snapshot and build list of responses
                    val stands = children.mapNotNull{
                        it.getValue(UserProfileResponse::class.java)
                    }

                    // Iterate through responses and convert to UserProfile
                    finalList = stands.map(UserProfileResponse::mapToStand)

                }
            }

        })
    }


    private fun setupBarChartData() {
        // create BarEntry for Bar Group
        val bargroup = ArrayList<BarEntry>()
        bargroup.add(BarEntry(1f, 2f, "1"))
        bargroup.add(BarEntry(2f, 4f, "2"))
        bargroup.add(BarEntry(3f, 6f, "3"))
        bargroup.add(BarEntry(4f, 5f, "4"))
        bargroup.add(BarEntry(5f, 0f, "5"))
        bargroup.add(BarEntry(6f, 3f, "6"))
        bargroup.add(BarEntry(7f, 12.5f, "7"))

        // creating dataset for Bar Group
        val barDataSet = BarDataSet(bargroup, "")

        barDataSet.color = ContextCompat.getColor(this, R.color.colorAccent)

        val data = BarData(barDataSet)
        barChartMain.data = data
        data.setValueTextColor(Color.BLACK)
        barChartMain.xAxis.position = XAxis.XAxisPosition.BOTTOM
        barChartMain.xAxis.labelCount = 7
//        barChart.xAxis.enableGridDashedLine(5f, 5f, 0f)
//        barChart.axisRight.enableGridDashedLine(5f, 5f, 0f)
//        barChart.axisLeft.enableGridDashedLine(5f, 5f, 0f)
        barChartMain.description.isEnabled = false
        barChartMain.animateY(1000)
        barChartMain.legend.isEnabled = false
        barChartMain.setPinchZoom(false)
        barChartMain.data.setDrawValues(true)
    }

    private fun setupDataWeek():  HashMap<String, Float> {
        val defaultDummyDataWeek = HashMap<String, Float>()

        // data for a month
        defaultDummyDataWeek["Mon"] = 3f
        defaultDummyDataWeek["Tue"] = 4f
        defaultDummyDataWeek["Wed"] = 4f
        defaultDummyDataWeek["Thr"] = 3f
        defaultDummyDataWeek["Fri"] = 2f
        defaultDummyDataWeek["Sat"] = 0f
        defaultDummyDataWeek["Sun"] = 1f

        return defaultDummyDataWeek
    }

    private fun setupDataDay():  HashMap<String, Float> {
        val defaultDummyDataDay = HashMap<String, Float>()

        // data for a month
        defaultDummyDataDay["hour0"] = 2f // 12am
        defaultDummyDataDay["hour1"] = 1f // 1am
        defaultDummyDataDay["hour2"] = 0f
        defaultDummyDataDay["hour3"] = 0f
        defaultDummyDataDay["hour4"] = 0f
        defaultDummyDataDay["hour5"] = 0f
        defaultDummyDataDay["hour6"] = 9f
        defaultDummyDataDay["hour7"] = 12f
        defaultDummyDataDay["hour8"] = 0f
        defaultDummyDataDay["hour9"] = 12f
        defaultDummyDataDay["hour10"] = 2f
        defaultDummyDataDay["hour11"] = 0f // 12pm
        defaultDummyDataDay["hour12"] = 5f // 1pm
        defaultDummyDataDay["hour13"] = 0f
        defaultDummyDataDay["hour14"] = 3f
        defaultDummyDataDay["hour15"] = 3f
        defaultDummyDataDay["hour16"] = 7f
        defaultDummyDataDay["hour17"] = 0f
        defaultDummyDataDay["hour18"] = 9f
        defaultDummyDataDay["hour19"] = 1f
        defaultDummyDataDay["hour20"] = 4f
        defaultDummyDataDay["hour21"] = 3f
        defaultDummyDataDay["hour22"] = 0f // 10pm
        defaultDummyDataDay["hour23"] = 0f // 11pm


        return defaultDummyDataDay
    }


}
