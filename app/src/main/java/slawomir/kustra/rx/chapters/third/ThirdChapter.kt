package slawomir.kustra.rx.chapters.third

import android.annotation.SuppressLint
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_third_chapter.*
import slawomir.kustra.rx.R
import java.util.concurrent.TimeUnit

class ThirdChapter : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private val booleanSubject: Subject<Boolean> = PublishSubject.create<Boolean>()

    private var flag = false
    private val list: List<Int> = listOf(0, 1, 2, 3, 4, 5)
    private val observable: Observable<Boolean> = Observable.just(flag)
    private val listObservable: Observable<List<String>> = Observable.just(listOf("one", "two", "three"))
    private val errorObservable: Observable<String> =
        Observable.create<String> {
            it.onNext("test1")
            it.onNext("test2")
            it.onNext("test3")
            it.onError(Exception("Error!"))
        }

    private val observableFromArray: Observable<List<Int>> =
        Observable.fromArray<List<Int>>(list)

    private val listToObservable: Observable<Int> = list.toObservable()
    private val justObservable = Observable.just(list)

    private val observer: Observer<Any> = object : Observer<Any> {
        override fun onComplete() {
            println("onComplete")
        }

        override fun onSubscribe(disposable: Disposable) {
            println("onSubscribe")
        }

        override fun onNext(next: Any) {
            println("onNext $next")
        }

        override fun onError(throwable: Throwable) {
            println("onError! ${throwable.message}")
        }
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_chapter)

        changeFlagState.setOnClickListener { changeFlagState() }

        observable.subscribe(observer)

        disposables.add(booleanSubject
            .subscribe(
                { println("boolean subject: $flag") },
                { println("subject error: ${it.message}") }
            ))

        listObservable.subscribe(observer)
        observableFromArray.subscribe(observer)
        errorObservable.subscribe(observer)
        listToObservable.subscribe(observer)
        justObservable.subscribe(observer)

        Observable.just(5).subscribe(observer)
        Observable.just("just").subscribe(observer)
        Observable.just("just", 1, "just 2", 2).subscribe(observer)

        Observable.range(0, 10).subscribe(observer)

        Observable.empty<String>().subscribe(observer)

        Observable.timer(5, TimeUnit.SECONDS)
            .subscribe(observer)

        Observable.interval(1, TimeUnit.SECONDS)
            .subscribe(object : Observer<Long> {
                lateinit var disposable: Disposable
                override fun onComplete() {
                    println("timer -> onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    disposable = d
                    println("timer -> onSubscribe")
                }

                override fun onNext(interval: Long) {
                    println("timer -> onNext $interval")
                    if (interval == 10L)
                        disposable.dispose()
                }

                override fun onError(e: Throwable) {
                    println("timer -> onError ${e.message}")
                }
            })

        disposables.add(
            Observable.fromArray<List<Int>>(list)
                .subscribe({
                    println("on next: $it")
                }, {
                    println("error: ${it.message}")
                }, {
                    println("on completed")
                })
        )

        disposables.add(connectableObservable.subscribe({
            println("connectableObservable 1 on next: $it")
        }, {
            println("connectableObservable 1 error: ${it.message}")
        }, {
            println("connectableObservable 1 on completed")
        }))

        disposables.add(connectableObservable.map { it*2 }
            .subscribe({
                println("connectableObservable 2 on next: $it")
            }, {
                println("connectableObservable 2 error: ${it.message}")
            }, {
                println("connectableObservable 2 on completed")
            }))

        connectableObservable.connect()

        connectableObservable.subscribe {
            println("connectableObservable 3 $it") //!This HOT observable -> observer onComplete() method will not called because .connect() is above!
            //Emissions will not be received after subsribe().
        }
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun changeFlagState() {
        flag = !flag
        booleanSubject.onNext(flag)
    }

    /*
    ------- Hot/Cold Observables -------

    Cold observables needs subscribe to start emitting items.
    It doesn't need subscriptions to start emission. Observables are like TV channels
    - they continue broadcasting (emitting)items/content irrespective of whether anyone is watching.

     ConnectableObservable

     It is example of Hot observable. It can turn any observable (hot/cold) to Hot observable.
     It doesn't starts emitting items on subscribe call; instead it gets activated after you call 'connect' method.

    "The main purpose of ConnectableObservable is for Observables with multiple
    subscriptions to connect all subscriptions of an Observable together so that they can react
    to a single push;"
     */

    private val connectableObservable = listOf(1, 2, 3, 4).toObservable().publish()
}
