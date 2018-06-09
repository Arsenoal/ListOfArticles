package com.arsen.listofarticles.common.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.model.ArticleField;
import com.arsen.listofarticles.util.helper.ScreenHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder> {

    private ArrayList<ArticleField> articles;
    private AppCompatActivity appCompatActivity;
    private final int DP_150;

    public ArticlesAdapter() {
        articles = new ArrayList<>();
        DP_150 = ScreenHelper.convertDpToPixel(150);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        appCompatActivity = (AppCompatActivity) recyclerView.getContext();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        AppCompatTextView articleCategory;
        AppCompatTextView articleTitle;
        AppCompatImageView articleImage;

        public ArticleHolder(View view) {
            super(view);

            articleImage = view.findViewById(R.id.article_img);
            articleTitle = view.findViewById(R.id.article_title);
            articleCategory = view.findViewById(R.id.article_category);
        }

        void bind(ArticleField article) {
            if (!appCompatActivity.isDestroyed())
                Glide.
                        with(appCompatActivity).
                        load(article.getThumbnail()).
                        apply(RequestOptions.centerCropTransform()).
                        into(new BaseTarget<Drawable>() {
                            @Override
                            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                articleImage.setImageDrawable(resource);
                            }

                            @Override
                            public void getSize(@NonNull SizeReadyCallback cb) {
                                cb.onSizeReady(SIZE_ORIGINAL, DP_150);
                            }

                            @Override
                            public void removeCallback(@NonNull SizeReadyCallback cb) {

                            }
                        });

            String title = article.getTitle();
            String category = article.getCategory();

            if (title != null) articleTitle.setText(title);
            if (category != null) articleCategory.setText(category);

        }
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_recycler_item, parent, false);

        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        holder.bind(articles.get(position));
    }

    public void addArticles(ArrayList<? extends ArticleField> newArticles) {
        articles.addAll(newArticles);
        notifyItemRangeChanged(articles.size() - newArticles.size(), newArticles.size());
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
