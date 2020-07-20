package com.example.olhovivoestagio

import com.example.olhovivoestagio.remote.OlhoVivoService
import com.example.olhovivoestagio.remote.RetrofitInstance
import junit.framework.Assert.assertEquals
import org.junit.Test

class RetrofitTests {

    val api_key: String = "2bc28344ead24b3b5fe273ec7389d2e4d14abd2bad45abe6d8d774026193f894"
    val remote = RetrofitInstance.createService(OlhoVivoService::class.java)

    @Test
    fun testingAuth() {
        val response = remote.getAuthCookie(api_key).execute()

        assertEquals(200, response.code())
    }

    @Test
    fun testingGetLine() {
        val responseAuth = remote.getAuthCookie(api_key).execute()
        val cookie = responseAuth.headers().get("Set-cookie").toString()

        val referenciaLinha: String = "8000" //podemos mudar a linha aqui pra ver a comunicação.
        val response = remote.getLinha(cookie, referenciaLinha).execute()

        assertEquals(200, response.code())
    }

    @Test
    fun testingGetCorredores() {
        val responseAuth = remote.getAuthCookie(api_key).execute()
        val cookie = responseAuth.headers().get("Set-cookie").toString()

        val response = remote.getCorredores(cookie).execute()

        assertEquals(200, response.code())
    }

    @Test
    fun testingGetParada() {
        val responseAuth = remote.getAuthCookie(api_key).execute()
        val cookie = responseAuth.headers().get("Set-cookie").toString()
        val codigoParada = ""

        val response = remote.getParada(cookie, codigoParada).execute()

        assertEquals(200, response.code())
    }

    @Test
    fun testingGetParadaLinhas() {
        val responseAuth = remote.getAuthCookie(api_key).execute()
        val cookie = responseAuth.headers().get("Set-cookie").toString()
        val linha = "1273"

        val response = remote.getParadasLinhas(cookie, linha).execute()

        assertEquals(200, response.code())
    }

    @Test
    fun testingGetParadaPrevisao() {
        val responseAuth = remote.getAuthCookie(api_key).execute()
        val cookie = responseAuth.headers().get("Set-cookie").toString()
        val codigoParada = "7014417"

        val response = remote.getPrevisaoChegadaParada(cookie, codigoParada).execute()

        assertEquals(200, response.code())
    }


}