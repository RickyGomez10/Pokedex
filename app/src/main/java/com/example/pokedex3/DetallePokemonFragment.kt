package com.example.pokedex3

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.pokedex3.models.Pokemon
import com.example.pokedex3.utils.NetworkUtils
import org.json.JSONObject
import java.io.IOException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DetallePokemonFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DetallePokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class DetallePokemonFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    //Detalle Pokemon
    var textoNombre: TextView? = null;
    var tvAlturaPokemon: TextView? = null
    var tvPesoPokemon: TextView? = null
    var tvNumeroPokemon: TextView? = null
    var tvXPbase: TextView? = null




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
        val vista : View =  inflater.inflate(R.layout.fragment_detalle_pokemon, container, false)

        //Textviews

        val textoNombre : TextView?= vista.findViewById(R.id.tv_NombrePokemon)

         tvAlturaPokemon = vista.findViewById(R.id.tv_AlturaPokemon)
         tvPesoPokemon = vista.findViewById(R.id.tv_PesoPokemon)
         tvNumeroPokemon = vista.findViewById(R.id.tv_numeroPokemon)
        tvXPbase = vista.findViewById(R.id.tv_XP)

        FetchPokemonTask().execute()

        val objetoPokemon : Bundle ?= arguments;
        var pokemon : String ?= null;
        if(objetoPokemon != null){

            pokemon = objetoPokemon.getSerializable("objeto") as String;
            textoNombre?.text = pokemon



            Log.i("Que se mando?", pokemon)


        }else{


            Log.i("Que se mando?:","Null")
        }


        return vista
    }





    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
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
         * @return A new instance of fragment DetallePokemonFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetallePokemonFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


//Detalle pokemon

    private inner class FetchPokemonTask : AsyncTask<String, Void, String>() {

        override fun doInBackground(vararg params: String?): String? {
            val objetoPokemon : Bundle ?= arguments;
            var pokemon : String ?= null;

    if(objetoPokemon != null) {
        pokemon = objetoPokemon.getSerializable("objeto") as String;
        val pokeAPI = NetworkUtils.buildUrl(pokemon, "pokemon")
        Log.i("asd", pokeAPI.toString())

        try {
            return NetworkUtils.getResponseFromHttpUrl(pokeAPI!!)
        } catch (e: IOException) {
            e.printStackTrace()
            Log.i("No estoy entrando:","nel<")
            return ""
        }
    } else {
        Log.i("No hay objeto pokemon", "Nel")
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
