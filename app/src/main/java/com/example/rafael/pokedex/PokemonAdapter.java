package com.example.rafael.pokedex;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.List;

/**
 * Created by rafael on 10/26/14.
 */
public class PokemonAdapter extends ArrayAdapter<Pokemon> {

    private List<Pokemon> pokemons;
    private Context context;

    public PokemonAdapter(List<Pokemon> pokemons, Context ctx) {
        super(ctx, R.layout.list_item_pokemon, pokemons);
        this.pokemons = pokemons;
        this.context = ctx;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            // This a new view we inflate the new layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_pokemon, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.list_item_pokemon_textview);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.list_item_pokemon_imageview);
            view.setTag(viewHolder);
        }
        final ViewHolder holder = (ViewHolder) view.getTag();
        Pokemon p = (Pokemon)getItem(i);
        holder.textView.setText(p.getNombre());

        //Picasso.with(context).load(p.getUrlImg()).into(holder.imageView);
        if ((p.getAvatar() == null) || p.getAvatar().isEmpty()) {
            holder.imageView.setImageResource(R.drawable.pokeball);
        } else{
            /*Picasso.with(context)
                    .load(p.getAvatar())
                    .placeholder(R.drawable.pokeball)
                    .error(R.drawable.pokeball)
                    .into(holder.imageView);*/

            //ImageLoader from Volley Library
            ImageLoader imageLoader = PokedexApplication.getInstance().getImageLoader();

            // If you are using normal ImageView
            imageLoader.get(p.getAvatar(), new ImageLoader.ImageListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("ERROR", "Image Load Error: " + error.getMessage());
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        // load image into imageview
                        holder.imageView.setImageBitmap(response.getBitmap());
                    }
                }
            });
        }

        return view;
    }

    static class ViewHolder {
        public TextView textView;
        public ImageView imageView;
    }
}
