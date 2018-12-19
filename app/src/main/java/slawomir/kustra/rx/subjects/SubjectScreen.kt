package slawomir.kustra.rx.subjects

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import slawomir.kustra.rx.R

class SubjectScreen : AppCompatActivity() {

    private val compositeSubscription = ArrayCompositeSubscription(1)

    /*
    ---------- Subject -----------
    Subject is a sort of bridge that acts both as an observer and as an Observable.
    Because it is observer it can subscribe to one or many Observables,
    and because it is and Observable it can emit new items.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_screen)
        /*
        ----------  PUBLISH SUBJECT ----------
       Publish subject emits items only after subject is subscribed to observer.
       */
        val publishSubject: PublishSubject<String> = PublishSubject.create()
        publishSubject.onNext("1")
        publishSubject.onNext("2")
        publishSubject.subscribe(object : Observer<String> {
            override fun onComplete() {
                println("publishSubject onComplete")
            }

            override fun onSubscribe(disposable: Disposable) {
                println("publishSubject onSubscribe")
            }

            override fun onNext(value: String) {
                println("publishSubject onNext: $value")
            }

            override fun onError(e: Throwable) {
                println("publishSubject onError: ${e.message}")
            }
        })
        publishSubject.onNext("3")
        publishSubject.onComplete()

        /*
      ----------  REPLAY SUBJECT ----------
       Even if on next called is before replay subject subscribe(), items will be emitted and
       invoked in onNext(cat: Cat) method.
        */

        val replaySubject: ReplaySubject<Cat> = ReplaySubject.create()

        replaySubject.onNext(Cat("Kitku", false))
        replaySubject.onNext(Cat("Bonifacy", false))
        replaySubject.onNext(Cat("Klakier", true))

        replaySubject.subscribe(object : Observer<Cat> {
            override fun onComplete() {
                println("replaySubject on complete")
            }

            override fun onSubscribe(disposable: Disposable) {
                println("replaySubject onSubscribe")
            }

            override fun onNext(cat: Cat) {
                println("replaySubject cat: $cat")
            }

            override fun onError(throwable: Throwable) {
                println("replaySubject error: ${throwable.message}")
            }
        })

        replaySubject.onNext(Cat("Kocurek", true))
        replaySubject.onComplete()

        /*
       ---------- BEHAVIOUR SUBJECT ----------
            Behaviour subjects works similar to publish subjects but it OVERPLAY the last emitted
            item for NEW OBSERVER (subscription)
         */
        val behaviourSubject = BehaviorSubject.create<Int>()

        behaviourSubject.onNext(1)

        behaviourSubject.subscribe(object : Observer<Int> {
            override fun onComplete() {
                println("behaviourSubject onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                println("behaviourSubject onSubscribe")
            }

            override fun onNext(value: Int) {
                println("behaviourSubject $value")
            }

            override fun onError(throwable: Throwable) {
                println("behaviourSubject onError: ${throwable.message}")
            }
        })
        behaviourSubject.onNext(2)
        behaviourSubject.onNext(3)
        behaviourSubject.onNext(4)

        /*
        After this subscribe behaviour subject will emit last one -> value "4"
         */
        behaviourSubject.subscribe(object : Observer<Int> {
            override fun onComplete() {
                println("behaviourSubject2 onComplete")
            }

            override fun onSubscribe(d: Disposable) {
                println("behaviourSubject2 onSubscribe")
            }

            override fun onNext(value: Int) {
                println("behaviourSubject2 $value")
            }

            override fun onError(throwable: Throwable) {
                println("behaviourSubject2 onError: ${throwable.message}")
            }
        })
        behaviourSubject.onComplete()
    }
}
