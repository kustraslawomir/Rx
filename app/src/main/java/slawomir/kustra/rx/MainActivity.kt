package slawomir.kustra.rx

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val source = Observable.interval(200, TimeUnit.MILLISECONDS).take(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        callDeferOperator()
        callFromCallableOperator()

    }

    private fun callDeferOperator() {
        val defer: Observable<Long> = Observable.defer { source }
            .doOnNext {
                Log.e("Rx defer", it.toString())
            }
        defer.subscribe()

        val defer2: Observable<Any> = Observable.defer {
            try {
                Observable.just(fakeHeavyMethod())
            } catch (e: java.lang.Exception) {
                Observable.error<Throwable>(e)
            }
        }
        defer2.doOnComplete {
            Log.e("Rx defer2 ", "completed")
        }.subscribe()
    }


    private fun fakeHeavyMethod(): Completable {
        for (i in 0..9999999999) {

        }
        return Completable.complete()
    }
}
