package com.example.ejercicio13

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IContacto {
    companion object{
        const val url: String = "http://192.168.1.12:4567"
    }

    @GET("/contactos")
    fun getContactos():Call<List<Contacto>>

    @PUT("/contacto")
    fun agregar(@Body contacto: Contacto):Call<Boolean>

    @POST("/contacto/{id}")
    fun modificar(@Path("id") id: String, @Body contacto: Contacto): Call<Boolean>

    @DELETE("/contacto/{id}")
    fun eliminar(@Path("id") id: Int): Call<Boolean>
}