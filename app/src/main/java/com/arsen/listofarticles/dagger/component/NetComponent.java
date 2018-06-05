package com.arsen.listofarticles.dagger.component;

import com.arsen.listofarticles.activity.MainActivity;
import com.arsen.listofarticles.dagger.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {NetworkModule.class})
public interface NetComponent {
    void inject(MainActivity mainActivity);
}
