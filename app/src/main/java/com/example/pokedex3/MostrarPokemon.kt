package com.example.pokedex3

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.content.Intent
import android.os.AsyncTask
import android.util.Log
import com.example.pokedex3.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_mostrar_pokemon.*
import org.json.JSONObject
import java.io.IOException
import android.os.AsyncTask.execute




class MostrarPokemon : AppCompatActivity() {
    var tvMostrarPokemons: TextView? = null
    var tvAlturaPokemon: TextView? = null
    var tvPesoPokemon: TextView? = null
    var tvNumeroPokemon: TextView? = null
    var tvXPbase: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mostrar_pokemon)


        bindView()
        val intent = intent
        val NombreP = intent.getStringExtra("Nombre")
        tvMostrarPokemons!!.setText(NombreP);

        FetchPokemonTask().execute()



    }

    internal fun bindView() {


        tvMostrarPokemons = findViewById(R.id.tv_NombrePokemon);
        tvAlturaPokemon = findViewById(R.id.tv_AlturaPokemon)
        tvPesoPokemon = findViewById(R.id.tv_PesoPokemon);
        tvNumeroPokemon = findViewById(R.id.tv_numeroPokemon)
        tvXPbase = findViewById(R.id.tv_XP)

    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {




            val intent = intent
            val NombreP = intent.getStringExtra("Nombre")
            val pokeAPI = NetworkUtils.buildUrl(NombreP, "pokemon")
            Log.i("asd",pokeAPI.toString())
            try {
                return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
            } catch (e: IOException) {
                e.printStackTrace()
                return ""
            }

        }

        override fun onPostExecute(pokemonInfo: String?) {
            if (pokemonInfo != null && pokemonInfo != "") {
                var jsonPokemon = JSONObject(pokemonInfo)
                var PokemonHeight = jsonPokemon.getString("height");
                var PokemonWeight = jsonPokemon.getString("weight");
                var PokemonNumber = jsonPokemon.getString("id");
                var PokemonBaseXP = jsonPokemon.getString("base_experience")

                tvAlturaPokemon!!.setText(PokemonHeight)
                tvPesoPokemon!!.setText(PokemonWeight)
                tvNumeroPokemon!!.setText(PokemonNumber)
                tvXPbase!!.setText(PokemonBaseXP)






            } else {

            }
        }
    }
}
