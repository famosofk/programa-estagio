package com.example.olhovivoestagio.remote.models

import com.google.gson.annotations.SerializedName

class PrevisaoParada{

    @SerializedName("p")
    var paradaRecuperada = PrevisaoParadaLinha()

     @SerializedName("hr")
    var horarioRefencia: String = ""

}

class PrevisaoParadaLinha : ParadaBase() {
    @SerializedName("l")
    var arrayAlgumacoisa = arrayListOf<LinhasLocalizadasComPrevisaDeChegada>()
}


class ModelPrevisaoParadaVeiculo : VeiculoLocalizado() {
    @SerializedName("t")
    var horarioChegada: String = ""
}

class LinhasLocalizadasComPrevisaDeChegada : LinhasLocalizadas() {

    @SerializedName("vs")
    var arrayVeiculos = arrayListOf<ModelPrevisaoParadaVeiculo>()
}


class ModelPrevisaoParada : ParadaBase(){

    @SerializedName("vs")
    var relacaoVeiculos : ModelPrevisaoParadaVeiculo = ModelPrevisaoParadaVeiculo()

}
