package com.arsen.listofarticles.dagger.module;

import com.arsen.listofarticles.presenters.ArticlesPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {
    @Provides
    ArticlesPresenter provideArticlesPresenter() {
        return new ArticlesPresenter();
    }
}
