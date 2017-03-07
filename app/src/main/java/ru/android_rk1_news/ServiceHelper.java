package ru.android_rk1_news;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import ru.mail.weather.lib.Scheduler;

public class ServiceHelper {
    private static final long SCHEDULE_PERIOD = 60000L;
    private static ServiceHelper instance;
    private NewsReceiver newsReceiver;
    private Scheduler scheduler;

    private ServiceHelper() {
        newsReceiver = new NewsReceiver(new Handler());
        scheduler = Scheduler.getInstance();
    }

    public static synchronized ServiceHelper getInstance() {
        if (instance == null) {
            instance = new ServiceHelper();
        }
        return instance;
    }

    public void requestNews(Context context) {
        context.startService(createIntent(context));
    }

    public void setNewsReceiverCallback(Callback callback) {
        newsReceiver.setCallback(callback);
    }

    public void enableBackgroundUpdate(Context context) {
        scheduler.schedule(context, createIntent(context), SCHEDULE_PERIOD);
    }

    public void disableBackgroundUpdate(Context context) {
        scheduler.unschedule(context, createIntent(context));
    }

    private Intent createIntent(Context context) {
        Intent intent = new Intent(context, NewsIntentService.class);
        intent.putExtra(NewsIntentService.EXTRA_RECEIVER, newsReceiver);
        return intent;
    }

    public interface Callback {
        void onNewsLoaded(int resultCode);
    }
}
