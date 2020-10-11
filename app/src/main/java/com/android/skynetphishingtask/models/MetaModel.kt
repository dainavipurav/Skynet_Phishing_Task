package com.android.skynetphishingtask.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MetaModel (
    var timestamp : String,
    var serverid : String,
    var status : String,
    var requestid : String
    )