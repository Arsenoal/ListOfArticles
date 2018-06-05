package com.arsen.listofarticles;

import android.support.multidex.MultiDexApplication;

import com.arsen.listofarticles.dagger.component.DaggerNetComponent;
import com.arsen.listofarticles.dagger.component.NetComponent;
import com.arsen.listofarticles.dagger.module.NetworkModule;

public class App extends MultiDexApplication {
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.
                builder().
                networkModule(new NetworkModule("")).
                build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
