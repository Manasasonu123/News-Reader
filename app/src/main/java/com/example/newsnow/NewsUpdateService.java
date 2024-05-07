package com.example.newsnow;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class NewsUpdateService extends IntentService {

    private static final String TAG = "NewsWorker";
    private static final String CHANNEL_ID = "NewsChannel";
    private static final int NOTIFICATION_ID_BASE = 1000;

    public NewsUpdateService() {
        super("NewsUpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        fetchNewsUpdates();
    }

    private void fetchNewsUpdates() {
        NewsApiClient newsApiClient = new NewsApiClient("1b4402c6ca0443dfa3d94cf13e282816");
        String[] categories = {"general"};
        for (String category : categories) {
            newsApiClient.getTopHeadlines(new TopHeadlinesRequest.Builder()
                            .category(category)
                            .language("en")
                            .build(),
                    new NewsApiClient.ArticlesResponseCallback() {
                        @Override
                        public void onSuccess(ArticleResponse response) {
                            List<Article> articles = response.getArticles();
                            if (articles != null && !articles.isEmpty()) {
                                for (int i = 0; i < articles.size(); i++) {
                                    // Generate unique identifier based on index
                                    String articleId = generateUniqueId();
                                    Article article = articles.get(i);
                                    sendNotification(article, articleId);
                                }
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            Log.e(TAG, "Failed to fetch news updates: " + throwable.getMessage());
                        }
                    }
            );
        }
    }

    private String generateUniqueId() {
        // Generate a unique identifier using UUID
        return UUID.randomUUID().toString();
    }


    private void sendNotification(Article article, String articleId) {
        Context applicationContext = getApplicationContext();

        Intent intent = new Intent(applicationContext, NewsFullActivity.class);
        intent.putExtra("url", article.getUrl());
        intent.putExtra("headline", article.getTitle());
        intent.putExtra("article_id", articleId); // Use the provided articleId parameter
        PendingIntent pendingIntent = PendingIntent.getActivity(applicationContext, articleId.hashCode(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_add_circle_24)
                .setContentTitle("New Article")
                .setContentText(article.getTitle())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) applicationContext.getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(notificationManager);

        int notificationId = NOTIFICATION_ID_BASE + new Random().nextInt(1000); // Generate unique notification ID
        notificationManager.notify(notificationId, builder.build());
    }

    private void createNotificationChannel(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "News Channel";
            String description = "Channel for news notifications";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableLights(true);
            channel.setLightColor(Color.GREEN);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
//package com.example.newsnow;
//
//import android.app.IntentService;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.os.Build;
//import android.util.Log;
//
//import androidx.core.app.NotificationCompat;
//
//import com.kwabenaberko.newsapilib.NewsApiClient;
//import com.kwabenaberko.newsapilib.models.Article;
//import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
//import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
//
//import java.util.List;
//
//public class NewsUpdateService extends IntentService {
//
//    private static final String TAG = "NewsUpdateService";
//    private static final String CHANNEL_ID = "NewsChannel";
//    private static final int NOTIFICATION_ID = 1;
//
//    public NewsUpdateService() {
//        super("NewsUpdateService");
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//
//                fetchNewsUpdates();
//    }
//
//    private void fetchNewsUpdates() {
//        NewsApiClient newsApiClient = new NewsApiClient("1b4402c6ca0443dfa3d94cf13e282816");
//        String[] categories = {"business", "entertainment", "health", "science", "sports", "technology"};
//        for (String category : categories) {
//            newsApiClient.getTopHeadlines(new TopHeadlinesRequest.Builder()
//                            .category(category)
//                            // Specify the category here (e.g., general)
//                            .build(),
//                    new NewsApiClient.ArticlesResponseCallback() {
//                        @Override
//                        public void onSuccess(ArticleResponse response) {
//                            List<Article> articles = response.getArticles();
//                            if (articles != null && !articles.isEmpty()) {
//                                // Check if new articles are available
//                                // If yes, trigger notification
//                                sendNotification();
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Throwable throwable) {
//                            Log.e(TAG, "Failed to fetch news updates: " + throwable.getMessage());
//                        }
//                    }
//            );
//        }
//    }
//
//    private void sendNotification() {
//        Log.d(TAG, "sendNotification() called");
//        String message = "New articles are available!";
//        String channelid = "CHANNEL_ID_NOTIFICATION";
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelid);
//
//        builder.setSmallIcon(R.drawable.baseline_add_circle_24)
//                .setContentTitle("New Articles")
//                .setContentText(message)
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("data", "Some value");
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelid);
//            if (notificationChannel == null) {
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                notificationChannel = new NotificationChannel(channelid, "News Updates", importance);
//                notificationChannel.setLightColor(Color.GREEN);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//
//        notificationManager.notify(0, builder.build());
//    }
//}
//
