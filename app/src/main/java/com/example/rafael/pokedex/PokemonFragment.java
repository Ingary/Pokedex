package com.example.rafael.pokedex;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by rafael on 10/19/14.
 */
public class PokemonFragment extends Fragment {

    private final String LOG_TAG = PokemonTask.class.getSimpleName();
    private PokemonAdapter mPokemonAdapter;
    private ProgressBar progressBar;
    private ListView listView;
    private Callbacks mCallbacks = sDummyCallbacks;

    private static final String STATE_ACTIVATED_POSITION = "activated_position";

    public interface Callbacks {
        /**
         * Callback for when an item has been selected.
         */
        public void onItemSelected(Pokemon pokemon);
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override
        public void onItemSelected(Pokemon pokemon) {

        }
    };

    public PokemonFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null
                && savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
            setActivatedPosition(savedInstanceState
                    .getInt(STATE_ACTIVATED_POSITION));
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalStateException(
                    "Activity must implement fragment's callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        // Get ListView object from xml
        listView = (ListView) rootView.findViewById(R.id.listview_pokemon);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar01);


        if (mPokemonAdapter == null ) {
            mPokemonAdapter = new PokemonAdapter(new ArrayList<Pokemon>(), getActivity());

            //new PokemonTask(mPokemonAdapter, listView, progressBar).execute();
            runTask();
        }

        // Assign adapter to ListView
        listView.setAdapter(mPokemonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Pokemon pokemon = (Pokemon)listView.getAdapter().getItem(i);
                mCallbacks.onItemSelected(pokemon);
            }
        });

        return rootView;
    }

    public void setActivateOnItemClick(boolean activateOnItemClick) {
        listView.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }

    public void setActivatedPosition(int position) {
        if (position == ListView.INVALID_POSITION) {
            listView.setItemChecked(0, false);
        } else {
            listView.setItemChecked(position, true);
        }
    }

    public void runTask(){
        /*PokemonListApiTask apiTask = new PokemonListApiTask(pokemonAdapter,
                listView,progressBarLoading);
        apiTask.execute();*/
        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        GsonRequest<Pokemon[]> getPersons =
                new GsonRequest<Pokemon[]>("http://mi-pokedex.herokuapp.com/api/v1/pokemons", Pokemon[].class,

                        new Response.Listener<Pokemon[]>() {
                            @Override
                            public void onResponse(Pokemon[] response) {
                                List<Pokemon> pokemons = Arrays.asList(response);
                                if(pokemons!=null){
                                    mPokemonAdapter.clear();
                                    mPokemonAdapter.addAll(pokemons);
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                listView.setVisibility(View.VISIBLE);
                            }

                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        listView.setVisibility(View.VISIBLE);
                    }
                });

        PokedexApplication.getInstance().addToRequestQueue(getPersons);
    }
}
