package com.arsen.listofarticles.common.adapter;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
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

public class PinnedArticlesAdapter extends RecyclerView.Adapter<PinnedArticlesAdapter.ArticleHolder> {

    private ArrayList<ArticleField> pinnedArticles;
    private AppCompatActivity appCompatActivity;
    private PublishSubject<Quad<String, String, String, AppCompatImageView>> onClickSubject;
    private final int DP_120;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        appCompatActivity = (AppCompatActivity) recyclerView.getContext();
    }

    public PinnedArticlesAdapter() {
        this.pinnedArticles = new ArrayList<>();
        this.DP_120 = ScreenHelper.convertDpToPixel(120);
        this.onClickSubject = PublishSubject.create();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        AppCompatImageView articleImage;
        View itemView;

        ArticleHolder(View itemView) {
            super(itemView);

            this.articleImage = itemView.findViewById(R.id.article_image);
            this.itemView = itemView;
        }

        void bind(ArticleField articleField) {
            if (!appCompatActivity.isDestroyed()) {
                if (articleField.getThumbnail() != null)
                    Glide.
                            with(appCompatActivity).
                            load(articleField.getThumbnail()).
                            apply(RequestOptions.centerCropTransform()).
                            into(new BaseTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    articleImage.setImageDrawable(resource);
                                }

                                @Override
                                public void getSize(@NonNull SizeReadyCallback cb) {
                                    cb.onSizeReady(DP_120, DP_120);
                                }

                                @Override
                                public void removeCallback(@NonNull SizeReadyCallback cb) {

                                }
                            });
                else
                    articleImage.setImageResource(R.drawable.ic_android);
            }

            itemView.setOnClickListener(v -> onClickSubject.onNext(new Quad<>(articleField.getArticleId(), articleField.getId(), Constants.PINNED_ARTICLE, articleImage)));
        }
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pinned_article_recycler_item, parent, false);

        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        holder.bind(pinnedArticles.get(position));
    }

    @Override
    public int getItemCount() {
        return pinnedArticles.size();
    }

    public void addArticles(ArrayList<? extends ArticleField> articleFields) {
        pinnedArticles.addAll(articleFields);
        notifyItemRangeChanged(pinnedArticles.size() - articleFields.size(), articleFields.size());
    }

    public void addArticle(ArticleField articleField) {
        pinnedArticles.add(articleField);
        notifyItemInserted(pinnedArticles.size() - 1);
    }

    public Observable<Quad<String, String, String, AppCompatImageView>> getArticleOnItemClick() {
        return onClickSubject.as(upstream -> upstream);
    }

    public int getLastItemsPosition() {
        return pinnedArticles.size() - 1;
    }
}
