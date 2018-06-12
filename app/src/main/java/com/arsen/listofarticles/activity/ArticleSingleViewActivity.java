package com.arsen.listofarticles.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageView;

import com.arsen.listofarticles.R;
import com.arsen.listofarticles.interfaces.view.ArticleSingleView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleSingleViewActivity extends AppCompatActivity implements ArticleSingleView {

    @BindView(R.id.article_image)
    AppCompatImageView articleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_single_view);

        ButterKnife.bind(this);
    }

    @Override
    public void loadImage() {

    }
}
