package slawomir.kustra.rx.subjects

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observable
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription
import io.reactivex.subjects.ReplaySubject
import slawomir.kustra.rx.R

class SubjectScreen : AppCompatActivity() {

    private val compositeSubscription = ArrayCompositeSubscription(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_screen)

        val replaySubject: ReplaySubject<Cat> = ReplaySubject.create()
        replaySubject.onNext(Cat("Kitku", false))
        replaySubject.onNext(Cat("Bonifacy", false))
        replaySubject.onNext(Cat("Klakier", true))

        val disposable = replaySubject.subscribe(object : Observable<Cat> {

        })
    }
}
