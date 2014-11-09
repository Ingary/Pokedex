package com.example.rafael.pokedex;

/**
 * Created by rafael on 10/31/14.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class PokemonDetailFragment extends Fragment {

    private static final String ARG_NOMBRE = "pokemon";
    private Pokemon mPokemon;

    public static PokemonDetailFragment newInstance(Pokemon pokemon) {
        PokemonDetailFragment fragment = new PokemonDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOMBRE, pokemon);
        fragment.setArguments(args);
        return fragment;
    }
    public PokemonDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPokemon = getArguments().getParcelable(ARG_NOMBRE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pokemon_detail,
                container, false);

        TextView nameView = (TextView)rootView.findViewById(R.id.textview_pokemon_nombre);
        ImageView imageView = (ImageView)rootView.findViewById(R.id.detail_pokemon_imageview);

        nameView.setText("Name: " + mPokemon.getNombre());

        Picasso.with(getActivity())
                .load(mPokemon.getAvatar())
                .placeholder(R.drawable.pokeball)
                .error(R.drawable.pokeball)
                .into(imageView);

        return rootView;
    }

}