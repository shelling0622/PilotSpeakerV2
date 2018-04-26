package lbproject.hcilab.yonsei.pilotspeakerv2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Test extends AppCompatActivity {

    String msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        TextView txtResult = findViewById(R.id.txt_result);

        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");

        txtResult.setText(msg);

    }
}
