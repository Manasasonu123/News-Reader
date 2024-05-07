package com.example.newsnow;

import com.kwabenaberko.newsapilib.models.Article;

public class BookmarkableArticle extends Article {
    private boolean isBookmarked;

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }
}

