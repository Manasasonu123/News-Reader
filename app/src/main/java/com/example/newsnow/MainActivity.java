package com.example.newsnow;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.TopHeadlinesRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    private static final long INTERVAL_PERIOD = 60 * 60 * 1000; // 1 hour
    private static final int REQUEST_CODE = 123;
    RecyclerView recyclerView;
    List<Article> articleList=new ArrayList<>();
    NewsRecyclerAdapter adapter;
    LinearProgressIndicator progressIndicator;
    Button btn1,btn2,btn3,btn4,btn5,btn6,btn7;
    SearchView searchview;
    Switch switcher;
    boolean nightmode;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.news_recycler_view);
        progressIndicator=findViewById(R.id.progress_bar);
        searchview=findViewById(R.id.search_view);
        btn1=findViewById(R.id.btn_1);
        btn2=findViewById(R.id.btn_2);
        btn3=findViewById(R.id.btn_3);
        btn4=findViewById(R.id.btn_4);
        btn5=findViewById(R.id.btn_5);
        btn6=findViewById(R.id.btn_6);
        btn7=findViewById(R.id.btn_7);
//        btnnoti=findViewById(R.id.btn_noti);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
        btn5.setOnClickListener(this);
        btn6.setOnClickListener(this);
        btn7.setOnClickListener(this);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
            }
        }
//        btnnoti.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makeNotification();
//            }
//        });
        startPeriodicTask();

//        scheduleNewsUpdate();

        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getNews("GENERAL",query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        setupRecyclerView();
        getNews("GENERAL",null);

//        switch dark and night
//        getSupportActionBar().hide();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        switcher=findViewById(R.id.switcher);
        //used sharedPreferences to save mode if exit the app and go back again
        sharedPreferences=getSharedPreferences("MODE", Context.MODE_PRIVATE);
        nightmode=sharedPreferences.getBoolean("night",false); //light mode is the default

        if(nightmode){
            switcher.setChecked(true);
        }

        switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nightmode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("night",false);
                }else{
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor=sharedPreferences.edit();
                    editor.putBoolean("night",true);

                }
                editor.apply();
            }
        });

    }

//    private void scheduleNewsUpdate() {
//        // Create a periodic work request to execute NewsWorker every 1 hour
//        PeriodicWorkRequest newsWorkRequest = new PeriodicWorkRequest.Builder(
//                NewsWorker.class,
//                1, // Repeat interval
//                TimeUnit.HOURS
//        ).build();
//
//        // Enqueue the work request
//        WorkManager.getInstance(getApplicationContext()).enqueue(newsWorkRequest);
//    }

    private void startPeriodicTask() {
        Intent intent = new Intent(this, NewsUpdateService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, REQUEST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set the interval period for the periodic task
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime(), INTERVAL_PERIOD, pendingIntent);
        }
    }
//    public  void makeNotification(){
//        String channelid="CHANNEL_ID_NOTIFICATION";
//        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),channelid);
//
//        builder.setSmallIcon(R.drawable.baseline_add_circle_24)
//                .setContentTitle("Notification Title")
//                .setContentText("Some text for Notification here")
//                .setAutoCancel(true)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.putExtra("data","Some value");
//
//        PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),0,intent,PendingIntent.FLAG_MUTABLE);
//        builder.setContentIntent(pendingIntent);
//        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelid);
//            if(notificationChannel == null){
//                int importance = NotificationManager.IMPORTANCE_HIGH;
//                notificationChannel = new NotificationChannel(channelid, "Some description", importance);
//                notificationChannel.setLightColor(Color.GREEN);
//                notificationChannel.enableVibration(true);
//                notificationManager.createNotificationChannel(notificationChannel);
//            }
//        }
//
//        notificationManager.notify(0,builder.build());
//    }

    void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter=new NewsRecyclerAdapter(articleList);
        recyclerView.setAdapter(adapter);
    }

    void changeInProgress(boolean show){
        if(show)
            progressIndicator.setVisibility(View.VISIBLE);
        else
            progressIndicator.setVisibility(View.INVISIBLE);
    }

    void getNews(String category,String query){
        changeInProgress(true);
        NewsApiClient newsApiClient=new NewsApiClient("1b4402c6ca0443dfa3d94cf13e282816");
        newsApiClient.getTopHeadlines(
                new TopHeadlinesRequest.Builder()
                        .language("en")
                        .category(category)
                        .q(query)
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
//                       response.getArticles().forEach((a)->{
//                           Log.i("Article",a.getTitle());
//                       });
                        runOnUiThread(()->{
                            changeInProgress(false);
                            articleList=response.getArticles();
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i("GOT Failure",throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public void onClick(View v) {
    Button btn=(Button) v;
    String category=btn.getText().toString();
    getNews(category,null);
    }
}