package slawomir.kustra.rx.chapters.second

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Maybe
import io.reactivex.MaybeObserver
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_second_chapter.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import slawomir.kustra.rx.R
import slawomir.kustra.rx.chapters.second.calculator.ReactiveCalculator
import slawomir.kustra.rx.isEven
import kotlin.random.Random
import kotlin.system.measureTimeMillis

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

        val reactiveCalculator = ReactiveCalculator(5, 10)
        reactiveCalculator.modifyNumbers(a = 3, b = 10)

        count.setOnClickListener { reactiveCalculator.handleInput(input.text.toString()) }

        runCoroutines.setOnClickListener {
            GlobalScope.launch { longRunningTsk() }
            println(fibonacci.take(10).joinToString(","))
        }

        runMonadExamples()
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

    /*
    ------------ Coroutines ------------
    Coroutines help with writing asynchronous, non-blocking (ui) code.
    Coroutines are light-weight threads. A light weight thread means it doest map on native thread,
     so it doest require context switching on processor, so they are faster.
     */

    private suspend fun longRunningTsk(): Long {
        return measureTimeMillis {
            println("Please wait")
            delay(2000)
            println("Delay Over")
        }
    }

    private val fibonacci = sequence {
        var a = 0
        var b = 0
        yield(a)
        yield(b)

        while (true) {
            val c = a + b
            yield(c)
            a = b
            b = c
        }
    }

    /*
    ------------ Monad ------------
     Monad is a structure that creates a new type by encapsulating a value and adding some extra functionalities to it.
     The Maybe monad says it may or may not contain a value, and it completes with or without a value or with an error.
     */

    private fun runMonadExamples() {

        val maybeValue: Maybe<Int> = Maybe.just(10)
        maybeValue.subscribe(object : MaybeObserver<Int> {
            override fun onSuccess(t: Int) {
                println("onSuccess")
            }

            override fun onComplete() {
                println("onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                println("onSubscribe")
            }

            override fun onError(e: Throwable) {
                println("onError ${e.message}")
            }
        })

        val maybeEmpty = Maybe.empty<Int>()
        maybeEmpty.subscribe({
            println("onSuccess $it")
        }, { println("onError ${it.message}") })
    }
}
