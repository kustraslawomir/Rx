package slawomir.kustra.rx.chapters.second.calculator

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.util.regex.Matcher
import java.util.regex.Pattern

class ReactiveCalculator(a: Int, b: Int) {

    private val subjectAdd: Subject<Pair<Int, Int>> =
        PublishSubject.create()
    private val subjectSub: Subject<Pair<Int, Int>> =
        PublishSubject.create()
    private val subjectMult: Subject<Pair<Int, Int>> =
        PublishSubject.create()
    private val subjectDiv: Subject<Pair<Int, Int>> =
        PublishSubject.create()
    private val subjectCalc: Subject<ReactiveCalculator> =
        PublishSubject.create()
    private var numbers: Pair<Int, Int> = Pair(0, 0)

    init {
        numbers = Pair(a, b)
        subjectAdd.map { it.first + it.second }.subscribe{ println("Add = $it") }
        subjectSub.map{ it.first - it.second }.subscribe{ println("Substract = $it") }
        subjectMult.map { it.first * it.second }.subscribe { println("Multiply = $it") }
        subjectDiv.map { it.first / (it.second * 1.0) }.subscribe { println("Divide = $it") }

        subjectCalc.subscribe {
            with(it) {
                calculateAddition()
                calculateSubstraction()
                calculateMultiplication()
                calculateDivision()
            }
        }
        subjectCalc.onNext(this)
    }

    fun calculateAddition() {
        subjectAdd.onNext(numbers)
    }

    fun calculateSubstraction() {
        subjectSub.onNext(numbers)
    }

    fun calculateMultiplication() {
        subjectMult.onNext(numbers)
    }

    fun calculateDivision() {
        subjectDiv.onNext(numbers)
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
                if (matcher.group(1).toLowerCase().equals("a")) {
                    a = matcher.group(2).toInt()
                } else if (matcher.group(1).toLowerCase().equals("b")) {
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
}
