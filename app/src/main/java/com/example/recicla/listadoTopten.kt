package com.example.recicla

import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_listado_topten.*
import org.json.JSONException

class listadoTopten : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listado_topten)
        var llenarLista = ArrayList<Elementos>()
        llenarLista.add(Elementos(BitmapFactory.decodeResource(resources,R.drawable.primero),"...TOP 10...",100000,BitmapFactory.decodeResource(resources,R.drawable.primero)))
        val adapter = AdaptadorElementos(llenarLista)
        lista_tt.adapter = adapter
        cargarLista()
    }
    fun cargarLista() {
        lista_tt.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        lista_tt.layoutManager = LinearLayoutManager(this)
        var llenarLista = ArrayList<Elementos>()
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(applicationContext)
            val url = getString(R.string.urlAPI) + "/api/topten?format=json"
            val stringRequest = JsonArrayRequest(url,
                Response.Listener { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val username =
                                response.getJSONObject(i).getString("user_id__username")
                            val total =
                                response.getJSONObject(i).getString("total_count").toInt()
                            if (i==0){
                                val imagen1 =
                                    BitmapFactory.decodeResource(resources,R.drawable.primero)
                                val imagen2 =
                                    BitmapFactory.decodeResource(resources,R.drawable.primero)
                                llenarLista.add(Elementos(imagen1,username,total,imagen2))
                            }else if(i==1){
                                val imagen1 =
                                    BitmapFactory.decodeResource(resources,R.drawable.segundo)
                                val imagen2 =
                                    BitmapFactory.decodeResource(resources,R.drawable.segundo)
                                llenarLista.add(Elementos(imagen1,username,total,imagen2))
                            }else if (i==2){
                                val imagen1 =
                                    BitmapFactory.decodeResource(resources,R.drawable.tercero)
                                val imagen2 =
                                    BitmapFactory.decodeResource(resources,R.drawable.tercero)
                                llenarLista.add(Elementos(imagen1,username,total,imagen2))
                            }
                            else{
                                val imagen1 =
                                    BitmapFactory.decodeResource(resources,R.drawable.medalla)
                                val imagen2 =
                                    BitmapFactory.decodeResource(resources,R.drawable.medalla)
                                llenarLista.add(Elementos(imagen1,username,total,imagen2))
                            }

                        }
                        val adapter = AdaptadorElementos(llenarLista)
                        lista_tt.adapter = adapter
                    } catch (e: JSONException) {
                        Toast.makeText(
                            applicationContext,
                            "Error al obtener los datos",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }, Response.ErrorListener {
                    Toast.makeText(
                        applicationContext,
                        "Verifique que esta conectado a internet",
                        Toast.LENGTH_LONG
                    ).show()
                })
            queue.add(stringRequest)
        }
    }
}