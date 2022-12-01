package com.example.recicla

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdaptadorElementos(val ListaElementos:ArrayList<Elementos>): RecyclerView.Adapter<AdaptadorElementos.ViewHolder>() {

    override fun getItemCount(): Int {
        return ListaElementos.size;
    }
    class ViewHolder (itemView : View): RecyclerView.ViewHolder(itemView) {
        val fImagen1 = itemView.findViewById<ImageView>(R.id.tt_imagen1);
        val fUsername = itemView.findViewById<TextView>(R.id.tt_username)
        val fTotal = itemView.findViewById<TextView>(R.id.tt_total)
        val fImagen2 = itemView.findViewById<ImageView>(R.id.tt_imagen2);

        //set the onclick listener for the singlt list item
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder?.fImagen1?.setImageBitmap(ListaElementos[position].imagen1)
        holder?.fUsername?.text=ListaElementos[position].username
        holder?.fTotal?.text="Cantidad: "+ListaElementos[position].total.toString()
        holder?.fImagen2?.setImageBitmap(ListaElementos[position].imagen2)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.elementoslista, parent, false);
        return ViewHolder(v);
    }
}