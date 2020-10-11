package com.android.skynetphishingtask.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResultsModel (
    var url: String,
    var in_database: Boolean,
    var phish_id: String,
    var phish_detail_page: String,
    var verified: Boolean ,
    var verified_at: String,
    var valid: Boolean = false
)