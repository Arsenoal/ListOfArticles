package com.arsen.listofarticles.dagger.component;

import com.arsen.listofarticles.activity.ArticlesViewActivity;
import com.arsen.listofarticles.dagger.module.AppModule;
import com.arsen.listofarticles.dagger.module.NetworkModule;
import com.arsen.listofarticles.models.ArticlesModel;
import com.arsen.listofarticles.presenters.ArticlesPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {
        NetworkModule.class,
        AppModule.class
})
public interface NetComponent {
    void inject(ArticlesPresenter articlesPresenter);
    void inject(ArticlesModel articlesModel);
}
