package slawomir.kustra.rx.`fun`

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.Observer
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import slawomir.kustra.rx.R

class FunScreen : AppCompatActivity() {

    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fun_screen)
        getResults()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::disposable.isInitialized)
            disposable.dispose()
    }

    private fun getResults() {
        /*
        publish shares observable
         */
        getNetworkResults()
            .publish { network ->
                Observable.merge(network, getCacheResults()
                        .takeUntil(network)
                )
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onComplete() {
                    println("onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    println("onSubscribe")
                }

                override fun onNext(value: String) {
                    println("onNext $value")
                }

                override fun onError(e: Throwable) {
                    println("onError ${e.message}")
                }
            })
    }

    private fun getNetworkResults(): Observable<String> {
        println("getNetworkResults")
        runBlocking { delay(2000) }
        println("return network response")
        return Observable.just("network")
    }

    private fun getCacheResults(): Observable<String> {
        return Observable.just("cache")
    }
}
