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

    private var flag = false

    private val booleanSubject: Subject<Boolean> = PublishSubject.create<Boolean>()

    private val observer: Observer<Boolean> = object : Observer<Boolean> {
        override fun onComplete() {
            println("onComplete | flag value: $flag")
        }

        override fun onSubscribe(d: Disposable) {
            println("onSubscribe")
        }

        override fun onNext(t: Boolean) {
            println("onNext")
        }

        override fun onError(e: Throwable) {
            println("onError")
        }
    }

    private val observable: Observable<Boolean> = Observable.just(flag)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_chapter)

        observable.subscribe(observer)

        disposables.add(booleanSubject
            .map { print("map subject: $flag") }.subscribe({
                print("boolean subject: $flag")
            }, {
                print("subject error: ${it.message}")
            }))

        changeFlagState.setOnClickListener { changeFlagState() }
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
