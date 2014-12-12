package com.example.rafael.pokedex;

/**
 * Created by rafael on 10/31/14.
 */

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PokemonDetailFragment extends Fragment {

    private static final String ARG_NOMBRE = "pokemon";
    private Pokemon mPokemon;
    private ShareActionProvider mShareActionProvider;
    private Bitmap mBitmap;
    private String urlImage;
    private ProgressDialog mProgressDialog;
    private String mLocation;

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

        final Button button = (Button) rootView.findViewById(R.id.saw_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                runTask();
            }
        });

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
        }else if(id == R.id.action_map){
            showMap();
        }

        return super.onOptionsItemSelected(item);
    }

    public void showMap() {
        Uri geoLocation = Uri.parse("geo:47.6,-122.3");
        //.buildUpon().appendQueryParameter("q", location).build();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void runTask(){
        if (mPokemon != null) {
            // Tag used to cancel the request
            String tag_json_obj = "json_obj_req";

            final String endpointLocation = "https://mi-pokedex.herokuapp.com/api/v1/pokemons";
            final int pokemon_id = mPokemon.getPokemonId();
            final String latitude = "1.89";
            final String longitude = "-2.36";


            Uri builtUri = Uri.parse(endpointLocation).buildUpon()
                    .appendPath(Integer.toString(pokemon_id))
                    .appendPath("lugar")
                    .build();

            String url = builtUri.toString();

            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.show();

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("lat", latitude);
            params.put("lon", longitude);

            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("RESPONSE FROM API::", response.toString());
                            mProgressDialog.hide();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Location has successfully been added.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("RESPONSE ERROR::", "Error: " + error.getMessage());
                    mProgressDialog.hide();
                    Toast.makeText(getActivity().getApplicationContext(),
                                   "Already registered location.",
                                   Toast.LENGTH_SHORT).show();
                }
            })

            /*{

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("lat", latitude);
                    params.put("lon", longitude);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("Content-Type", "application/json");

                    return headers;
                }
            }*/;

            // Adding request to request queue
            PokedexApplication.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }
    }

}
