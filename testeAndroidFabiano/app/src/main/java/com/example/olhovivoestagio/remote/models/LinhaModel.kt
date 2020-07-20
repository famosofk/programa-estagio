package com.example.olhovivoestagio.remote.models

import com.google.gson.annotations.SerializedName

class PosicaoModel{

    @SerializedName("l")
    var arraylinhas = arrayListOf<LinhasLocalizadasSemPrevisaoChegada>()


    @SerializedName("hr")
    var horarioReferencia: String = ""
}

class PosicaoVeiculoLinha {

    @SerializedName("vs")
    var arrayVeiculos = arrayListOf<VeiculoLocalizado>()

    @SerializedName("hr")
    var horarioReferencia : String = ""

}

open class LinhasLocalizadas{
    @SerializedName("c")
    var letreiroCompleto : String = ""
    @SerializedName("cl")
    var codigoLinha : Int = 0

    @SerializedName("sl")
      var sentidoLinha : Int = 0
      @SerializedName("lt0")
      var letreiroDestino : String = ""

    @SerializedName("lt1")
    var letreiroOrigem: String = ""

    @SerializedName("qv")
    var quantidadeVeiculos: Int = 0
}

class LinhasLocalizadasSemPrevisaoChegada : LinhasLocalizadas(){
    @SerializedName("vs")
    var arrayVeiculos = arrayListOf<VeiculoLocalizado>()
}

open class VeiculoLocalizado {
    @SerializedName("p")
    var prefixoVeiculo: Int = 0

    @SerializedName("a")
    var acessivelDeficientes: Boolean = false

    @SerializedName("py")
    var latitude: Double = 0.0

    @SerializedName("px")
    var longitude: Double = 0.0


    @SerializedName("ta")
    var horarioLocalizacao: String = ""

}

class LinhaModel{
    @SerializedName("cl")
    var codigoLinha: Int = 0

    @SerializedName("lt")
    var letreiroInicial: String = ""

    @SerializedName("sl")
    var letreiroFinal: Int = 0

    @SerializedName("tp")
    var terminalPrincipal: String = ""

    @SerializedName("ts")
    var terminalSecundario: String = ""

    @SerializedName("tl")
    var sentidoLinha : Int = 0
    @SerializedName("lc")
    var linhaCircular : Boolean = false


}
