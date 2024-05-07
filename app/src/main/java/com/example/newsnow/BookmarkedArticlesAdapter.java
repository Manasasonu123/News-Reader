package com.example.newsnow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookmarkedArticlesAdapter extends RecyclerView.Adapter<BookmarkedArticlesAdapter.BookmarkedArticlesViewHolder> {

    private List<BookmarkableArticle> bookmarkedArticlesList;

    public BookmarkedArticlesAdapter(List<BookmarkableArticle> bookmarkedArticlesList) {
        this.bookmarkedArticlesList = bookmarkedArticlesList;
    }

    @NonNull
    @Override
    public BookmarkedArticlesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmarks_recycler_row, parent, false);
        return new BookmarkedArticlesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkedArticlesViewHolder holder, int position) {
        BookmarkableArticle article = bookmarkedArticlesList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        // Set the bookmark icon based on the article's bookmark status
        if (article.isBookmarked()) {
            holder.bookmarkButton.setImageResource(R.drawable.bookmark);
        } else {
            holder.bookmarkButton.setImageResource(R.drawable.unbookmarked);
        }
    }

    @Override
    public int getItemCount() {
        return bookmarkedArticlesList.size();
    }

    public static class BookmarkedArticlesViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView;
        ImageView bookmarkButton;

        public BookmarkedArticlesViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            bookmarkButton = itemView.findViewById(R.id.bookmark_button);
        }
    }
}
