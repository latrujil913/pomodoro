package com.example.luistrujillo.pomodoro

import android.annotation.SuppressLint
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import kotlinx.android.synthetic.main.activity_timer.*
import android.graphics.Typeface
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.TextView
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class TimerActivity : AppCompatActivity() {
    val calander = Calendar.getInstance()
    var cDay = calander.get(Calendar.DAY_OF_MONTH)
    var cWeekDay = calander.get(Calendar.DAY_OF_WEEK)
    var cMonth = calander.get(Calendar.MONTH) + 1
    var cYear = calander.get(Calendar.YEAR)
    private lateinit var masterTimer: CountDownTimer
    private var masterTimerInSeconds: Long = 0
    private var workState = TimerState.Stopped
    private var timerRemainingInSeconds: Long = 0
    private var workSession: Boolean = true
    var selectedMonth = "" + cMonth
    var selectedYear = "" + cYear
    var cHour = calander.get(Calendar.HOUR)
    var cMinute = calander.get(Calendar.MINUTE)
    var cSecond = calander.get(Calendar.SECOND)

    companion object {
        val currentSeconds: Long get() = Calendar.getInstance().timeInMillis / 1000

        fun alarmZero(context: Context){
            Preferences.setAlarm(0, context)
        }

        @RequiresApi(Build.VERSION_CODES.KITKAT)
        fun alarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long{
            val time = (nowSeconds + secondsRemaining) * 1000
            //TODO "@RequiresApi(Build.VERSION_CODES.KITKAT)" otherwise pending doesn't setExact doesn't work
            Preferences.setAlarm(nowSeconds, context)
            return time
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Work"


        val tx = findViewById<TextView>(R.id.workStatusTextView)
        val tx2 = findViewById<TextView>(R.id.textView_countDown)
        val customFont = Typeface.createFromAsset(assets, "fonts/Raleway-Regular.ttf")
        tx.typeface = customFont
        tx2.typeface = customFont

        workStatusTextView.text = "Start working."
        netPomodoroTextView.text = MainActivity.finalList[0].netPomodoroCount.toInt().toString()
        projectButton.text = MainActivity.finalList[0].projectList.keys.elementAt(0)

        MainActivity.finalList[0].projectList["Theory"] = true

        button_start.setOnClickListener{
            onTimerStart()
            workState =  TimerState.Running // State change

            if (workSession){
                workStatusTextView.text = "Work session in progress."
            }
            else if (!workSession){
                workStatusTextView.text = "Break session in progress."
            }

//            timerRunning = true
            toggleButtons()
        }

        button_stop.setOnClickListener {
            // TODO: update workStatusTextView
            if (workSession){
//                workStatusTextView.text = "Start working."
                showDialog("Start working.")
//                onCreateDialog() // TODO: call when a project is selected

//                println("Break Completed. Start Work session?")

            }
            else if (!workSession){
//                workStatusTextView.text = "Start break."
                showDialog("Start break.")
//                println("Work Completed. Start break?")
            }
        }

        projectButton.setOnClickListener {
            onCreateDialog()
        }
    }

    private fun onCreateDialog(){
         val builder = AlertDialog.Builder(this)
         var arr2 : Array<String> = arrayOf()

         for ((index, keys) in  MainActivity.finalList[0].projectList.keys.withIndex()){
             arr2 +=  MainActivity.finalList[0].projectList.keys.elementAt(index).toString()
         }

         builder.
             setTitle("Pick a project to work on").
             setItems(arr2) { _, which ->
//                Toast.makeText(this, "You selected ${arr2[which]}", Toast.LENGTH_LONG).show()
                 projectButton.text = arr2[which]
             }

         builder.setNegativeButton("Cancel"){ dialog, which ->
             dialog.dismiss()
         }

         builder.show()
    }

    override fun onResume() {
        super.onResume()

        setTimer()

        alarmZero(this)
//        NotificationUtil.hideTimerNotification(this)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onPause() {
        super.onPause()

        if (workState == TimerState.Running){ // State change
//        if (timerRunning){ // State change
            masterTimer.cancel()
            val wakeUpTime = alarm(this, currentSeconds, timerRemainingInSeconds)
//            NotificationUtil.showTimerRunning(this, wakeUpTime)
        }
        saveState()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    private fun saveState () {
        Preferences.setOldTime(masterTimerInSeconds, this)
        Preferences.setSeconds(timerRemainingInSeconds, this)
        Preferences.setState(workState, this) // State change
//        Preferences.setState(timerRunning, this) // State change

    }

    private fun setTimer(){
        workState = Preferences.getState(this) // State change
//        timerRunning = Preferences.getState(this) // State change

        if (workState == TimerState.Stopped) // State change
//        if (timerRunning == false) // State change
            initNewTimer()
        else
            initOldTimer()

        timerRemainingInSeconds = if (workState == TimerState.Running) // State change
//        timerRemainingInSeconds = if (timerRunning) // State change
            Preferences.getSeconds(this)
        else
            masterTimerInSeconds

        val alarmSetTime = Preferences.getAlarm(this)
        if (alarmSetTime > 0)
            timerRemainingInSeconds -= currentSeconds - alarmSetTime

        if (timerRemainingInSeconds <= 0)
            timerComplete()
        else if (workState == TimerState.Running) // State change
//        else if (timerRunning) // State change
            onTimerStart()

        toggleButtons()
        setTimerTextView()
    }

    private fun timerComplete(){
        workState = TimerState.Stopped // State change
//        timerRunning = false // State change

        //set the length of the masterTimer to be the one set in SettingsActivity
        //if the length was changed when the masterTimer was running
        initNewTimer()

//        progress_countdown.progress = 0

        Preferences.setSeconds(masterTimerInSeconds, this)
        timerRemainingInSeconds = masterTimerInSeconds

        toggleButtons()
        setTimerTextView()
    }

    private fun onTimerStart(){
        workState = TimerState.Running // State change
//        timerRunning = true // State change
        val database = FirebaseDatabase.getInstance();
        val ref = database.getReference();

        masterTimer = object :
            CountDownTimer(timerRemainingInSeconds * 1000, 1000) {
            override fun onFinish() {
                //todo: set the new work session in Preferences
                if (workSession){
                    workSession = false
                }
                else if (!workSession) {
                    workSession = true
                }

                if (workSession && timerRemainingInSeconds <= 0 ){
                    println("Break Completed. Start Work session?")
                    workStatusTextView.text = "Break Completed.\nStart Work session?"

                    MainActivity.finalList[0].netBreakCount++
                    try {
                        ref.child("UserProfiles").child("Luis").child("netBreakCount").setValue(MainActivity.finalList[0].netBreakCount)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                    netPomodoroTextView.text = MainActivity.finalList[0].netPomodoroCount.toInt().toString()
                }
                else if (!workSession && timerRemainingInSeconds <= 0){
                    println("Work Completed. Start break?")
                    workStatusTextView.text = "Work Completed.\nStart break?"

                    MainActivity.finalList[0].netPomodoroCount++
                    try {
                        ref.child("UserProfiles").child("Luis").child("netPomodoroCount").setValue(MainActivity.finalList[0].netPomodoroCount)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }


                    netPomodoroTextView.text = MainActivity.finalList[0].netPomodoroCount.toInt().toString()
                }

                println("Pomodoro: " + MainActivity.finalList[0].netPomodoroCount)
                println("Break: " + MainActivity.finalList[0].netBreakCount)

                timerComplete()
            }

            override fun onTick(millisUntilFinished: Long) {
                timerRemainingInSeconds = millisUntilFinished / 1000
                setTimerTextView()
            }
        }.start()
    }

    private fun initNewTimer(){
        val lengthInMinutes: Int

        // get time length of work
        if (workSession){ //todo: set the new work session in preferences
            lengthInMinutes = Preferences.getTimerWork(this)
            masterTimerInSeconds = (lengthInMinutes * 60L)

        }
        // get time length of break
        else if (!workSession){
            lengthInMinutes = Preferences.getTimerBreak(this)
            masterTimerInSeconds = (lengthInMinutes * 60L)

        }
    }

    private fun initOldTimer(){
        masterTimerInSeconds = Preferences.getOldTime(this)
    }

    @SuppressLint("SetTextI18n")
    private fun setTimerTextView(){
        val minutesUntilFinished = timerRemainingInSeconds / 60
        val secondsInMinuteUntilFinished = timerRemainingInSeconds - minutesUntilFinished * 60
        val secondsStr = secondsInMinuteUntilFinished.toString()
        textView_countDown.text =
                "$minutesUntilFinished:" +
                if (secondsStr.length == 2)
                    secondsStr
                else
                    "0$secondsStr"
    }

    enum class TimerState{
        Stopped, Running
    }

    private fun toggleButtons(){
        when (workState) {
            TimerState.Running ->{ // State change
                button_start.isEnabled = false
                button_stop.isEnabled = true
            }
            TimerState.Stopped -> { // State change
                button_start.isEnabled = true
                button_stop.isEnabled = false
            }
        }
//        if (timerRunning){
//                button_start.isEnabled = false
//                button_stop.isEnabled = true
//
//        } else {
//            button_start.isEnabled = true
//            button_stop.isEnabled = false
//
//        }
    }

    private fun showDialog(str: String) { // TODO: upon click if the start button is clicked when session in progress!!!
        val alertDialog = AlertDialog.Builder(this/*, R.style.AlertDialogStyle*/)
            .setIcon(R.drawable.ic_alarm_off)
            .setTitle("Forfeit session?")
            .setMessage("Are you sure you want to forfeit the current session?")
            .setPositiveButton("Yes") { _, _ ->
                workStatusTextView.text = str
                masterTimer.cancel()
                timerComplete()
            }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }



}


