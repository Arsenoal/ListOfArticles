package com.arsen.listofarticles.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;

import com.arsen.listofarticles.App;
import com.arsen.listofarticles.R;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.presenters.ArticleSinglePresenter;
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.helper.ScreenHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class ArticleSingleViewActivity extends AppCompatActivity implements ArticleSingleView {

    @BindView(R.id.article_image)
    AppCompatImageView articleImage;

    @BindView(R.id.article_title)
    AppCompatTextView titleLabel;

    @BindView(R.id.article_category)
    AppCompatTextView categoryLabel;

    @BindView(R.id.pin_to_dashboard)
    FloatingActionButton pinToDashboard;

    @Inject
    ArticleSinglePresenter articleSinglePresenter;

    @BindView(R.id.root_view)
    CoordinatorLayout rootView;

    @BindString(R.string.success_on_article_pin)
    String successfullyPinned;

    @BindString(R.string.error_on_article_pin)
    String errorOnPin;

    private static int DP_200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single_view);

        ButterKnife.bind(this);
        ((App) getApplication()).getNetComponent().inject(this);

        prepare();

        articleSinglePresenter.attachView(this);
        articleSinglePresenter.loadData();
    }

    private void prepare() {
        DP_200 = ScreenHelper.convertDpToPixel(200);
    }

    @Optional
    @OnClick(R.id.pin_to_dashboard)
    void onPinCLicked() {
        articleSinglePresenter.pinArticle();
        pinToDashboard.setClickable(false);
    }

    @Override
    public void loadData(ArticleField articleField) {
        String thumbNail = articleField.getThumbnail();
        String title = articleField.getTitle();
        String category = articleField.getCategory();

        if (!this.isDestroyed()) {
            if (thumbNail != null) {
                Glide.
                        with(this).
                        load(thumbNail).
                        apply(RequestOptions.centerCropTransform()).
                        into(new BaseTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                articleImage.setImageDrawable(resource);
                            }

                            @Override
                            public void getSize(@NonNull SizeReadyCallback cb) {
                                cb.onSizeReady(SIZE_ORIGINAL, DP_200);
                            }

                            @Override
                            public void removeCallback(@NonNull SizeReadyCallback cb) {

                            }
                        });
            } else
                articleImage.setImageResource(R.drawable.ic_android);
        }

        if (title != null)
            titleLabel.setText(title);

        if (category != null)
            categoryLabel.setText(category);
    }

    @Override
    public Context provideContext() {
        return this;
    }

    @Override
    public void successfullyPinned() {
        Snackbar.make(rootView, successfullyPinned, Snackbar.LENGTH_SHORT).show();
        pinToDashboard.setClickable(true);
    }

    @Override
    public void errorOnPin() {
        Snackbar.make(rootView, errorOnPin, Snackbar.LENGTH_SHORT).show();
        pinToDashboard.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        articleSinglePresenter.detachView();
    }
}
