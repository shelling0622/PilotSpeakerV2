package lbproject.hcilab.yonsei.pilotspeakerv2;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "1111";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // actionbar Customizing
        ActionBar bar = getSupportActionBar();
        bar.setDisplayShowCustomEnabled(true);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayHomeAsUpEnabled(false);
        bar.setElevation(0);
        bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        View customView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.bar_main, null);
        bar.setCustomView(customView);
        android.support.v7.widget.Toolbar parent = (android.support.v7.widget.Toolbar)customView.getParent();
        parent.setContentInsetsAbsolute(0,0);
        parent.setContentInsetsRelative(0, 0);

        //call View
        Button btnStart = findViewById(R.id.btn_main_start);

        btnStart.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                // firbase Subscription start
                    // subscribe topics
                FirebaseMessaging.getInstance().subscribeToTopic("pilot");
                Toast.makeText(MainActivity.this, "subscribed", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, RecordDiary.class);
                startActivity(intent);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_LOW));
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d(TAG, "Key: " + key + " Value: " + value);
            }
        }
        // [END handle_data_extras]

    }

}
