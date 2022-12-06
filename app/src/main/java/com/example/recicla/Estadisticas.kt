package com.example.recicla

import android.content.Intent
import android.os.AsyncTask
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
import org.json.JSONException

var porcen = 0
var porcentajePlastico: MutableList<Int> = mutableListOf()
var porcentajeVidrio: MutableList<Int> = mutableListOf()
var porcentajePapel: MutableList<Int> = mutableListOf()
var fechaLista: MutableList<String> = mutableListOf()

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
            startActivity(intent)
        })

        //cargarPlastico()
        //cargarVidrio()
        //cargarPapel()
        //principal()

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation);
        navigation?.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.action_calendar -> {
                    startActivity(Intent(this,CalendarEvents::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.action_top10->{
                    startActivity(Intent(this,listadoTopten::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.action_statics->{
                    startActivity(Intent(this,Estadisticas::class.java))
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
        AsyncTask.execute {
            val queue = Volley.newRequestQueue(applicationContext)
            val urlPlastico = getString(com.example.recicla.R.string.urlAPI) + "/api/rplastico/6"
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

                            agregarPlastico(total_count)
                            fechaLista.add(reg_date)
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
            val urlVidrio = getString(com.example.recicla.R.string.urlAPI) + "/api/rvidrio/6"
            val stringRequestVidrio = JsonArrayRequest(urlVidrio,
                { response ->
                    try {
                        var total_count=0
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            agregarVidrio(total_count)
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
            val urlPapel = getString(com.example.recicla.R.string.urlAPI) + "/api/rpapel/6"
            val stringRequestPapel = JsonArrayRequest(urlPapel,
                { response ->
                    try {
                        var total_count=0
                        for (i in 0 until response.length()) {
                            total_count =
                                response.getJSONObject(i).getInt("total_count")
                            agregarPapel(total_count)
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