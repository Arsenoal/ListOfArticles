package com.arsen.listofarticles.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;

import com.arsen.listofarticles.R;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;
import com.arsen.listofarticles.presenters.ArticleSinglePresenter;
import com.arsen.listofarticles.util.helper.ScreenHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleSingleViewActivity extends AppCompatActivity implements ArticleSingleView {

    @BindView(R.id.article_image)
    AppCompatImageView articleImage;

    @Inject
    ArticleSinglePresenter articleSinglePresenter;

    private static int DP_200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single_view);

        ButterKnife.bind(this);

        prepare();

        articleSinglePresenter.attachView(this);
        articleSinglePresenter.loadImage();
    }

    private void prepare() {
        DP_200 = ScreenHelper.convertDpToPixel(200);
    }

    @Override
    public void loadImage(String url) {
        if (!this.isDestroyed())
            Glide.
                    with(this).
                    load(url).
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
                         }
                    );
    }

    @Override
    public Context provideContext() {
        return this.getApplicationContext();
    }
}
