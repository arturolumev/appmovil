package com.example.recicla

import android.R
import android.content.Intent
import android.icu.number.NumberFormatter.with
import android.icu.number.NumberRangeFormatter.with
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso
import org.json.JSONException


class CalendarEvents : AppCompatActivity() {
    var calendar: CalendarView? = null
    var date_view: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.recicla.R.layout.activity_calendar_events)

        cargarProyectos()

        val navigation = findViewById<BottomNavigationView>(com.example.recicla.R.id.bottom_navigation);
        navigation?.setOnItemSelectedListener{
            when(it.itemId) {
                com.example.recicla.R.id.action_calendar -> {
                    startActivity(Intent(this,CalendarEvents::class.java))
                    return@setOnItemSelectedListener true
                }
                com.example.recicla.R.id.action_top10->{
                    startActivity(Intent(this,TopTenAll::class.java))
                    return@setOnItemSelectedListener true
                }
                com.example.recicla.R.id.action_statics->{
                    startActivity(Intent(this,Estadisticas::class.java))
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    fun cargarProyectos() {
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(applicationContext)
            val url = getString(com.example.recicla.R.string.urlAPI) + "/api/proyectos"
            val stringRequest = JsonArrayRequest(url,
                { response ->
                    try {
                        var id=""
                        var titulo=""
                        var descripcion=""
                        var lugar=""
                        var imagen=""
                        var fecha=""

                        for (i in 0 until response.length()) {
                            id =
                                response.getJSONObject(i).getString("id")
                            titulo =
                                response.getJSONObject(i).getString("titulo")
                            descripcion =
                                response.getJSONObject(i).getString("descripcion")
                            lugar =
                                response.getJSONObject(i).getString("lugar")
                            imagen =
                                response.getJSONObject(i).getString("imagen")
                            fecha =
                                response.getJSONObject(i).getString("fecha").substring(0,10)

                            almacenarDatos(id, titulo,descripcion,lugar,imagen,fecha)
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            applicationContext,
                            "Error al obtener los datos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, {
                    Toast.makeText(
                        applicationContext,
                        "Verifique que esta conectado a internet",
                        Toast.LENGTH_LONG
                    ).show()
                })
            queue.add(stringRequest)
        }
    }

    fun almacenarDatos(id:String, titulo:String, descripcion:String, lugar:String, imagen:String, fecha:String){
        var diccionario: Map<String, String> = mapOf(Pair("titulo2", titulo),
            Pair("descripcion2", descripcion),
            Pair("lugar2", lugar),
            Pair("imagen2", imagen),
            Pair("fecha2", fecha)
        )

        //queria almacenar las fechas en un array
        val fechas = arrayOfNulls<String>(4)
        fechas.plus(diccionario["fecha2"].toString())
        for(i in fechas.indices){
            Log.e("FECHASSSSS","${fechas[i]} está en la posición ${i+1}")
        }

        calendar = findViewById<View>(com.example.recicla.R.id.calendar) as CalendarView
        date_view = findViewById<View>(com.example.recicla.R.id.date_view) as TextView

        //por cada elemento en el diccionario imprime
        //y sí se imprimen tooodos los elementos de la bd
        //funciona bien
        Log.e("DICCIONARIOOOOO", diccionario.toString())
        for((clave, valor) in diccionario) {
            println("$clave tiene un precio $valor")

            calendar!!.setOnDateChangeListener(
                OnDateChangeListener { view, year, month, dayOfMonth ->
                    val Date = (year.toString() + "-" + (month + 1) + "-" + dayOfMonth.toString())
                    date_view!!.text = Date
                    //diccionario["fecha2"].toString()
                    if (Date.compareTo("$valor") == 0) {
                        Toast.makeText(
                            applicationContext,
                            diccionario["titulo2"].toString(),
                            Toast.LENGTH_SHORT
                        ).show();

                        val customLayout: View = layoutInflater.inflate(
                            com.example.recicla.R.layout.activity_evento_detalle,
                            null
                        )
                        val mensaje: AlertDialog = AlertDialog.Builder(this).create()
                        mensaje.setView(customLayout)

                        val imagen_editImage: ImageView =
                            customLayout.findViewById(com.example.recicla.R.id.imagen)
                        Picasso.get().load(diccionario["imagen2"].toString()).into(imagen_editImage)

                        val fecha_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.fecha)
                        fecha_editText.setText(diccionario["fecha2"].toString())

                        val titulo_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.titulo)
                        titulo_editText.setText(diccionario["titulo2"].toString())

                        val descripcion_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.descripcion)
                        descripcion_editText.setText(diccionario["descripcion2"].toString())

                        val lugar_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.lugar)
                        lugar_editText.setText(diccionario["lugar2"].toString())

                        mensaje.show()
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No hay eventos planeados para ese día",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            )
            println();
        }// Add Listener in calendar

    }
}
