package com.stephen.android.kaola

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hello.setOnClickListener {
//            startActivity(Intent("swan.biz.readhub.activity.ReadHubMasterActivity.action"))
            startActivity(Intent("swan.biz.koala.activity.MzituMasterActivity.action"))
        }
    }
}
