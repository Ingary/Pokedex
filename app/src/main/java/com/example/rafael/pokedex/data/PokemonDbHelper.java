package com.example.rafael.pokedex.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rafael.pokedex.data.PokedexContract.PokemonEntry;
import com.example.rafael.pokedex.data.PokedexContract.LocationEntry;

/**
 * Created by rafael on 12/11/14.
 */
public class PokemonDbHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "pokedex.db";
    public static Integer DATABASE_VERSION = 1;

    public PokemonDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
       final String CREATE_TABLE_POKEMONS = "CREATE TABLE " + PokemonEntry.TABLE_NAME +"(" +
               PokemonEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
               PokemonEntry.COLUMN_NOMBRE + " TEXT NOT NULL," +
               PokemonEntry.COLUMN_AVATAR + " TEXT NOT NULL," +
               PokemonEntry.COLUMN_ALTURA + " REAL NOT NULL," +
               PokemonEntry.COLUMN_NUMERO + " INTEGER NOT NULL," +
               PokemonEntry.COLUMN_PESO + " REAL NOT NULL" + ");";

       final String CREATE_TABLE_LOCATION = "CREATE TABLE " + LocationEntry.TABLE_NAME +"(" +
                LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                LocationEntry.COLUMN_POKEMON_ID + " INTEGER NOT NULL," +
                LocationEntry.COLUMN_LATITUD + " REAL NOT NULL," +
                LocationEntry.COLUMN_LONGITUD + " REAL NOT NULL, " +
                " FOREIGN KEY (" + LocationEntry.COLUMN_POKEMON_ID + ") REFERENCES " +
                LocationEntry.TABLE_NAME + " (" + PokemonEntry._ID + ")" + ");";

       sqLiteDatabase.execSQL(CREATE_TABLE_POKEMONS);
       sqLiteDatabase.execSQL(CREATE_TABLE_LOCATION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PokemonEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LocationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
