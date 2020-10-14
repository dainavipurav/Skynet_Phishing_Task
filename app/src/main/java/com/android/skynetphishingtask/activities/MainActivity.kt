package com.android.skynetphishingtask.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.skynetphishingtask.R

class MainActivity : AppCompatActivity() {
    private lateinit var mNavControllerMain: NavController
    lateinit var mBundle : Bundle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mIntent : Intent = intent
        val action : String = mIntent.action.toString()
        val mType : String = mIntent.type.toString()

        if (Intent.ACTION_SEND.equals(action) && mType != null) {
            if ("text/plain".equals(mType)) {
                val mSharedText: String = mIntent.getStringExtra(Intent.EXTRA_TEXT)
                if (mSharedText != null) {
                    val mNavController = this@MainActivity.findNavController(R.id.nav_host_fragment_container)
                    mBundle = Bundle()
                    mBundle.putString(getString(R.string.text_key_shared_text),mSharedText,)
                    mNavController.setGraph(mNavController.graph,mBundle)
                }
            }
        }
    }
}