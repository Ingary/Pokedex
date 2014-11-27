package com.example.rafael.pokedex;

/**
 * Created by rafael on 10/31/14.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.squareup.picasso.Picasso;

public class PokemonDetailFragment extends Fragment {

    private static final String ARG_NOMBRE = "pokemon";
    private Pokemon mPokemon;
    private ShareActionProvider mShareActionProvider;
    private Bitmap mBitmap;
    private String urlImage;

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
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_pokemon_detail,
                container, false);

        TextView nameView = (TextView)rootView.findViewById(R.id.textview_pokemon_nombre);
        final ImageView imageView = (ImageView)rootView.findViewById(R.id.detail_pokemon_imageview);

        if (mPokemon != null) {
            nameView.setText("Name: " + mPokemon.getNombre());

            if (!mPokemon.getAvatar().isEmpty()){
                /*Picasso.with(getActivity())
                        .load(mPokemon.getAvatar())
                        .placeholder(R.drawable.pokeball)
                        .error(R.drawable.pokeball)
                        .into(imageView);*/

                //ImageLoader from Volley Library
                ImageLoader imageLoader = PokedexApplication.getInstance().getImageLoader();

                // If you are using normal ImageView
                imageLoader.get(mPokemon.getAvatar(), new ImageLoader.ImageListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ERROR", "Image Load Error: " + error.getMessage());
                    }

                    @Override
                    public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                        if (response.getBitmap() != null) {
                            // load image into imageview
                            mBitmap = response.getBitmap();
                            imageView.setImageBitmap(mBitmap);
                        }
                    }
                });
            }else{
                Picasso.with(getActivity())
                        .load(R.drawable.pokeball)
                        .placeholder(R.drawable.pokeball)
                        .error(R.drawable.pokeball)
                        .into(imageView);
            }
        }

        return rootView;
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.pokemon_detail, menu);

        // Get the menu item.
        MenuItem item = menu.findItem(R.id.action_share);

        // Get the provider and hold onto it to set/change the share intent.
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(getDefaultShareIntent());

        super.onCreateOptionsMenu(menu, inflater);

    }

    public Intent getDefaultShareIntent(){
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        if(mPokemon != null){
          sendIntent.putExtra(Intent.EXTRA_SUBJECT, "¿Quién es éste pokemon?");
          sendIntent.putExtra(Intent.EXTRA_TEXT, mPokemon.getNombre());
        }

        return sendIntent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(getActivity());
            return true;
        }else if (id == R.id.action_see){
            if (mPokemon != null && mPokemon.getAvatar() != null && mPokemon.getAvatar().length() > 0) {
                urlImage = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                            mBitmap, mPokemon.getNombre(), "");

                Log.i("IMAGE", "Image URLr: " + urlImage);

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlImage));
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

}
