package slawomir.kustra.rx.chapters.fourth

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.Flowable
import io.reactivex.FlowableSubscriber
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.reactivestreams.Subscription
import slawomir.kustra.rx.R

class FourthScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fourth_screen)

        val observable = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val subject = BehaviorSubject.create<Int>()

        subject
            .observeOn(Schedulers.computation())
            .subscribe(object : Observer<Int> {
                override fun onComplete() {
                    println("onComplete")
                }

                override fun onSubscribe(disposable: Disposable) {
                    println("onSubscribe")
                }

                override fun onNext(value: Int) {
                    println("onNext $value")
                    runBlocking { delay(200) }
                }

                override fun onError(e: Throwable) {
                    println("onError ${e.message}")
                }
            })

        subject
            .observeOn(Schedulers.computation())
            .subscribe(object : Observer<Int> {
                override fun onComplete() {
                    println("onComplete2")
                }

                override fun onSubscribe(disposable: Disposable) {
                    println("onSubscribe2")
                }

                override fun onNext(value: Int) {
                    println("onNext2 $value")
                }

                override fun onError(e: Throwable) {
                    println("onError 2 ${e.message}")
                }
            })
        observable.subscribe(subject)
        runBlocking { delay(2000) }

        /*
        Hot observable (emitted once for both of the observers) is still OK!
        However for the first observer computation took long, the emission got queued!
         */

        //Another example
        val observable2 = Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
        observable2
            .map { SampleObject(it) }
            .observeOn(Schedulers.computation())
            .subscribe(object : Observer<SampleObject> {
                override fun onComplete() {
                    println("observable 2 onComplete ")
                }

                override fun onSubscribe(d: Disposable) {
                    println("observable 2 onSubscribe ")
                }

                override fun onNext(t: SampleObject) {
                    println("observable 2 onNext ${t.value}")
                    runBlocking { delay(200) }
                }

                override fun onError(e: Throwable) {
                    println("observable 2 onError ${e.message}")
                }
            })

        runBlocking { delay(2000) }

        /*
        output of above example:

        SampleObject constructor() with value 1
        ...
         SampleObject constructor() with value 9
         ->
         observable 2 onNext 1
         ...
         observable 2 onNext 9

         First was printed all .map{} object creation constructor, after that
         we printed values from onNextMethod.
         So, the problem is that the emissions get queued in the consumer, while the consumer is
         busy processing previous emissions by the producer

         Solution:
         We need to tell producer of data to wait!
         Out Consumer can't map objects on time (because of delay - fake heave operation)
         To achieve this, we could use Flowable observable to handle Backpressure.

        When consumer is taking time to process data, the emitted items may wait in buffer!

        Example:
         */

        val disposable = Flowable.range(1, 1000)
            .map { SampleObject(it) }
            .observeOn(Schedulers.computation())
            .subscribe({
                println("Flowable onNext ${it.value}")
                runBlocking { delay(50) }
            }, { println("error! ${it.message}") })
        runBlocking { delay(2000) }

        disposable.dispose()

        /*
        Flowable, instead of emitting all the items, emitted few
        items in a chunk, waited for the consumer to coup up then again continued, and completed
        in an interleaved manner.
         */
    }
}
