package com.arsen.listofarticles.presenters;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.models.ArticleSingleModel;
import com.arsen.listofarticles.rest.services.ArticlesService;
import com.arsen.listofarticles.util.Constants;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ArticleSinglePresenter {

    @Inject
    ArticleSingleModel articleSingleModel;

    @Inject
    ArticlesService articlesService;

    private ArticleSingleView articleSingleView;
    private String id;

    public void attachView(ArticleSingleView articleSingleView) {
        this.articleSingleView = articleSingleView;

        ((App) articleSingleView.provideContext()).getNetComponent().inject(this);
    }

    public void detachView() {
        this.articleSingleView = null;
    }

    public void loadImage() {
        articleSingleModel.loadData(
                articlesService.
                        getArticle(
                                id,
                                Constants.API_KEY,
                                Constants.FIELDS).
                        subscribeOn(Schedulers.io()).
                        observeOn(AndroidSchedulers.mainThread()).
                        map(article -> article).
                        subscribe(
                                article -> articleSingleView.loadImage(article.getResponse().getContent().getFields().getThumbnail()),
                                error -> {
                                    //TODO implement UI presentation, for now just print stacktrace
                                    error.printStackTrace();
                                }
                        )
        );
    }
}
