package com.arsen.listofarticles;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.arsen.listofarticles.dagger.component.DaggerNetComponent;
import com.arsen.listofarticles.dagger.component.NetComponent;
import com.arsen.listofarticles.dagger.module.AppModule;
import com.arsen.listofarticles.dagger.module.DBModule;
import com.arsen.listofarticles.dagger.module.ModelsModule;
import com.arsen.listofarticles.dagger.module.NetworkModule;
import com.arsen.listofarticles.dagger.module.PresentersModule;
import com.arsen.listofarticles.util.Constants;

public class App extends MultiDexApplication {
    private NetComponent netComponent;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        netComponent = DaggerNetComponent.
                builder().
                networkModule(new NetworkModule(Constants.BASE_URL)).
                appModule(new AppModule(this)).
                dBModule(new DBModule(getApplicationContext())).
                modelsModule(new ModelsModule(getApplicationContext())).
                presentersModule(new PresentersModule()).
                build();
    }

    public NetComponent getNetComponent() {
        return netComponent;
    }
}
