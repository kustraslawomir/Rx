package slawomir.kustra.rx.chapters.third

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
                override fun onComplete() {
                    println("timer -> onComplete")
                }

                override fun onSubscribe(d: Disposable) {
                    println("timer -> onSubscribe")
                }

                override fun onNext(interval: Long) {
                    println("timer -> onNext $interval")
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
    }

    override fun onDestroy() {
        disposables.dispose()
        super.onDestroy()
    }

    private fun changeFlagState() {
        flag = !flag
        booleanSubject.onNext(flag)
    }
}
