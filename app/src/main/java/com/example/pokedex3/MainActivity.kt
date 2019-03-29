package com.example.pokedex3

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.pokedex3.models.Pokemon
import com.example.pokedex3.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject




import java.io.IOException






class MainActivity : AppCompatActivity() {
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var mPokemonNumber: EditText? = null
    var mSearchButton: Button? = null
    var mResultText: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindView()

        mSearchButton!!.setOnClickListener { view ->
            val pokemonNumber = mPokemonNumber!!.text.toString().toLowerCase().trim { it <= ' ' }
            if (pokemonNumber.isEmpty()) {
                mResultText!!.setText(R.string.text_nothing_to_show)
            } else {

                FetchPokemonTask().execute(pokemonNumber)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    fun initRecycler(lista: List<String>) {
        var pokemon: MutableList<Pokemon> = MutableList(lista.size) {i ->
            Pokemon(i,lista.get(i),"type:" +et_pokemon_number.text.toString())
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = PokemonAdapter(pokemon, object :ClickListener{
            override fun onClick(vista: View, position: Int) {
                val intent = Intent(this@MainActivity, MostrarPokemon::class.java)
                intent.putExtra("Nombre", lista.get(position))
                startActivity(intent);
            }

        })

        rv_pokemons.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    internal fun bindView() {
        mPokemonNumber = findViewById(R.id.et_pokemon_number)
        mSearchButton = findViewById(R.id.bt_search_pokemon)
        mResultText = findViewById(R.id.tv_result)

    }


    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg pokemonNumbers: String): String? {

            if (pokemonNumbers.size == 0) {
                return null
            }

            val ID = pokemonNumbers[0]

            val pokeAPI = NetworkUtils.buildUrl(ID, "type")
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
                var pokeArray = jsonPokemon.getJSONArray("pokemon");
                var ListaPokemons = mutableListOf<String>()
                for (it in 0 until pokeArray.length()) {
                    var pokemon = pokeArray.optJSONObject(it).getJSONObject("pokemon");
                    var nombrePokemon = pokemon.getString("name");

                   ListaPokemons.add(it,nombrePokemon)
                }
                initRecycler(ListaPokemons)



            } else {
                var ListaPokemons = mutableListOf<String>()
                initRecycler(ListaPokemons)
                mResultText!!.setText(R.string.text_nothing_to_show)
            }
        }
    }
}

