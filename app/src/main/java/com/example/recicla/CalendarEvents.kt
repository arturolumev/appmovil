package com.example.recicla

import android.content.Intent
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
                        for (i in 0 until response.length()) {
                            var id =
                                response.getJSONObject(i).getString("id")
                            var titulo =
                                response.getJSONObject(i).getString("titulo")
                            var descripcion =
                                response.getJSONObject(i).getString("descripcion")
                            var lugar =
                                response.getJSONObject(i).getString("lugar")
                            var imagen =
                                response.getJSONObject(i).getString("imagen")
                            var fecha =
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

    val titulos: MutableList<String> = mutableListOf()
    val descripciones: MutableList<String> = mutableListOf()
    val lugares: MutableList<String> = mutableListOf()
    val imagenes: MutableList<String> = mutableListOf()
    val fechas: MutableList<String> = mutableListOf()

    fun almacenarDatos(id:String, titulo:String, descripcion:String, lugar:String, imagen:String, fecha:String): String {
        var diccionario: Map<String, String> = mapOf(Pair("titulo2", titulo),
            Pair("descripcion2", descripcion),
            Pair("lugar2", lugar),
            Pair("imagen2", imagen),
            Pair("fecha2", fecha)
        )

        titulos.add(diccionario["titulo2"].toString())
        descripciones.add(diccionario["descripcion2"].toString())
        lugares.add(diccionario["lugar2"].toString())
        imagenes.add(diccionario["imagen2"].toString())
        fechas.add(diccionario["fecha2"].toString())
        //println("List: $fechas")

        calendar = findViewById<View>(com.example.recicla.R.id.calendar) as CalendarView
        date_view = findViewById<View>(com.example.recicla.R.id.date_view) as TextView

        for ((valor) in fechas.withIndex()) {
            //println("$valor")
            calendar!!.setOnDateChangeListener(
                OnDateChangeListener { view, year, month, dayOfMonth ->
                    val Date = (year.toString() + "-" + (month + 1) + "-" + dayOfMonth.toString())
                    date_view!!.text = Date
                    if (fechas.indexOf(Date)> -1) {

                        var indice=fechas.indexOf(Date).toString().toInt()

                        Toast.makeText(
                            applicationContext,
                            titulos[indice],
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
                        Picasso.get().load(imagenes[indice]).into(imagen_editImage)

                        val fecha_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.fecha)
                        fecha_editText.setText(fechas[indice])

                        val titulo_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.titulo)
                        titulo_editText.setText(titulos[indice])

                        val descripcion_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.descripcion)
                        descripcion_editText.setText(descripciones[indice])

                        val lugar_editText: TextView =
                            customLayout.findViewById(com.example.recicla.R.id.lugar)
                        lugar_editText.setText(lugares[indice])

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
        }
        return diccionario.toString();
    }
}
