package com.arsen.listofarticles.models;

import android.support.v7.app.AppCompatActivity;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.rest.services.FilmsService;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ArticlesModel {
    private AppCompatActivity appCompatActivity;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    FilmsService filmsService;

    public ArticlesModel(AppCompatActivity appCompatActivity) {
        this.appCompatActivity = appCompatActivity;

        ((App)appCompatActivity.getApplication()).getNetComponent().inject(this);
    }

    public void loadArticles(Disposable disposable) {
        compositeDisposable.add(disposable);
    }

    public void invalidate() {
        compositeDisposable.clear();
    }

    public FilmsService getFilmsService() {
        return filmsService;
    }
}
