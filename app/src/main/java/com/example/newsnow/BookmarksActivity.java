package com.example.newsnow;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.List;

public class BookmarksActivity extends AppCompatActivity {
    private RecyclerView bookmarksRecyclerView;
    private List<Article> bookmarkedArticles;
    private ArticleDatabaseHelper articleDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        bookmarksRecyclerView = findViewById(R.id.bookmarks_recycler_view);
        articleDatabaseHelper = new ArticleDatabaseHelper(this);
        bookmarkedArticles = articleDatabaseHelper.getAllArticles();

        setupRecyclerView();
    }

    void setupRecyclerView() {
        bookmarksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        BookmarksRecyclerAdapter adapter = new BookmarksRecyclerAdapter(bookmarkedArticles, this);
        bookmarksRecyclerView.setAdapter(adapter);
    }
}
