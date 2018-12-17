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

        validateNumberWithInlineFun(6) { x: Int -> x.isEven() }

    }

    /*
    ------------ Pure functions ------------
    if the return value of a function is completely dependent on its arguments/parameters,
    then this function may be referred to as a pure function

    PURE FUNCTION DON'T HAVE SIDE EFFECTS (won't modify any other state)

     */

    private fun square(n: Int) = n * n

    /*
   ------------ High order function ------------
    Those functions that take another function as an argument or return fun(args) : Any as result

    NOTE: Each object is allocated space in memory heap and the methods that call this method are also allocated space.
    To avoid allocating memory heap we can consider using inline functions
     */

    private fun validateNumber(x: Int, isEven: (x: Int) -> Boolean) {
        if (isEven(x))
            print("x=$x is even")
        else print("x=$x is not even")
    }

    /*
    ------------ Inline functions ------------
    Tip: Use in high order functions
    Helps reduce call overhead. Inline tells the compiler to copy these functions and parameters to call site.
    Definition:
    Inline functions are an enhancement feature to improve the performance and memory optimization of a program

    Example:

    public void nonInlined(Function block) {
        System.out.println("test");
        block.invoke();

    }

    Compiler will create from above method instance of Function:

    nonInlined(new Function() {
    @Override
    public void invoke() {
        System.out.println("do something here");
    }});

    and if we use inline function same block of code would look like this:
    System.out.println("do something here");
    No new instances are created!
    */

    private inline fun validateNumberWithInlineFun(x: Int, isEven: (x: Int) -> Boolean) {
        if (isEven(x))
            print("inline x=$x is even")
        else print("inline x=$x is not even")
    }
}
