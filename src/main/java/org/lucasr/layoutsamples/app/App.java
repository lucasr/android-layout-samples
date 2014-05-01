package org.lucasr.layoutsamples.app;

import android.app.Application;
import android.content.Context;

import org.lucasr.layoutsamples.async.UIElementCache;

public class App extends Application {
    private UIElementCache mElementCache;

    @Override
    public void onCreate() {
        super.onCreate();
        mElementCache = new UIElementCache();
    }

    public UIElementCache getElementCache() {
        return mElementCache;
    }

    public static App getInstance(Context context) {
        return (App) context.getApplicationContext();
    }
}
