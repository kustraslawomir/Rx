package slawomir.kustra.rx

import android.app.Activity
import android.content.Context
import android.content.Intent

fun Context.startScreen(activity : Activity){
    startActivity(Intent(this, activity::class.java))
}

fun Int.isEven() = this % 2 == 0