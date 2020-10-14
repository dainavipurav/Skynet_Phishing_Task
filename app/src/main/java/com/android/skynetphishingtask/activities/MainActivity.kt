package com.android.skynetphishingtask.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.android.skynetphishingtask.R
import java.net.URI

class MainActivity : AppCompatActivity() {
    private lateinit var mNavControllerMain: NavController
    lateinit var mBundle : Bundle
    lateinit var mURL :String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mUri : Uri? = intent.data
        if (mUri !=null){
            /*var mParams : List<String> = mUri.pathSegments
            var mId : String = mParams.get(mParams.size - 1)*/
            mURL = mUri.toString()
        }
        else{
            mURL =""
        }
        val mNavController = this@MainActivity.findNavController(R.id.nav_host_fragment_container)
        mBundle = Bundle()
        mBundle.putString(getString(R.string.text_key_shared_text),mURL,)
        mNavController.setGraph(mNavController.graph,mBundle)
    }
}