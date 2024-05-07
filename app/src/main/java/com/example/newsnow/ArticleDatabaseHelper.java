package com.example.newsnow;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kwabenaberko.newsapilib.models.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "articles.db";
    private static final int DATABASE_VERSION = 1;

    public ArticleDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_ARTICLES_TABLE = "CREATE TABLE articles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT NOT NULL, " +
                "source TEXT NOT NULL, " +
                "url TEXT NOT NULL UNIQUE, " +
                "imageUrl TEXT" +
                ");";
        db.execSQL(SQL_CREATE_ARTICLES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS articles");
        onCreate(db);
    }

    public long addArticle(Article article) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", article.getTitle());
        values.put("source", article.getSource().getName());
        values.put("url", article.getUrl());
        values.put("imageUrl", article.getUrlToImage());

        long id = db.insert("articles", null, values);
        db.close();
        return id;
    }

    public int removeArticle(Article article) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete("articles", "url = ?", new String[]{article.getUrl()});
        db.close();
        return rowsAffected;
    }

    public List<Article> getAllArticles() {
        List<Article> articleList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query("articles", null, null, null, null, null, null);

        int titleIndex = cursor.getColumnIndex("title");
        int sourceIndex = cursor.getColumnIndex("source");
        int urlIndex = cursor.getColumnIndex("url");
        int imageUrlIndex = cursor.getColumnIndex("imageUrl");

        if (cursor.moveToFirst()) {
            do {
                String title = titleIndex >= 0 ? cursor.getString(titleIndex) : null;
                String source = sourceIndex >= 0 ? cursor.getString(sourceIndex) : null;
                String url = urlIndex >= 0 ? cursor.getString(urlIndex) : null;
                String imageUrl = imageUrlIndex >= 0 ? cursor.getString(imageUrlIndex) : null;

                if (title != null && source != null && url != null) {
                    com.kwabenaberko.newsapilib.models.Source articleSource = new com.kwabenaberko.newsapilib.models.Source();
                    articleSource.setName(source);

                    Article article = new Article();
                    article.setTitle(title);
                    article.setSource(articleSource);
                    article.setUrl(url);
                    article.setUrlToImage(imageUrl);

                    articleList.add(article);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return articleList;
    }


}
