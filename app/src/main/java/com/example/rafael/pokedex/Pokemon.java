package com.example.rafael.pokedex;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rafael on 10/26/14.
 */
public class Pokemon {
    private String name;
    private String avatar;

    public Pokemon(JSONObject p) throws JSONException {
        this.name = p.getString("nombre");
        this.avatar = p.getString("avatar");

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
