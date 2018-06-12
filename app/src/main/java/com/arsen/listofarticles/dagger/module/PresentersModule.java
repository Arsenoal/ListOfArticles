package com.arsen.listofarticles.dagger.module;

import com.arsen.listofarticles.presenters.ArticleSinglePresenter;
import com.arsen.listofarticles.presenters.ArticlesPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class PresentersModule {

    @Provides
    ArticlesPresenter provideArticlesPresenter() {
        return new ArticlesPresenter();
    }

    @Provides
    ArticleSinglePresenter provideArticleSinglePresenter() {
        return new ArticleSinglePresenter();
    }
}
