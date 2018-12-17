package slawomir.kustra.rx.chapters.second.calculator

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReactiveCalculator(a: Int, b: Int) {


    private val subjectCalc: Subject<ReactiveCalculator> = PublishSubject.create()
    private var numbers: Pair<Int, Int> = Pair(0, 0)
    private val disposables = CompositeDisposable()

    init {
        numbers = Pair(a, b)

        disposables.add(subjectCalc.subscribe {
            with(it) {
                calculateAddition()
                calculateSubtraction()
                calculateMultiplication()
                calculateDivision()
            }
        })
        subjectCalc.onNext(this)
    }

    private fun calculateAddition() : Int {
        val result  =  numbers.first + numbers.second
        println("calculateAddition = $result")
        return result
    }

    private fun calculateSubtraction() : Int {
        val result =  numbers.first - numbers.second
        println("calculateSubtraction = $result")
        return result
    }

    private fun calculateMultiplication() : Int {
        val result =  numbers.first * numbers.second
        println("calculateMultiplication = $result")
        return result
    }

    private fun calculateDivision() : Int {
        val result =  numbers.first / numbers.second
        println("calculateDivision = $result")
        return result
    }

    fun modifyNumbers(a: Int = numbers.first, b: Int = numbers.second) {
        numbers = Pair(a, b)
        subjectCalc.onNext(this)
    }

    fun handleInput(inputLine: String?) {
        if (!inputLine.equals("exit")) {
            val pattern: Pattern = Pattern.compile("([a|b])(?:\\s)?=(?:\\s)?(\\d*)")
            var a: Int? = null
            var b: Int? = null
            val matcher: Matcher = pattern.matcher(inputLine)
            if (matcher.matches() && matcher.group(1) != null
                && matcher.group(2) != null
            ) {
                if (matcher.group(1).toLowerCase() == "a") {
                    a = matcher.group(2).toInt()
                } else if (matcher.group(1).toLowerCase() == "b") {
                    b = matcher.group(2).toInt()
                }
            }
            when {
                a != null && b != null -> modifyNumbers(a, b)
                a != null -> modifyNumbers(a = a)
                b != null -> modifyNumbers(b = b)
                else -> println("Invalid Input")
            }
        }
    }

    fun dispose(){
        disposables.dispose()
    }
}
