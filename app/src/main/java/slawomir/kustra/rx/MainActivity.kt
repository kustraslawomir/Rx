package slawomir.kustra.rx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Function3
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val source = Observable.interval(200, TimeUnit.MILLISECONDS).take(10)
    private val stringListSource = arrayListOf("liquid", "AOC", "window", "sky", "future")
    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callDeferOperators()
        callFromCallableOperator()
        changeStringListToReactiveDataStream()
        chainObservablesWithZipOperator(getSimpleObservable("cat"), getSimpleObservable("dog"))

    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }

    private fun callDeferOperators() {
        val defer: Observable<Long> = Observable.defer { source }
            .doOnNext {
                Log.e("Rx defer", it.toString())
            }
        defer.subscribe()

        val defer2: Observable<Any> = Observable.defer {
            try {
                Observable.just(fakeHeavyMethod())
            } catch (e: Exception) {
                Observable.error<Throwable>(e)
            }
        }
        defer2.doOnComplete {
            Log.e("Rx defer2 ", "completed")
        }.subscribe()
    }

    private fun callFromCallableOperator() {
        val fromCallable: Observable<Completable> = Observable.fromCallable(::fakeHeavyMethod)
        fromCallable
            .doOnComplete {
                Log.e("Rx fromCallable ", "completed")
            }
            .subscribe()
    }

    private fun changeStringListToReactiveDataStream() {
        disposables.add(
            Observable.just("one", "two", "three", "four", "five")
                .subscribe({
                    Log.e("Rx .just value: ", it)
                }, {
                    Log.e("Rx .just error: ", it.message)
                })
        )

        disposables.add(
            Observable.fromIterable(stringListSource).subscribe({
                Log.e("Rx .fromIterable: ", it)
            }, {
                Log.e("Rx .fromIterable e: ", it.message)
            })
        )
    }

    private fun fakeHeavyMethod(): Completable {
        for (i in 0..2999999999) {

        }
        return Completable.complete()
    }

    private fun chainObservablesWithZipOperator(
        simpleObservable: Observable<String>,
        simpleObservable1: Observable<String>,
        simpleObservable2: Observable<String>
    ) {
        disposables.add(
            Observable.zip(
                simpleObservable,
                simpleObservable1,
                simpleObservable2,
                Function3<String, String, String, String> { t1, t2, t3 -> "$t1 $t2 $t3" })
                .subscribe({ Log.e("Rx zip x3 result: ", it) }, {
                    Log.e("Rx zip x3 error: ", it.message)
                })
        )

    }

    private fun getSimpleObservable(value: String): Observable<String> = Observable.just(value)

}
