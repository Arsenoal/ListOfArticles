package com.arsen.listofarticles.dagger.component;

import com.arsen.listofarticles.activity.ArticlesViewActivity;
import com.arsen.listofarticles.dagger.module.AppModule;
import com.arsen.listofarticles.dagger.module.DBModule;
import com.arsen.listofarticles.dagger.module.ModelsModule;
import com.arsen.listofarticles.dagger.module.NetworkModule;
import com.arsen.listofarticles.dagger.module.PresentersModule;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.presenters.ArticlesPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        AppModule.class,
        DBModule.class,
        ModelsModule.class,
        PresentersModule.class
})
public interface NetComponent {
    void inject(ArticlesPresenter articlesPresenter);
    void inject(ArticlesModel articlesModel);
    void inject(ArticlesViewActivity articlesModel);
}
