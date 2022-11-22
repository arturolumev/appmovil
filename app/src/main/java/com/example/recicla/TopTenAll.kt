package com.example.recicla

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
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

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}