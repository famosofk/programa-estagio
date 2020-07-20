package com.example.olhovivoestagio.remote

import com.example.olhovivoestagio.remote.models.*

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface OlhoVivoService {

    @POST("Login/Autenticar")
    fun getAuthCookie(@Query("token") value: String): Call<Boolean>
    //Faz a autenticação do app

    @GET("Posicao")
    fun getPosicaoTodosVeiculos(@Header("Cookie") cookieValue: String): Call<PosicaoModel>
    //Retorna a posição de todos os veículos


    @GET("Parada/BuscarParadasPorLinha")
    fun getParadasLinhas(
        @Header("Cookie") cookieValue: String,
        @Query("codigoLinha") linhaValue: String
    ): Call<List<ParadaBuscar>>
    //Retorna as paradas que uma linha de onibus faz

    @GET("Parada/Buscar")
    fun getParada(
        @Header("Cookie") cookieValue: String,
        @Query("termosBusca") parada: String
    ): Call<List<ParadaBuscar>>
    //Retorna a localização de paradas


    @GET("Linha/Buscar")
    fun getLinha(@Header("Cookie") cookieValue:String, @Query("termosBusca") referenciaLinha:String) : Call<List<LinhaModel>>
    //Recupera os dados de uma linha de ônibus

    @GET("Posicao/Linha")
    fun getPosicaoVeiculosLinha(
        @Header("Cookie") cookieValue: String,
        @Query("codigoLinha") referenciaLinha: String
    ): Call<PosicaoVeiculoLinha>
    //Recupera a posição dos ônibus de uma linha

    @GET("Previsao/Parada")
    fun getPrevisaoChegadaParada(
        @Header("Cookie") cookieValue: String,
        @Query("codigoParada") referenciaParada: String
    ): Call<PrevisaoParada>
    //retorna todas as linhas que parem nessa parada

    @GET("Previsao")
    fun getPrevisaoDaLinhaNaChegada(
        @Header("Cookie") cookieValue: String,
        @Query("codigoParada") referenciaParada: String,
        @Query("codigoLinha") referenciaLinha: String
    ): Call<PrevisaoParada>
    //Mesma coisa da de cima, porém retorna só da linha especifica

    @GET("Corredor")
    fun getCorredores(@Header("Cookie") cookieValue: String): Call<List<Corredor>>
    //Informações de corredores

    @GET("Parada/BuscarParadasPorCorredor")
    fun getStopPointCorredor(
        @Header("Cookie") cookieValue: String,
        @Query("codigoCorredor") codigoCorredor: String
    ): Call<List<ParadaBuscar>>


}