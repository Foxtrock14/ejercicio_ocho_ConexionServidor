package com.example.ejercicio_ocho



import android.content.Intent
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

class EditActivity : AppCompatActivity() {
    lateinit var ednombre: EditText
    lateinit var edtelefono: EditText
    lateinit var edid: EditText

    var position: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        ednombre = findViewById(R.id.ednombre)
        edtelefono = findViewById(R.id.edtelefono)
        edid = findViewById(R.id.edId)

        val contact = ProvicionalData.listContact[position]
        ednombre.setText(contact.nombre)
        edtelefono.setText(contact.telefono)
        edid.setText(contact.id)

    }

    fun guardar(v: View) {
        val id = edid.text.toString()
        val nombre = ednombre.text.toString()
        val telefono = edtelefono.text.toString()

        val retrofit = RetrofitAPP.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)

        val contacto = Contacto(id, nombre, telefono)
        val peticion: Call<Boolean> = servicio.modificar(id, contacto)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res == true) {
                        Toast.makeText(applicationContext, "Se modificó correctamente", Toast.LENGTH_SHORT).show()
                        ednombre.setText("")
                        edtelefono.setText("")
                        ednombre.requestFocus()
                        val intent = Intent(this@EditActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "No se pudo modificar el contacto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", "Error en la respuesta del servidor")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", "Fallo en la solicitud: ${t.message}")
            }
        })
    }
    fun eliminar(v: View) {
        val id = edid.text.toString().toIntOrNull() ?: return

        val retrofit = RetrofitAPP.getRetrofit()
        val servicio = retrofit.create(IContacto::class.java)

        val peticion: Call<Boolean> = servicio.eliminar(id)

        peticion.enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful) {
                    val res = response.body()
                    if (res == true) {
                        Toast.makeText(applicationContext, "Se eliminó correctamente", Toast.LENGTH_SHORT).show()
                        ednombre.setText("")
                        edtelefono.setText("")
                        ednombre.requestFocus()
                        val intent = Intent(this@EditActivity, MainActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(applicationContext, "No se pudo eliminar el contacto", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", "Error en la respuesta del servidor")
                }
            }

            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("Error", "Fallo en la solicitud: ${t.message}")
            }
        })
    }
}