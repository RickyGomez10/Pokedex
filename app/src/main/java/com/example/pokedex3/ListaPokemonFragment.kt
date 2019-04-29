package com.example.pokedex3

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.pokedex3.Interfaces.IComunicaFragments
import com.example.pokedex3.models.Pokemon
import com.example.pokedex3.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_lista_pokemon.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ListaPokemonFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ListaPokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ListaPokemonFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    //Interface
    var actividad = Activity()

    var interfaceComunica = object : IComunicaFragments {
          override fun enviarPokemon(nombre : String){}
    }


    //Lista pokemon
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var mPokemonNumber: EditText? = null
    var mSearchButton: Button? = null
    var mResultText: TextView? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val vista : View = inflater.inflate(R.layout.fragment_lista_pokemon, container, false)

        mPokemonNumber = vista?.findViewById(R.id.et_pokemon_number)
        mSearchButton = vista?.findViewById(R.id.bt_search_pokemon)
        mResultText = vista?.findViewById(R.id.tv_result) as TextView
        mPokemonNumber?.text?.clear()


        mSearchButton!!.setOnClickListener { view ->
            val pokemonNumber = mPokemonNumber!!.text.toString().toLowerCase().trim { it <= ' ' }
            if (pokemonNumber.isEmpty()) {
                mResultText!!.setText(R.string.text_nothing_to_show)
            } else {

                FetchPokemonTask().execute(pokemonNumber)
                mPokemonNumber?.text?.clear()
            }
        }



        return vista
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Activity){
            this.actividad = context
            interfaceComunica = this.actividad as IComunicaFragments

        }

        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListaPokemonFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListaPokemonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun initRecycler(lista: List<String>) {
        var pokemon: MutableList<Pokemon> = MutableList(lista.size) { i ->
            Pokemon(i,lista.get(i),"type:" +et_pokemon_number.text.toString())
        }

        viewManager = LinearLayoutManager(context)
        viewAdapter = PokemonAdapter(pokemon, object :ClickListener{
            override fun onClick(vista: View, position: Int) {
                interfaceComunica.enviarPokemon(lista.get(rv_pokemons.getChildAdapterPosition(vista)))
            }

        })

        rv_pokemons.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

    }

    internal fun bindView() {

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
