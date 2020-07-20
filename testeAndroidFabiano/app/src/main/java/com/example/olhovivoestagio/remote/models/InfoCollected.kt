package com.example.olhovivoestagio.remote.models

import java.io.Serializable

data class InfoCollected(
    var tipo: String = "",

    var codigo: String = "",
    var nome: String = "",
    var desc: String = "",
    var lat: Double = 0.0,
    var lon: Double = 0.0
) : Serializable {

    companion object {
        const val TIPO_PARADA = "Parada"
        const val TIPO_VEICULO = "Veículo"
        const val TIPO_LINHA = "Linha"
        const val TIPO_CORREDOR = "Corredor"
        const val TIPO_PARADA_CORREDOR = "ParadaCorredor"
    }

}

class ListInfoCollected : Serializable {

    var lista = mutableListOf<InfoCollected>()

}