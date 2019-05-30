package com.example.matiasgualino.emvdecoder

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    val emvData = "00020101021143520016com.mymerchantar0128http://localhost:3344/p/931850150011203675539615204970053030325802AR5921SERGIO MATIAS GUALINO6004CABA63044edf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        emv_value.text = emvData

        decode_button.setOnClickListener {
            decodeEMVQR(emvData)?.let {
                val result = it.map { "${it.key} = ${it.value}" }.joinToString("\n")
                decode_result.text = result
            }
        }
    }

}
