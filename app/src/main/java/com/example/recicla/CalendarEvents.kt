package com.example.recicla

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CalendarEvents : AppCompatActivity() {
    var calendar: CalendarView? = null
    var date_view: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.recicla.R.layout.activity_calendar_events)

        calendar = findViewById<View>(com.example.recicla.R.id.calendar) as CalendarView
        date_view = findViewById<View>(com.example.recicla.R.id.date_view) as TextView
        // Add Listener in calendar
        calendar!!.setOnDateChangeListener(
            OnDateChangeListener { view, year, month, dayOfMonth ->
                // In this Listener have one method
                // and in this method we will
                // get the value of DAYS, MONTH, YEARS
                // Store the value of date with
                // format in String type Variable
                // Add 1 in month because month
                // index is start with 0
                val Date = (dayOfMonth.toString() + "-"
                        + (month + 1) + "-" + year)
                date_view!!.text = Date
                if (Date.compareTo("13-11-2022") == 0) {
                    Toast.makeText(applicationContext, "Teachers' Professional Day", Toast.LENGTH_SHORT).show();
                    val mensaje:AlertDialog= AlertDialog.Builder(this).create()
                    mensaje.setView(
                        LayoutInflater.from(this).inflate(
                            R.layout.activity_evento_detalle, null));
                    mensaje.show()
                }else {
                    Toast.makeText(applicationContext, "No hay eventos planeados para ese día", Toast.LENGTH_SHORT).show();
                }
            }
        )
    }
}
