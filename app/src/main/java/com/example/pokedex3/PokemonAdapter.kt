package com.example.pokedex3


import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pokedex3.models.Pokemon

import kotlinx.android.synthetic.main.item_pokemons.view.*

class PokemonAdapter(val items: List<Pokemon>, var clickListener: ClickListener) : RecyclerView.Adapter<PokemonAdapter.ViewHolder>() {

    // TODO: Para contar elementos creados

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemons, parent, false)

        /*
         * TODO: Muestra el valor de contador de view creadas solo se hace aqui, para asegurar
         * que solo se asigne el valor aqui
         */

        return ViewHolder(view, clickListener)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }


    class ViewHolder(itemView: View, listener: ClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var listener:ClickListener?= null;


       init {
           this.listener = listener;
           itemView.setOnClickListener(this)
       }

        fun bind(item: Pokemon) = with(itemView) {
            tv_pokemon_name.text = item.name



        }
        override fun onClick(v: View?) {
            this.listener?.onClick(v!!, adapterPosition)
        }
    }

}