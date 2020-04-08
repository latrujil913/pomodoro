package com.example.luistrujillo.pomodoro

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.Window
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user.*
import android.widget.EditText
import com.example.luistrujillo.pomodoro.R.id.addButton
import com.example.luistrujillo.pomodoro.R.id.removeButton
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_user)
        supportActionBar?.title = "User"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

//        userNameTextView.text = MainActivity.finalList[0].name

        val calender = Calendar.getInstance()
        var cDay = calender.get(Calendar.DAY_OF_MONTH)
        var cMonth = calender.get(Calendar.MONTH) + 1
        var cYear = calender.get(Calendar.YEAR)
        var selectedMonth = "" + cMonth
        var selectedYear = "" + cYear
        var cHour = calender.get(Calendar.HOUR)
        var cMinute = calender.get(Calendar.MINUTE)
        var cSecond = calender.get(Calendar.SECOND)
        var cAmPm = calender.get(Calendar.AM_PM)

        println("Day of the month: $cDay")
        println("Month: $cMonth")
        println("Year: $cYear")
        println("Selected Month: $selectedMonth")
        println("Selected Year: $selectedYear")
        println("Hour: $cHour")
        println("Minute: $cMinute")
        println("Second: $cSecond")
        println("AM or PM: $cAmPm")

        addButton.setOnClickListener {
            onCreateAddDialog()
        }
        removeButton.setOnClickListener {
            onCreateRemoveDialog()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun onCreateRemoveDialog(){
        val database = FirebaseDatabase.getInstance();
        val ref = database.getReference();

        val builder = AlertDialog.Builder(this)
        var arr2 : Array<String> = arrayOf()

        for ((index, keys) in  MainActivity.finalList[0].projectList.keys.withIndex()){
            arr2 +=  MainActivity.finalList[0].projectList.keys.elementAt(index).toString()
        }

        builder.
            setTitle("Pick a project to remove").
            setItems(arr2) { _, which ->
                //                Toast.makeText(this, "You selected ${arr2[which]}", Toast.LENGTH_LONG).show()
                MainActivity.finalList[0].projectList.remove(arr2[which])
                try {
                    ref.child("UserProfiles").child("Luis").child("projectList").setValue(MainActivity.finalList[0].projectList)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

        builder.setNegativeButton("Cancel"){ dialog, which ->
            dialog.dismiss()
        }

        builder.show()
    }

    @SuppressLint("ResourceType")
    private fun onCreateAddDialog() {
        val database = FirebaseDatabase.getInstance();
        val ref = database.getReference();
        val alert = AlertDialog.Builder(this)

        alert.setTitle("Add project")
        alert.setMessage("Add a new project to work on.")

        // Set an EditText view to get user input
        val input = EditText(this)
        alert.setView(input)

        alert.setPositiveButton("Add") { dialog, whichButton ->
            val value = input.text
            // Do something with value!
            Toast.makeText(this, "$value was added" , Toast.LENGTH_LONG).show()

            MainActivity.finalList[0].projectList[value.toString()] = true
            try {
                ref.child("UserProfiles").child("Luis").child("projectList").setValue(MainActivity.finalList[0].projectList)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            println(MainActivity.finalList[0].projectList)
        }

        alert.setNegativeButton("Cancel") { dialog, whichButton ->
            // Canceled.
        }

        alert.show()
    }

}

