package org.lucasr.layoutsamples.app;

import android.app.Application;
import android.content.Context;

import org.lucasr.layoutsamples.async.ElementCache;

public class App extends Application {
    private ElementCache mElementCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mElementCache = new ElementCache();
    }

    public ElementCache getElementCache() {
        return mElementCache;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }
}
