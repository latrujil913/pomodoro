package com.example.luistrujillo.pomodoro

import android.content.Context
import android.preference.PreferenceManager

class Preferences {
    // Persistent timer values for when life cycle functions are called
    companion object {
        private const val WORK_STATE = "com.luistrujillo.timer.timer_state"
        fun getState(context: Context): TimerActivity.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(WORK_STATE, 0)
            return TimerActivity.TimerState.values()[ordinal]
        }

        fun setState(state: TimerActivity.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(WORK_STATE, ordinal)
            editor.apply()
        }

        private const val NEW_TIME = "com.luistrujillo.timer.backgrounded_time"
        fun getAlarm(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(NEW_TIME, 0)
        }

        private const val OLD_TIMER = "com.luistrujillo.timer.previous_timer_length_seconds"
        fun getOldTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(OLD_TIMER, 0)
        }

        fun setOldTime(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(OLD_TIMER, seconds)
            editor.apply()
        }

        private const val SECONDS = "com.luistrujillo.timer.seconds_remaining"
        fun getSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS, 0)
        }

        fun setSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS, seconds)
            editor.apply()
        }

        fun setAlarm(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(NEW_TIME, time)
            editor.apply()
        }

        private const val LEN_WORK = "com.luistrujillo.timer.timer_length"
        fun getTimerWork(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(LEN_WORK, 25)
        }

        private const val LEN_BREAK = "com.luistrujillo.timer.timer_break_length"
        fun getTimerBreak(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(LEN_BREAK, 5)
        }
    }
}