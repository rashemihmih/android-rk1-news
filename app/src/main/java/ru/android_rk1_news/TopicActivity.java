package ru.android_rk1_news;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

public class TopicActivity extends AppCompatActivity {
    private Storage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        storage = Storage.getInstance(this);
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_topic);
        for (final String topic : Topics.ALL_TOPICS) {
            Button button = new Button(this);
            button.setText(topic);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    storage.saveCurrentTopic(topic);
                    finish();
                }
            });
            layout.addView(button);
        }
    }
}
