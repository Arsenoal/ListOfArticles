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
import com.arsen.listofarticles.rest.models.interfaces.ArticleField;
import com.arsen.listofarticles.util.Constants;
import com.arsen.listofarticles.util.Quad;
import com.arsen.listofarticles.util.helper.ScreenHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BaseTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder> {
    private ArrayList<ArticleField> articles;
    private AppCompatActivity appCompatActivity;
    private final int DP_150;
    private final PublishSubject<Quad<String, String, String, AppCompatImageView>> onClickSubject;

    public ArticlesAdapter() {
        this.articles = new ArrayList<>();
        this.DP_150 = ScreenHelper.convertDpToPixel(150);
        this.onClickSubject = PublishSubject.create();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        appCompatActivity = (AppCompatActivity) recyclerView.getContext();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        View rootView;
        AppCompatTextView articleCategory;
        AppCompatTextView articleTitle;
        AppCompatImageView articleImage;

        ArticleHolder(View view) {
            super(view);

            this.rootView = view;

            this.articleImage = view.findViewById(R.id.article_img);
            this.articleTitle = view.findViewById(R.id.article_title);
            this.articleCategory = view.findViewById(R.id.article_category);
        }

        void bind(ArticleField article) {
            String thumbNail = article.getThumbnail();

            if (!appCompatActivity.isDestroyed()) {
                if (thumbNail != null)
                    Glide.
                            with(appCompatActivity).
                            load(thumbNail).
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
                else
                    articleImage.setImageResource(R.drawable.ic_android);
            }

            String title = article.getTitle();
            String category = article.getCategory();

            if (title != null) articleTitle.setText(title);
            if (category != null) articleCategory.setText(category);


            rootView.setOnClickListener(v -> onClickSubject.onNext(new Quad<>(article.getArticleId(), article.getId(), Constants.LIST_ARTICLE, articleImage)));
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

    public void addNewArticle(ArticleField articleField) {
        articles.add(0, articleField);
        notifyItemInserted(0);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void reset() {
        articles.clear();
        notifyDataSetChanged();
    }

    public Observable<Quad<String, String, String, AppCompatImageView>> getArticleIdOnItemClick() {
        return onClickSubject.as(upstream -> upstream);
    }
}
