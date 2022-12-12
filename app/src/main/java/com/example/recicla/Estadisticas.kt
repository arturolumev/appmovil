package com.example.recicla

import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.data.Set
import com.anychart.enums.Anchor
import com.anychart.enums.MarkerType
import com.anychart.enums.TooltipPositionMode
import com.anychart.graphics.vector.Stroke
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_estadisticas.*
import kotlinx.android.synthetic.main.activity_evento_detalle.*
import org.json.JSONException
import java.time.Instant
import java.time.ZonedDateTime

var porcen = 0
var porcentajePlastico: MutableList<Int> = mutableListOf()
var porcentajeVidrio: MutableList<Int> = mutableListOf()
var porcentajePapel: MutableList<Int> = mutableListOf()
var fechaLista: MutableList<String> = mutableListOf()

var user_id = ""
var date_joined =""

//probando
var fechasPlastico: MutableList<String> = mutableListOf()
var fechasPapel: MutableList<String> = mutableListOf()
var fechasVidrio: MutableList<String> = mutableListOf()

class Estadisticas : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        //quite el agregado todo en uno
        cargar()
        Handler().postDelayed({
            principal()
        }, 500)
        actualizar.setOnClickListener({
            val intent = Intent(this, Estadisticas::class.java)
            intent.putExtra("user_id",user_id)
            intent.putExtra("date_joined",date_joined)
            startActivity(intent)
            //startActivity(intent)
        })

        //cargarPlastico()
        //cargarVidrio()
        //cargarPapel()
        //principal()

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation);
        navigation?.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.action_calendar -> {
                    var intent = Intent(this,CalendarEvents::class.java)
                    intent.putExtra("user_id",user_id)
                    intent.putExtra("date_joined",date_joined)
                    startActivity(intent)
                    //startActivity(Intent(this,CalendarEvents::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.action_top10->{
                    startActivity(Intent(this,listadoTopten::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.action_statics->{
                    //startActivity(Intent(this,Estadisticas::class.java))
                    var intent = Intent(this,Estadisticas::class.java)
                    intent.putExtra("user_id",user_id)
                    intent.putExtra("date_joined",date_joined)
                    startActivity(intent)
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }

    class CustomDataEntry internal constructor(
        x: String?,
        value: Number?,
        value2: Number?,
        value3: Number?
    ) :
        ValueDataEntry(x, value) {
        init {
            setValue("value2", value2)
            setValue("value3", value3)
        }
    }

    fun cargar() {

        var parametros:Bundle ?=  intent.extras
        if (parametros!= null){
            user_id = parametros.getString("user_id").toString()
            date_joined = parametros.getString("date_joined").toString()
        }

        AsyncTask.execute {
            val queue = Volley.newRequestQueue(applicationContext)

            setDatesData(date_joined)

            val urlPlastico = getString(com.example.recicla.R.string.urlAPI) + "/api/rplastico/" + user_id
            val stringRequestPlastico = JsonArrayRequest(urlPlastico,
                { response ->
                    try {
                        var total_count=0
                        var reg_date=""
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            fechasPlastico.add(reg_date)
                        }

                        Log.e("FECHAS PLASTICO", fechasPlastico.toString())
                        var a = 0
                        var parawhile = 0
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            if (fechasPlastico[i] == fechaLista[a]) {
                                agregarPlastico(total_count)
                                a++
                                parawhile++
                            } else {
                                while (fechasPlastico[i] != fechaLista[parawhile]) {
                                    agregarPlastico(0)
                                    parawhile++
                                }
                                Log.e("PARAWHILE PLASTICO", parawhile.toString())
                                agregarPlastico(total_count)
                                parawhile++
                            }
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
            queue.add(stringRequestPlastico)

            //
            val urlVidrio = getString(com.example.recicla.R.string.urlAPI) + "/api/rvidrio/" + user_id
            val stringRequestVidrio = JsonArrayRequest(urlVidrio,
                { response ->
                    try {
                        var total_count=0
                        var reg_date=""
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            fechasVidrio.add(reg_date)
                        }

                        Log.e("FECHAS VIDRIO", fechasVidrio.toString())
                        var a = 0
                        var parawhile = 0
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            if (fechasVidrio[i] == fechaLista[a]) {
                                agregarVidrio(total_count)
                                a++
                                parawhile++
                            } else {
                                while (fechasVidrio[i] != fechaLista[parawhile]) {
                                    agregarVidrio(0)
                                    parawhile++
                                }
                                Log.e("PARAWHILE VIDRIO", parawhile.toString())
                                agregarVidrio(total_count)
                                parawhile++
                            }
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
            queue.add(stringRequestVidrio)

            //
            val urlPapel = getString(com.example.recicla.R.string.urlAPI) + "/api/rpapel/" + user_id
            val stringRequestPapel = JsonArrayRequest(urlPapel,
                { response ->
                    try {
                        var total_count=0
                        var reg_date=""
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            fechasPapel.add(reg_date)
                        }

                        Log.e("FECHAS VIDRIO", fechasVidrio.toString())
                        var a = 0
                        var parawhile = 0
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            reg_date =
                                response.getJSONObject(i).getString("reg_date").substring(0,10)
                            if (fechasPapel[i] == fechaLista[a]) {
                                agregarPapel(total_count)
                                a++
                                parawhile++
                            } else {
                                while (fechasPapel[i] != fechaLista[parawhile]) {
                                    agregarPapel(0)
                                    parawhile++
                                }
                                Log.e("PARAWHILE PAPEL", parawhile.toString())
                                agregarPapel(total_count)
                                parawhile++
                            }
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
            queue.add(stringRequestPapel)
        }

    }

    fun agregarFecha(fecha: String) {

        var fechas = fecha
        fechaLista.add(fechas)
        //principal()
    }

    fun setDatesData(a:String){
        var timestamp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.parse(a)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        var hoy = ZonedDateTime.now().toString().substring(0,10)+"T00:00:00Z"
        //Log.d("fechaaaaaaaaaaaaaaaaaaa",hoy.toString())
        var today = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Instant.parse(hoy)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        while(timestamp<=today){
            fechaLista.add(timestamp.toString().substring(0,10))
            timestamp = timestamp.plusSeconds(86400)
        }
        //Log.d("soy la lista final", fechaLista.toString())
    }

    fun agregarPlastico(porcentaje: Int) {

        var plastico = porcentaje
        porcentajePlastico.add(plastico)
        //principal()
    }

    fun agregarVidrio(porcentaje:Int) {

        //var date = fecha
        var vidrio = porcentaje

        //fechaLista.add(date)
        porcentajeVidrio.add(vidrio)
    }

    fun agregarPapel(porcentaje:Int) {

        //var date = fecha
        var papel = porcentaje

        //fechaLista.add(date)
        porcentajePapel.add(papel)
    }

    fun principal(){

        Log.e("Fechas", fechaLista.toString())
        Log.e("Plasticos", porcentajePlastico.toString())
        Log.e("Vidrios", porcentajeVidrio.toString())
        Log.e("Papeles", porcentajePapel.toString())

        //var date = fecha
        //var porcen = porcentaje

        //return porcentajePlastico.add(porcen)
        //fechaLista.add(date)

        //Log.e("Porcentaje Plastico", porcentajePlastico.toString())
        //Log.e("Fecha Plastico", fechaLista.toString())

        val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
        //anyChartView.setProgressBar(findViewById(R.id.progress_bar))
        val cartesian = AnyChart.line()
        cartesian.animation(true)
        cartesian.padding(10.0, 20.0, 5.0, 20.0)
        cartesian.crosshair().enabled(true)
        cartesian.crosshair()
            .yLabel(true)
            .yStroke(null as Stroke?, null, null, null as String?, null as String?)
        cartesian.tooltip().positionMode(TooltipPositionMode.POINT)

        val seriesData: MutableList<DataEntry> = ArrayList()

        //Log.e("Plasticossss", porcentajePlastico.toString())
        //Log.e("Fechassss", fechaLista.toString())

        /*
        for (i in fechaLista.indices) {
            seriesData.add(CustomDataEntry(fechaLista[i].toString(), porcentajePlastico[i].toString().toDouble(), porcentajeVidrio[i].toString().toDouble(), porcentajePapel[i].toString().toDouble()))
        }
        */

        for (i in fechaLista.indices) {

            var sizeFecha = fechaLista.size
            var sizePlastico = porcentajePlastico.size
            var sizePapel = porcentajePapel.size
            var sizeVidrio = porcentajeVidrio.size

            if (sizePlastico != sizeFecha) {
                porcentajePlastico.add(0)
            }
            if (sizePapel != sizeFecha) {
                porcentajePapel.add(0)
            }
            if (sizeVidrio != sizeFecha) {
                porcentajeVidrio.add(0)
            }



            Log.e("NUEVO PLASTICO", "PLASTICO -> " + porcentajePlastico.toString())
            Log.e("NUEVO VIDRIO", "VIDRIO -> " + porcentajeVidrio.toString())
            Log.e("NUEVO PAPEL", "PAPEL -> " + porcentajePapel.toString())

        }

        for (i in fechaLista.indices) {
            seriesData.add(CustomDataEntry(fechaLista[i].toString(), porcentajePlastico[i].toString().toDouble(), porcentajeVidrio[i].toString().toDouble(), porcentajePapel[i].toString().toDouble()))
        }

        val set = Set.instantiate()
        set.data(seriesData)
        val series1Mapping = set.mapAs("{ x: 'x', value: 'value' }")
        val series2Mapping = set.mapAs("{ x: 'x', value: 'value2' }")
        val series3Mapping = set.mapAs("{ x: 'x', value: 'value3' }")
        val series1 = cartesian.line(series1Mapping)
        series1.name("Plástico")
        series1.hovered().markers().enabled(true)
        series1.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(5.0)

        series1.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
        val series2 = cartesian.line(series2Mapping)
        series2.name("Vidrio")
        series2.hovered().markers().enabled(true)
        series2.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series2.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
        val series3 = cartesian.line(series3Mapping)
        series3.name("Papel")
        series3.hovered().markers().enabled(true)
        series3.hovered().markers()
            .type(MarkerType.CIRCLE)
            .size(4.0)
        series3.tooltip()
            .position("right")
            .anchor(Anchor.LEFT_CENTER)
            .offsetX(5.0)
            .offsetY(5.0)
        cartesian.legend().enabled(true)
        cartesian.legend().fontSize(13.0)
        cartesian.legend().padding(0.0, 0.0, 10.0, 0.0)
        anyChartView.setChart(cartesian)

        Log.e("ver", seriesData.toString())

    }

}