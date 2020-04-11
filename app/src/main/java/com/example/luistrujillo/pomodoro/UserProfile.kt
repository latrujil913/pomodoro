package com.example.luistrujillo.pomodoro


data class UserProfileResponse( // I just changed val to var
    val name : String = "",
    val userId: String = "",
    val netPomodoroCount : Float = 0f,
    val netBreakCount : Float = 0f,
    val projectList: HashMap<String, Boolean> = hashMapOf(),
    val weekHourList: HashMap<String, Float> = hashMapOf(),
    val dayHourList: HashMap<String, Float> = hashMapOf())
//    var weekHourList: HashMap<String, Float> = hashMapOf("Mon" to 0f, "Tue" to 0f, "Wed" to 0f,
//                                                         "Thr" to 0f, "Fri" to 0f, "Sat" to 0f, "Sun" to 0f),
//    var dayHourList: HashMap<String, Float> = hashMapOf("1" to 0f, "2" to 0f, "3" to 0f,"4" to 0f, "5" to 0f,
//                                                        "6" to 0f, "7" to 0f, "8" to 0f, "9" to 0f, "10" to 0f,
//                                                        "11" to 0f, "12" to 0f, "13" to 0f, "14" to 0f, "15" to 0f,
//                                                        "16" to 0f, "17" to 0f, "18" to 0f, "19" to 0f, "20" to 0f,
//                                                        "21" to 0f, "22" to 0f, "23" to 0f, "24" to 0f))

fun UserProfileResponse.mapToStand() =
    UserProfile(name, userId, netPomodoroCount, netBreakCount, projectList, weekHourList, dayHourList)


data class UserProfile(
    var name : String?,
    var userId : String,
    var netPomodoroCount: Float,
    var netBreakCount: Float,
    var projectList: HashMap<String,Boolean>,
    var weekHourList: HashMap<String, Float>,
    var dayHourList: HashMap<String, Float>)

data class Project(
    var name : String,
    var active : Boolean)


//{
//
//
////    operator fun inc(): UserProfile {
////        return UserProfile(name, netPomodoroCount++)
////    }
//}