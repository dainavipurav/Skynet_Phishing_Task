package com.android.skynetphishingtask.fragments

import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.android.skynetphishingtask.R
import com.android.skynetphishingtask.models.ResponseModel
import com.android.skynetphishingtask.networks.APIClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CheckURLFragment : Fragment(R.layout.fragment_check_u_r_l), View.OnClickListener  {

    private lateinit var mConstraintLayoutCheckURL: ConstraintLayout

    private lateinit var mTextInputLayoutCheckURLEnterURL: TextInputLayout
    private lateinit var mTextInputEditTextCheckURLEnterURL: TextInputEditText

    private lateinit var mButtonCheckURLSubmit: Button

    private lateinit var mTextViewCheckURLResponse: TextView

    private lateinit var mURL: String

    private lateinit var mConnectivityManager: ConnectivityManager

    private var connected: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mConstraintLayoutCheckURL = view.findViewById(R.id.constraintLayoutCheckURL)

        mTextInputLayoutCheckURLEnterURL = view.findViewById(R.id.textInputLayoutCheckURLEnterURL)
        mTextInputEditTextCheckURLEnterURL =
            view.findViewById(R.id.textInputEditTextCheckURLEnterURL)

        mButtonCheckURLSubmit = view.findViewById(R.id.buttonCheckURLSubmit)
        mButtonCheckURLSubmit.setOnClickListener(this@CheckURLFragment)

        mTextViewCheckURLResponse = view.findViewById(R.id.textViewCheckURLResponse)

        mConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.buttonCheckURLSubmit -> {
                isURLValidated()
            }
        }
    }

    fun isNetworkConnected(): Boolean {
        if (
            mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED ||
            mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).state == NetworkInfo.State.CONNECTED
        ) {
            return true
        } else {
            return false
        }
    }

    fun isURLValidated() {
        connected = isNetworkConnected()
        mURL = mTextInputEditTextCheckURLEnterURL.text.toString()
        if (mURL.isEmpty()) {
            mTextInputLayoutCheckURLEnterURL.isErrorEnabled
            mTextInputLayoutCheckURLEnterURL.error = "Field can't be empty"
        } else {
            fetchData()
        }
    }

    fun fetchData(){
        if (connected) {

            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Sign Up")
            progressDialog.setMessage("Please wait, this may take while...")
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setCancelable(false)
            progressDialog.show()

            APIClient.instance.checkURL(
                mURL,
                "json"
            ).enqueue(object  : Callback<ResponseModel>{
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    progressDialog.dismiss()
                    val mResponseModel : ResponseModel = response.body() as ResponseModel
                    mTextViewCheckURLResponse.text = getString(
                        R.string.api_response_result,
                        mResponseModel.results.url,
                        mResponseModel.results.in_database.toString(),
                        mResponseModel.results.phish_id,
                        mResponseModel.results.phish_detail_page,
                        mResponseModel.results.verified.toString(),
                        mResponseModel.results.verified_at,
                        mResponseModel.results.valid.toString()
                    )
                }

                override fun onFailure(call: Call<ResponseModel>, t: Throwable) {
                    Toast.makeText(context, t.localizedMessage, Toast.LENGTH_LONG)
                        .show()
                }

            })

        } else {
            Toast.makeText(
                context,
                "Connection Error : Please Connect to the internet.",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}