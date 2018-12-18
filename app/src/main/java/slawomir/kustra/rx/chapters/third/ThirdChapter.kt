package slawomir.kustra.rx.chapters.third

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.android.synthetic.main.activity_third_chapter.*
import slawomir.kustra.rx.R

class ThirdChapter : AppCompatActivity() {

    private val disposables = CompositeDisposable()

    private val booleanSubject: Subject<Boolean> = PublishSubject.create<Boolean>()

    private var flag = false
    private val list: List<Int> = listOf(0, 1, 2, 3, 4, 5)
    private val observable: Observable<Boolean> = Observable.just(flag)
    private val listObservable: Observable<List<String>> = Observable.just(arrayListOf("one", "two", "three"))

    private val observableFromArray: Observable<List<Int>> =
        Observable.fromArray<List<Int>>(list)

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
            println("onError")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_chapter)

        observable.subscribe(observer)

        disposables.add(booleanSubject
            .subscribe(
                { println("boolean subject: $flag") },
                { println("subject error: ${it.message}") }
            ))

        changeFlagState.setOnClickListener { changeFlagState() }

        listObservable.subscribe(observer)
        observableFromArray.subscribe(observer)
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
