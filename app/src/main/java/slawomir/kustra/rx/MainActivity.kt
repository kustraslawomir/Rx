package slawomir.kustra.rx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import slawomir.kustra.rx.chapters.fourth.FourthScreen
import slawomir.kustra.rx.chapters.second.SecondChapter
import slawomir.kustra.rx.chapters.third.ThirdChapter
import slawomir.kustra.rx.subjects.SubjectScreen


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secondChapter.setOnClickListener { startScreen(SecondChapter()) }
        thirdChapter.setOnClickListener { startScreen(ThirdChapter()) }
        subjects.setOnClickListener { startScreen(SubjectScreen()) }
        fourthChapter.setOnClickListener { startScreen(FourthScreen()) }
    }
}
