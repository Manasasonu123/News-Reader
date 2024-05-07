package com.example.newsnow;

import android.content.Context;
import android.content.Intent;
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

public class BookmarksRecyclerAdapter extends RecyclerView.Adapter<BookmarksRecyclerAdapter.BookmarksViewHolder> {
    private List<Article> bookmarkedArticles;
    private Context context;


    public BookmarksRecyclerAdapter(List<Article> articles, Context context) {
        this.bookmarkedArticles = articles;
        this.context = context;
    }
    @NonNull
    @Override
    public BookmarksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bookmarks_recycler_row, parent, false);
        return new BookmarksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarksViewHolder holder, int position) {
        Article article = bookmarkedArticles.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                .error(R.drawable.try_icon)
                .placeholder(R.drawable.try_icon)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the URL of the post
                String url = article.getUrl();

                // Start a new activity to display the webview
                Intent intent = new Intent(context,NewsFullActivity.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookmarkedArticles.size();
    }

    class BookmarksViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, sourceTextView;
        ImageView imageView;

        public BookmarksViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);
        }
    }
}