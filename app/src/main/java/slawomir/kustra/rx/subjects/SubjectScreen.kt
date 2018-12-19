package slawomir.kustra.rx.subjects

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.subscriptions.ArrayCompositeSubscription
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

        val replaySubject: ReplaySubject<Cat> = ReplaySubject.create()

        replaySubject.onNext(Cat("Kitku", false))
        replaySubject.onNext(Cat("Bonifacy", false))
        replaySubject.onNext(Cat("Klakier", true))

        /*
        Even if on next called is before replay subject subscribe(), items will be emitted and
        invoked in onNext(cat: Cat) method.
         */
        replaySubject.subscribe(object : Observer<Cat> {
            override fun onComplete() {
            println("on complete replay subject")
            }

            override fun onSubscribe(disposable: Disposable) {
                println("onSubscribe replay subject")
            }

            override fun onNext(cat: Cat) {
                println("replay cat: $cat")
            }

            override fun onError(throwable: Throwable) {
                println("replay error: ${throwable.message}")
            }
        })

        replaySubject.onNext(Cat("Kocurek", true))
        replaySubject.onComplete()
    }
}
