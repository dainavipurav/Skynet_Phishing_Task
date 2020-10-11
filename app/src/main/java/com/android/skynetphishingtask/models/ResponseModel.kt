package com.android.skynetphishingtask.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseModel(
    var meta: MetaModel,
    var results: ResultsModel
)