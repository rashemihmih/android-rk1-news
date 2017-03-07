package ru.android_rk1_news;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class MainActivity extends AppCompatActivity implements ServiceHelper.Callback {
    private static final String DEFAULT_TOPIC = Topics.AUTO;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private Storage storage;
    private ServiceHelper serviceHelper;
    private TextView newsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = Storage.getInstance(this);
        storage.saveCurrentTopic(DEFAULT_TOPIC);
        serviceHelper = ServiceHelper.getInstance();
        serviceHelper.setNewsReceiverCallback(this);
        newsText = (TextView) findViewById(R.id.text_news);
        findViewById(R.id.button_topic).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TopicActivity.class);
                startActivity(intent);
            }
        });
        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceHelper.requestNews(MainActivity.this);
            }
        });
        findViewById(R.id.button_background_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceHelper.enableBackgroundUpdate(MainActivity.this);
                Toast.makeText(MainActivity.this, "Фоновое обновление включено", Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.button_no_background_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serviceHelper.disableBackgroundUpdate(MainActivity.this);
                Toast.makeText(MainActivity.this, "Фоновое обновление выключено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((Button) findViewById(R.id.button_topic)).setText(storage.loadCurrentTopic());
        serviceHelper.requestNews(this);
    }

    @Override
    protected void onDestroy() {
        serviceHelper.setNewsReceiverCallback(null);
        super.onDestroy();
    }

    @Override
    public void onNewsLoaded(int resultCode) {
        if (resultCode != NewsIntentService.RESULT_CODE_OK) {
            Toast.makeText(this, "Не удалось загрузить новости", Toast.LENGTH_SHORT).show();
        }
        News news = storage.getLastSavedNews();
        if (news != null) {
            newsText.setText(news.getTitle() + '\n' + DATE_FORMAT.format(news.getDate()) + '\n' + news.getBody());
        }
    }
}
