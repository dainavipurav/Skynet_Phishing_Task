package com.android.skynetphishingtask.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.webkit.URLUtil
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.android.skynetphishingtask.R
import com.android.skynetphishingtask.models.ResponseModel
import com.android.skynetphishingtask.networks.APIClient
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CheckURLFragment : Fragment(R.layout.fragment_check_u_r_l), View.OnClickListener {

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

        val mArgs: CheckURLFragmentArgs by navArgs()
        val text = mArgs.sharedText


        mConstraintLayoutCheckURL = view.findViewById(R.id.constraintLayoutCheckURL)

        mTextInputLayoutCheckURLEnterURL = view.findViewById(R.id.textInputLayoutCheckURLEnterURL)
        mTextInputEditTextCheckURLEnterURL =
            view.findViewById(R.id.textInputEditTextCheckURLEnterURL)

        mButtonCheckURLSubmit = view.findViewById(R.id.buttonCheckURLSubmit)
        mButtonCheckURLSubmit.setOnClickListener(this@CheckURLFragment)

        mTextViewCheckURLResponse = view.findViewById(R.id.textViewCheckURLResponse)

        mConnectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (!text.equals("")){
            mTextInputEditTextCheckURLEnterURL.setText(text, TextView.BufferType.EDITABLE)
        }
        else{
            mTextInputEditTextCheckURLEnterURL.setText("", TextView.BufferType.EDITABLE)
        }


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
            mTextInputLayoutCheckURLEnterURL.error =
                getString(R.string.text_string_field_cant_be_empty)
        } else {
            if (URLUtil.isValidUrl(mURL) && Patterns.WEB_URL.matcher(mURL).matches()) {
                mTextInputLayoutCheckURLEnterURL.isErrorEnabled = false
                fetchData()
            } else {
                mTextInputLayoutCheckURLEnterURL.isErrorEnabled
                mTextInputLayoutCheckURLEnterURL.error =
                    getString(R.string.text_string_please_enter_valid_url)
            }
        }
    }

    fun fetchData() {
        if (connected) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle(getString(R.string.text_title_extracting_details))
            progressDialog.setMessage(getString(R.string.text_sring_please_wait_this_may_take_while))
            progressDialog.setCanceledOnTouchOutside(false)
            progressDialog.setCancelable(false)
            progressDialog.show()

            val imm = context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

            APIClient.instance.checkURL(
                mURL,
                "json"
            ).enqueue(object : Callback<ResponseModel> {
                override fun onResponse(
                    call: Call<ResponseModel>,
                    response: Response<ResponseModel>
                ) {
                    progressDialog.dismiss()
                    val mResponseModel: ResponseModel = response.body() as ResponseModel
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

    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}