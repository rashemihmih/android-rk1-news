package ru.android_rk1_news;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class NewsReceiver extends ResultReceiver {
    private ServiceHelper.Callback callback;

    public NewsReceiver(Handler handler) {
        super(handler);
    }

    public void setCallback(ServiceHelper.Callback callback) {
        this.callback = callback;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (callback != null) {
            callback.onNewsLoaded(resultCode);
        }
    }
}
