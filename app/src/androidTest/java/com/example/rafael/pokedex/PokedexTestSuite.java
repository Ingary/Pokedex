package com.example.rafael.pokedex;

import android.test.suitebuilder.TestSuiteBuilder;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Created by rafael on 12/11/14.
 */
public class PokedexTestSuite extends TestSuite {

    public static Test suite(){
        return new TestSuiteBuilder(PokedexTestSuite.class).includeAllPackagesUnderHere().build();
    }

    public PokedexTestSuite() {
        super();
    }
}
