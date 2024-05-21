package com.example.ejercicio_ocho

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.ejercicio13.Contacto
import com.example.ejercicio13.IContacto
import com.example.ejercicio13.RetrofitAPP
import com.example.ejercicio_ocho.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Timer
import java.util.TimerTask


class MainActivity : AppCompatActivity() {

    lateinit var rcv: RecyclerView
    private lateinit var adapter: Adapter

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rcv = findViewById(R.id.rvContact)
        adapter = Adapter(this)
        rcv.adapter = adapter
        rcv.layoutManager = LinearLayoutManager(this)

        // Hacer la solicitud HTTP para obtener los datos de /contactos
        iniciarActualizacionPeriodica()



    }
    override fun onResume() {
        super.onResume()
        iniciarActualizacionPeriodica()

    }
    private fun obtenerContactos() {
        val retrofit = Retrofit.Builder()
            .baseUrl(IContacto.url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(IContacto::class.java)
        val call = service.getContactos()

        call.enqueue(object : Callback<List<Contacto>> {
            override fun onResponse(call: Call<List<Contacto>>, response: Response<List<Contacto>>) {
                if (response.isSuccessful) {
                    val contactos = response.body()
                    if (contactos != null) {
                        // Guardar la cantidad de elementos antes de agregar nuevos datos
                        val itemCountBefore = ProvicionalData.listContact.size

                        // Limpiar la lista anterior y agregar los nuevos contactos
                        ProvicionalData.listContact.clear()
                        ProvicionalData.listContact.addAll(contactos)

                        // Notificar al adaptador sobre el rango de datos actualizado
                        adapter.notifyItemRangeInserted(itemCountBefore, contactos.size)

                        // Asegurarse de que el RecyclerView esté visible y tenga un layout manager y un adaptador
                        if (rcv.layoutManager == null) {
                            rcv.layoutManager = LinearLayoutManager(this@MainActivity)
                        }
                        if (rcv.adapter == null) {
                            rcv.adapter = adapter
                        }
                    } else {
                        Log.e("MainActivity", "Lista de contactos nula")
                    }
                } else {
                    Log.e("MainActivity", "Error en la solicitud: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Contacto>>, t: Throwable) {
                Log.e("MainActivity", "Error al obtener los contactos: ${t.message}")
            }
        })
    }
    fun iniciarActualizacionPeriodica() {
        val timer = Timer()
        val task = object : TimerTask() {
            override fun run() {
                obtenerContactos()
            }
        }
        // Ejecutar la tarea cada 5 segundos (5000 milisegundos)
        timer.schedule(task, 0, 500)
    }
    fun btnAdd(v: View) {
       val intent = Intent(this, agregar::class.java)
        startActivity(intent)
    }



    fun clickItem(position: Int) {
        val intent = Intent(this, EditActivity::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

}

