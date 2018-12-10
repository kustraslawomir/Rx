package slawomir.kustra.rx

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import io.reactivex.Observable
import io.reactivex.ObservableSource
import java.util.concurrent.Callable
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
                Log.e("defer", it.toString())
            }
        defer.subscribe()
    }

    private fun callFromCallableOperator() {

    }
}
