package com.example.olhovivoestagio.remote.models

import com.google.gson.annotations.SerializedName

open class ParadaBase {
    @SerializedName("cp")
    var codigoParada : String = ""
    @SerializedName("np")
    var nomeParada : String = ""
    @SerializedName("py")
    var latitudeParada : Double = 0.0
    @SerializedName("px")
    var longitudeParada : Double = 0.0
}

class ParadaBuscar : ParadaBase(){

    @SerializedName("ed")
    var enderecoParada : String = ""
}


