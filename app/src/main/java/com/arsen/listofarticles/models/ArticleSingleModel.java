package com.arsen.listofarticles.models;

import android.content.Context;

import com.arsen.listofarticles.App;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ArticleSingleModel {

    @Inject
    CompositeDisposable compositeDisposable;

    public ArticleSingleModel(Context context) {
        ((App)context).getNetComponent().inject(this);
    }

    public void loadData(Disposable disposable) {
        compositeDisposable.add(disposable);
    }
}