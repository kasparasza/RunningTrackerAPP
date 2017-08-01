package com.example.kasparasza.runningtrackerapp;

import com.facebook.stetho.Stetho;

/**
 * A class that is necessary for the app to implement Stetho debugging tools
 */

public class Application extends android.app.Application{


    @Override
    public void onCreate() {
        super.onCreate();

        // initialization of Stetho
        Stetho.initializeWithDefaults(this);

        // Create an InitializerBuilder
        Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);

        // Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );

        // Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(getApplicationContext())
        );

        // Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();

        // Initialize Stetho with the Initializer
        Stetho.initialize(initializer);
    }
}
