package slawomir.kustra.rx

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import slawomir.kustra.rx.chapters.second.SecondChapter


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        secondChapter.setOnClickListener { startScreen(SecondChapter()) }

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}
