package com.example.rafael.pokedex;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity implements
        PokemonFragment.Callbacks{

    private boolean mTwoPane;
    private Pokemon mCurrentPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.pokemon_detail_container) != null){
            ((PokemonFragment) getSupportFragmentManager().findFragmentById(R.id.container)).setActivateOnItemClick(true);
            mTwoPane = true;
        }

        if (savedInstanceState == null && mTwoPane) {
            PokemonDetailFragment fragment = PokemonDetailFragment.newInstance(null);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pokemon_detail_container, fragment).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Pokemon pokemon) {
        mCurrentPokemon = pokemon;

        if (mTwoPane) {
            PokemonDetailFragment fragment = PokemonDetailFragment.newInstance(mCurrentPokemon);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.pokemon_detail_container, fragment).commit();

        }else {
            Intent detailIntent = new Intent(this, PokemonDetailActivity.class);
            detailIntent.putExtra("pokemon", pokemon);
            startActivity(detailIntent);
        }
    }

}
