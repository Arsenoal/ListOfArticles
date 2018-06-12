package com.arsen.listofarticles.dagger.module;

import android.content.Context;

import com.arsen.listofarticles.models.ArticlesModel;

import dagger.Module;
import dagger.Provides;

@Module
public class ModelsModule {
    private Context context;

    public ModelsModule(Context context) {
        this.context = context;
    }

    @Provides
    ArticlesModel provideArticlesModel() {
        return new ArticlesModel(context);
    }
}
