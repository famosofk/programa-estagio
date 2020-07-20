package com.example.olhovivoestagio.maps

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.olhovivoestagio.remote.OlhoVivoService
import com.example.olhovivoestagio.remote.RetrofitInstance
import com.example.olhovivoestagio.remote.models.InfoCollected
import com.example.olhovivoestagio.remote.models.PosicaoVeiculoLinha
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsViewModel(application: Application) : AndroidViewModel(application) {


    var linha: Boolean = false
    var argumento: String = ""
    var cookie: String? = null
    private lateinit var retrofitInstance: OlhoVivoService
    var lista = mutableListOf<InfoCollected>()
    private val _updateMap = MutableLiveData<Boolean>(null)
    val updateMap: LiveData<Boolean>
        get() = _updateMap

    fun startCouting(linhaValue: Boolean, argumentoValue: String, viewModel: MapsViewModel) {
        linha = linhaValue; argumento = argumentoValue
        val customHandler: android.os.Handler = android.os.Handler()
        val updateTimerThread: Runnable = object : Runnable {
            override fun run() {
                val counter = MyCount(
                    60000,
                    1000,
                    viewModel
                )
                counter.start()

                customHandler.postDelayed(this, 60000)
            }
        }
        customHandler.postDelayed(updateTimerThread, 0)
    }

    private fun createRetrofit() {
        retrofitInstance = RetrofitInstance.createService(OlhoVivoService::class.java)
    }

    fun getAuthCookie(key: String) {
        createRetrofit()
        retrofitInstance.getAuthCookie(key).enqueue(object : Callback<Boolean> {
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Erro:", t.message.toString())
            }

            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                cookie = response.headers().get("Set-Cookie")

            }

        })
    }

    fun buscarLinha() {
        val listaAuxiliar = mutableListOf<InfoCollected>()
        for (item in lista) {
            if (!(item.tipo == InfoCollected.TIPO_VEICULO)) {
                listaAuxiliar.add(item)
            }
        }

        retrofitInstance.getPosicaoVeiculosLinha(cookie!!, argumento)
            .enqueue(object : Callback<PosicaoVeiculoLinha> {
                override fun onFailure(call: Call<PosicaoVeiculoLinha>, t: Throwable) {
                    Log.e("Erro:", t.message.toString())
                }

                override fun onResponse(
                    call: Call<PosicaoVeiculoLinha>,
                    response: Response<PosicaoVeiculoLinha>
                ) {
                    val res = response.body()
                    if (res != null) {

                        for (item in res.arrayVeiculos) {
                            listaAuxiliar.add(
                                InfoCollected(
                                    InfoCollected.TIPO_VEICULO,
                                    item.prefixoVeiculo.toString(),
                                    argumento,
                                    "Acessível: ${item.acessivelDeficientes}",
                                    item.latitude,
                                    item.longitude
                                )
                            )
                        }
                        lista = listaAuxiliar

                        _updateMap.value = true
                    }
                }

            })


    }

    fun doneUpdateMap() {
        _updateMap.value = false
    }

}

class MyCount(
    millisInFuture: Long,
    countDownInterval: Long,
    val viewModel: MapsViewModel
) : CountDownTimer(
    millisInFuture,
    countDownInterval
) {
    override fun onFinish() {
        viewModel.buscarLinha()
    }

    override fun onTick(p0: Long) {

    }

}

