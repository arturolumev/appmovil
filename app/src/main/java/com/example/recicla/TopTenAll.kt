package com.example.recicla

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_top_ten_all.*

class TopTenAll : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_ten_all)

        lista.addItemDecoration(DividerItemDecoration( this , DividerItemDecoration.VERTICAL))

        lista.layoutManager = LinearLayoutManager(this);

        var llenarLista = ArrayList<TopTen>()
        for(i in 1 until 10){
            llenarLista.add(TopTen( "Elemento" + i,
                BitmapFactory.decodeResource(resources , R.drawable.imaconfiguracion)))
        }

        val adapter = TopTen.AdaptadorElementos(llenarLista)
        lista.adapter = adapter

        val navigation = findViewById<BottomNavigationView>(R.id.bottom_navigation);
        navigation?.setOnItemSelectedListener{
            when(it.itemId) {
                R.id.action_calendar -> {
                    startActivity(Intent(this,CalendarEvents::class.java))
                    return@setOnItemSelectedListener true
                }
                R.id.action_top10->{
                    startActivity(Intent(this,TopTenAll::class.java))
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}