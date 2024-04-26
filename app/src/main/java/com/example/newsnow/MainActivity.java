package com.example.newsnow;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getNews();
    }

    void getNews(){
        NewsApiClient newsApiClient=new NewsApiClient("1b4402c6ca0443dfa3d94cf13e282816");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                       response.getArticles().forEach((a)->{
                           Log.i("Article",a.getTitle());
                       });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT Failure",throwable.getMessage());
                    }
                }
        );
    }
}