package com.example.pokedex3

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.example.pokedex3.Interfaces.IComunicaFragments
import com.example.pokedex3.models.Pokemon
import com.example.pokedex3.utils.NetworkUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject




import java.io.IOException






class MainActivity : AppCompatActivity(), ListaPokemonFragment.OnFragmentInteractionListener, DetallePokemonFragment.OnFragmentInteractionListener,
    IComunicaFragments {

    var listaFragment = ListaPokemonFragment()
    var detalleFragment = DetallePokemonFragment()

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    var mPokemonNumber: EditText? = null
    var mSearchButton: Button? = null
    var mResultText: TextView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(findViewById<LinearLayout>(R.id.ContenedorFragments) != null){
            if(savedInstanceState != null){


            }
            supportFragmentManager.beginTransaction().replace(R.id.ContenedorFragments, listaFragment).commit()

        }else{
            supportFragmentManager.beginTransaction().add(R.id.fl_lista, listaFragment).commit();

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    override fun enviarPokemon(nombre : String){
        val bundlenvio = Bundle()
        bundlenvio.putSerializable("objeto",nombre);
        detalleFragment.arguments = bundlenvio
        val fragmento : Fragment? = supportFragmentManager.findFragmentById(R.id.fl_detalle);
        if(findViewById<LinearLayout>(R.id.ContenedorFragments) == null){

            if(fragmento is DetallePokemonFragment) {
                val fragmento2: Fragment? = supportFragmentManager.findFragmentByTag("fragdetalle")
                supportFragmentManager.beginTransaction().remove(fragmento2!!).commit()

            }
                supportFragmentManager.beginTransaction().add(R.id.fl_detalle, detalleFragment, "fragdetalle").addToBackStack(null).commitAllowingStateLoss();

            Log.i("Aja:","Aja")

        }else{
            val bundlenvio = Bundle()
            bundlenvio.putSerializable("objeto",nombre);
            detalleFragment.arguments = bundlenvio

            supportFragmentManager.beginTransaction().replace(R.id.ContenedorFragments, detalleFragment).addToBackStack(null).commit()


        }





    }

}

