package com.example.olhovivoestagio.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.olhovivoestagio.remote.OlhoVivoService
import com.example.olhovivoestagio.remote.RetrofitInstance
import com.example.olhovivoestagio.remote.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val retrofit = RetrofitInstance.createService(OlhoVivoService::class.java)
    private var _cookie = MutableLiveData<String>()
    private val cookie: LiveData<String>
        get() = _cookie
    private var _busca = MutableLiveData<Boolean>()
    val busca: LiveData<Boolean>
        get() = _busca
    private var _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate
    private var _updateRecycler = MutableLiveData<Boolean>()
    val updateRecycler: LiveData<Boolean>
        get() = _updateRecycler
    var dataCollected = ListInfoCollected()

    var posicaoVeiculosLinha = false
    var posicaoParadasLinha = false


    fun realizeAuth(apikey: String) {
        retrofit.getAuthCookie(apikey).enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                _cookie.value = t.message
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                _cookie.value = response.headers().get("Set-cookie")
            }
        })
    }

    fun getStopPointOfLine(linha: String, conjunto: Boolean = false) {
        retrofit.getParadasLinhas(cookie.value!!, linha)
            .enqueue(object : Callback<List<ParadaBuscar>> {
                override fun onFailure(call: Call<List<ParadaBuscar>>, t: Throwable) {
                    Log.e("erro ", t.message.toString())
                }

                override fun onResponse(
                    call: Call<List<ParadaBuscar>>,
                    response: Response<List<ParadaBuscar>>
                ) {
                    val res = response.body()
                    if (res != null) {
                        for (item in res) {
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_PARADA,
                                    item.codigoParada,
                                    item.nomeParada,
                                    item.enderecoParada,
                                    item.latitudeParada,
                                    item.longitudeParada
                                )
                            )
                        }
                        if (conjunto) {
                            posicaoParadasLinha = true
                            verificarPosicaoVeiculosParada()
                        } else {
                            timeToNavigate()
                        }
                    }
                }
            })
    }

    fun getAllVehiclesPosition() {
        retrofit.getPosicaoTodosVeiculos(cookie.value!!).enqueue(object : Callback<PosicaoModel> {
            override fun onFailure(call: Call<PosicaoModel>, t: Throwable) {
                Log.e("resposta ", t.message.toString())
            }

            override fun onResponse(
                call: Call<PosicaoModel>, response: Response<PosicaoModel>
            ) {
                val res = response.body()
                if (res != null) {
                    val linhasLocalizadas = res.arraylinhas
                    for (lines in linhasLocalizadas) {
                        for (item in lines.arrayVeiculos) {
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_VEICULO,
                                    item.prefixoVeiculo.toString(),
                                    lines.codigoLinha.toString(),
                                    "Acessível: ${item.acessivelDeficientes}",
                                    item.latitude,
                                    item.longitude
                                )
                            )
                        }
                    }
                    timeToNavigate()
                }
            }
        })
    }

    fun getVehiclePositionOfLine(linha: String) {
        retrofit.getPosicaoVeiculosLinha(cookie.value!!, linha)
            .enqueue(object : Callback<PosicaoVeiculoLinha> {
                override fun onFailure(call: Call<PosicaoVeiculoLinha>, t: Throwable) {
                    Log.e("resposta ", t.message.toString())
                }

                override fun onResponse(
                    call: Call<PosicaoVeiculoLinha>, response: Response<PosicaoVeiculoLinha>
                ) {
                    val res = response.body()
                    if (res != null) {

                        for (item in res.arrayVeiculos) {
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_VEICULO,
                                    item.prefixoVeiculo.toString(),
                                    linha,
                                    "Acessível: ${item.acessivelDeficientes}",
                                    item.latitude,
                                    item.longitude
                                )
                            )
                        }
                        posicaoVeiculosLinha = true
                        verificarPosicaoVeiculosParada()
                    }
                }
            })
    }

    fun getStopPoint(parada: String) {
        retrofit.getParada(cookie.value!!, parada).enqueue(object : Callback<List<ParadaBuscar>> {
            override fun onFailure(call: Call<List<ParadaBuscar>>, t: Throwable) {
                Log.e("resposta ", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<ParadaBuscar>>,
                response: Response<List<ParadaBuscar>>
            ) {

                val res = response.body()
                if (res != null) {
                    for(item in res){
                        dataCollected.lista.add(InfoCollected(InfoCollected.TIPO_PARADA,"Código: "+ item.codigoParada,"Nome: " + item.nomeParada, item.enderecoParada, item.latitudeParada, item.longitudeParada))
                    }

                    timeToUpdateRecycler()
                }
            }
        })
    }

    fun getLineData(linha: String) {
        retrofit.getLinha(cookie.value!!, linha).enqueue(object : Callback<List<LinhaModel>> {
            override fun onFailure(call: Call<List<LinhaModel>>, t: Throwable) {
                Log.e("resposta ", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<LinhaModel>>,
                response: Response<List<LinhaModel>>
            ) {
                val res = response.body()
                if (res != null) {
                    for(item in res){
                        dataCollected.lista.add(InfoCollected(InfoCollected.TIPO_LINHA, "Código: " + item.codigoLinha.toString(), "Nome: " + item.letreiroInicial + item.letreiroFinal,item.terminalPrincipal + " - " + item.terminalSecundario))
                    }
                timeToUpdateRecycler()
                }
            }
        })
    }

    fun getVehiclesArrivalAtStopPoint(parada: String) {
        retrofit.getPrevisaoChegadaParada(cookie.value!!, parada)
            .enqueue(object : Callback<PrevisaoParada> {
                override fun onFailure(call: Call<PrevisaoParada>, t: Throwable) {
                    Log.e("resposta ", t.message.toString())
                }

                override fun onResponse(
                    call: Call<PrevisaoParada>,
                    response: Response<PrevisaoParada>
                ) {
                    val res = response.body()
                    if (res != null) {
                        for (line in res.paradaRecuperada.arrayAlgumacoisa) {
                            var descricao = ""
                            for (veiculo in line.arrayVeiculos) {
                                descricao += veiculo.prefixoVeiculo.toString() + ": " + veiculo.horarioChegada + "\n"
                                // dataCollected.lista.add(InfoCollected(InfoCollected.TIPO_VEICULO, veiculo.prefixoVeiculo.toString(), line.letreiroOrigem + line.letreiroDestino, veiculo.acessivelDeficientes.toString(), veiculo.latitude, veiculo.longitude))
                            }
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_LINHA,
                                    "Código: " + line.codigoLinha.toString(),
                                    "Letreiro: " + line.letreiroCompleto,
                                    descricao
                                )
                            )

                        }

                        timeToUpdateRecycler()
                    }
                }
            })
    }

    fun getLineArrivalAtStopPoint(parada: String, linha: String) {
        retrofit.getPrevisaoDaLinhaNaChegada(cookie.value!!, parada, linha)
            .enqueue(object : Callback<PrevisaoParada> {
                override fun onFailure(call: Call<PrevisaoParada>, t: Throwable) {
                    Log.e("Erro:", t.message.toString())
                }

                override fun onResponse(
                    call: Call<PrevisaoParada>,
                    response: Response<PrevisaoParada>
                ) {
                    val res = response.body()
                    if (res != null) {
                        val previsaoParada = res.paradaRecuperada

                        if (previsaoParada != null) {
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_PARADA,
                                    "Código: " + previsaoParada.codigoParada,
                                    "Nome: " + previsaoParada.nomeParada,
                                    "",
                                    previsaoParada.latitudeParada,
                                    previsaoParada.longitudeParada
                                )
                            )
                            for (line in previsaoParada.arrayAlgumacoisa) {
                                var descricao = ""
                                for (veiculo in line.arrayVeiculos) {
                                    descricao += veiculo.prefixoVeiculo.toString() + ": " + veiculo.horarioChegada + "\n"
                                }
                                dataCollected.lista.add(
                                    InfoCollected(
                                        InfoCollected.TIPO_LINHA,
                                        line.codigoLinha.toString(),
                                        line.letreiroCompleto,
                                        descricao
                                    )
                                )
                            }
                        }
                    }
                    timeToUpdateRecycler()
                }
            })
    }

    fun getCorredoresInfo() {
        retrofit.getCorredores(cookie.value!!).enqueue(object : Callback<List<Corredor>> {
            override fun onFailure(call: Call<List<Corredor>>, t: Throwable) {
                Log.e("erro", t.message.toString())
            }

            override fun onResponse(
                call: Call<List<Corredor>>,
                response: Response<List<Corredor>>
            ) {
                val res = response.body()
                if (res != null) {
                    for (item in res) {
                        dataCollected.lista.add(
                            InfoCollected(
                                InfoCollected.TIPO_CORREDOR,
                                item.codigoCorredor.toString(),
                                item.nomeCorredor
                            )
                        )
                    }
                    timeToUpdateRecycler()
                }
            }
        })
    }

    fun getCorredoresStopPoint(codigoCorredor: String) {
        retrofit.getStopPointCorredor(cookie.value!!, codigoCorredor)
            .enqueue(object : Callback<List<ParadaBuscar>> {
                override fun onFailure(call: Call<List<ParadaBuscar>>, t: Throwable) {
                    Log.e("Erro", t.message.toString())
                }

                override fun onResponse(
                    call: Call<List<ParadaBuscar>>,
                    response: Response<List<ParadaBuscar>>
                ) {
                    val res = response.body()
                    if (res != null) {
                        dataCollected.lista.clear()
                        for (item in res) {
                            dataCollected.lista.add(
                                InfoCollected(
                                    InfoCollected.TIPO_PARADA_CORREDOR,
                                    item.codigoParada,
                                    item.nomeParada,
                                    item.enderecoParada,
                                    item.latitudeParada,
                                    item.longitudeParada
                                )
                            )
                        }
                        timeToNavigate()
                    }
                }

            })
    }


    fun verificarPosicaoVeiculosParada() {
        if (posicaoVeiculosLinha && posicaoParadasLinha) {
            posicaoVeiculosLinha = false
            posicaoParadasLinha = false
            timeToNavigate()
        }
    }

    fun getLinePositionAndLineStop(linha: String) {
        getVehiclePositionOfLine(linha)
        getStopPointOfLine(linha, true)
    }


    fun atualizarBusca() {
        if (busca.value == null) {
            _busca.value = true
        } else {
            _busca.value = !(_busca.value!!)
        }
    }


    fun timeToNavigate() {
        if (navigate.value == null) {
            _navigate.value = true
        } else {
            _navigate.value = !(_navigate.value!!)
        }
    }
    fun timeToUpdateRecycler() {
        if (updateRecycler.value == null) {
            _updateRecycler.value = true
        } else {
            _updateRecycler.value = !(_updateRecycler.value!!)
        }
    }

}