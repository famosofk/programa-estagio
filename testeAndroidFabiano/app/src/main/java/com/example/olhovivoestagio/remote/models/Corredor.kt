package com.example.olhovivoestagio.remote.models

import com.google.gson.annotations.SerializedName

class Corredor {

    @SerializedName("cc")
    var codigoCorredor: Int = 0

    @SerializedName("nc")
    var nomeCorredor: String = ""

}