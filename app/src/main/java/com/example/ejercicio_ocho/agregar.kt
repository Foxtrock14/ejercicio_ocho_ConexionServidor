package com.example.ejercicio_ocho

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ejercicio13.Contacto
import com.example.ejercicio13.IContacto
import com.example.ejercicio13.RetrofitAPP
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Random

class agregar : AppCompatActivity() {
    lateinit var ednombre: EditText
    lateinit var edtelefono: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agregar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        ednombre = findViewById(R.id.ednombre)
        edtelefono = findViewById(R.id.edtelefono)

    }


    fun guardar(v: View){
        val nombre = ednombre.text.toString()
        val telefono = edtelefono.text.toString()

        Log.v("Datos", "$nombre $telefono")

        val retrofit = RetrofitAPP.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)

        val contacto = Contacto("0", nombre, telefono)
        val peticion: Call<Boolean> = servicio.agregar(contacto)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                val res = response.body()
                Log.v("Respuesta", "Valor ${res}")
                ednombre.setText("")
                edtelefono.setText("")
                ednombre.requestFocus()
                Toast.makeText(applicationContext, "Se grabo correctamente", Toast.LENGTH_SHORT).show()

            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", t.message.toString())
            }

        })

    }
}