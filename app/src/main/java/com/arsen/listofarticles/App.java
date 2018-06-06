package com.arsen.listofarticles;

import android.support.multidex.MultiDexApplication;

import com.arsen.listofarticles.dagger.component.DaggerNetComponent;
import com.arsen.listofarticles.dagger.component.NetComponent;
import com.arsen.listofarticles.dagger.module.NetworkModule;
import com.arsen.listofarticles.util.Constants;

public class App extends MultiDexApplication {
    private NetComponent netComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.
                builder().
                networkModule(new NetworkModule(Constants.BASE_URL)).
                build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
