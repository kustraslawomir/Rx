package slawomir.kustra.rx

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlin.random.Random

class SecondChapter : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second_chapter)

        val sum = { x: Int, y: Int -> x + y }
        println("sum 4+5 = ${sum(4, 5)}")

        val multipleRandom = { x: Int -> (Random.nextInt(15) + 1) * x }
        println("multipleRandom 3 = ${multipleRandom(2)}")
    }
}
