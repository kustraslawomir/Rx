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
        println("sum 4+5: ${sum(4, 5)}")

        val multipleRandom = { x: Int -> (Random.nextInt(15) + 1) * x }
        println("multipleRandom 3: ${multipleRandom(2)}")

        println("square from 5: ${square(5)}")

        validateNumber(4) { x: Int -> x.isEven() }
        validateNumber(5) { x: Int -> x.isEven() }

    }

    /*
    Pure functions definition:
    if the return value of a function is completely dependent on its arguments/parameters,
    then this function may be referred to as a pure function

    PURE FUNCTION DON'T HAVE SIDE EFFECTS (won't modify any other state)

     */

    private fun square(n: Int) = n * n

    /*
    High order function definition:
    Those functions that take another function as an argument or return fun(args) : Any as result
     */

    private fun validateNumber(x: Int, isEven: (x: Int) -> Boolean) {
        if (isEven(x))
            print("x=$x is even")
        else print("x=$x is not even")
    }
}
