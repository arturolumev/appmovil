package com.example.recicla

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class TopTen(val texto:String , val imagen:Bitmap) {
    class AdaptadorElementos(val ListaElementos:ArrayList<TopTen>): RecyclerView.Adapter<AdaptadorElementos.ViewHolder>(){

        class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
            val fTexto = itemView.findViewById<TextView>(R.id.elemento_texto)
            val fImagen = itemView.findViewById<ImageView>(R.id.elemento_imagen);
        }


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AdaptadorElementos.ViewHolder {
            val v = LayoutInflater.from(parent?.context).inflate(R.layout.activity_top_ten, parent , false);
            return ViewHolder(v);
        }

        override fun onBindViewHolder(holder: AdaptadorElementos.ViewHolder, position: Int) {
            holder?.fTexto?.text = ListaElementos[position].texto
            holder?.fImagen?.setImageBitmap(ListaElementos[position].imagen)
        }

        override fun getItemCount(): Int {
            return ListaElementos.size;
        }

    }
}