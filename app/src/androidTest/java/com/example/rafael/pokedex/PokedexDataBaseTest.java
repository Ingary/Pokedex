package com.example.rafael.pokedex;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.test.AndroidTestCase;

import com.example.rafael.pokedex.data.PokedexContract.PokemonEntry;
import com.example.rafael.pokedex.data.PokemonDbHelper;

/**
 * Created by rafael on 12/11/14.
 */
public class PokedexDataBaseTest extends AndroidTestCase {

    public void testCreateDb() throws Throwable{
        mContext.deleteDatabase(PokemonDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new PokemonDbHelper(mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());
        db.close();
    }

    public void testInsertDb(){
        int id = 1;
        String nombre = "Charmeleon";
        String avatar = "http://img1.wikia.nocookie.net/__cb20110530041701/pokemonfakemon/images/0/06/Charmeleon_Dream.png";
        int numero = 5;
        double altura = 3.23;
        double peso = 223.36;

        ContentValues values = new ContentValues();
        values.put(PokemonEntry._ID, id);
        values.put(PokemonEntry.COLUMN_NOMBRE, nombre);
        values.put(PokemonEntry.COLUMN_AVATAR, avatar);
        values.put(PokemonEntry.COLUMN_NUMERO, numero);
        values.put(PokemonEntry.COLUMN_ALTURA, altura);
        values.put(PokemonEntry.COLUMN_PESO, peso);

        SQLiteDatabase db = new PokemonDbHelper(mContext).getWritableDatabase();

        long pokemon_id = db.insert(PokemonEntry.TABLE_NAME, null, values);

        assertEquals(id, pokemon_id);
        db.close();
    }


}
