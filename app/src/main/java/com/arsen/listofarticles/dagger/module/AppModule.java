package com.arsen.listofarticles.dagger.module;

import android.support.multidex.MultiDexApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private MultiDexApplication multiDexApplication;

    public AppModule(MultiDexApplication multiDexApplication) {
        this.multiDexApplication = multiDexApplication;
    }

    @Provides
    @Singleton
    MultiDexApplication provideMultiDexApplication() {
        return multiDexApplication;
    }
}
