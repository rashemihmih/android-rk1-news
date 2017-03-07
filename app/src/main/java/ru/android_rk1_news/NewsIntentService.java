package ru.android_rk1_news;

import android.app.IntentService;
import android.content.Intent;
import android.os.ResultReceiver;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;

public class NewsIntentService extends IntentService {
    public static final int RESULT_CODE_OK = 0;
    public static final int RESULT_CODE_ERROR = 1;
    public static final String EXTRA_RECEIVER = "receiver";

    public NewsIntentService() {
        super(NewsIntentService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        ResultReceiver resultReceiver = intent.getParcelableExtra(EXTRA_RECEIVER);
        Storage storage = Storage.getInstance(this);
        String topic = storage.loadCurrentTopic();
        try {
            News news = new NewsLoader().loadNews(topic);
            storage.saveNews(news);
            resultReceiver.send(RESULT_CODE_OK, null);
        } catch (IOException e) {
            resultReceiver.send(RESULT_CODE_ERROR, null);
        }
    }
}
