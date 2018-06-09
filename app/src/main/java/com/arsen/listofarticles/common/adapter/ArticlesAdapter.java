package com.arsen.listofarticles.common.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.arsen.listofarticles.R;
import com.arsen.listofarticles.common.model.ArticleField;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleHolder> {

    private ArrayList<ArticleField> articles;
    private AppCompatActivity appCompatActivity;

    public ArticlesAdapter() {
        articles = new ArrayList<>();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        appCompatActivity = (AppCompatActivity) recyclerView.getContext();
    }

    class ArticleHolder extends RecyclerView.ViewHolder {
        AppCompatImageView articleImage;
        AppCompatTextView articleTitle;

        public ArticleHolder(View view) {
            super(view);

            articleImage = view.findViewById(R.id.article_img);
            articleTitle = view.findViewById(R.id.article_title);
        }

        void bind(ArticleField article) {
            if (!appCompatActivity.isDestroyed())
                Glide.
                        with(appCompatActivity).
                        load(article.getThumbnail()).
                        apply(RequestOptions.centerCropTransform()).
                        into(articleImage);

            String title = article.getTitle();

            if (title != null)
                articleTitle.setText(title);

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
