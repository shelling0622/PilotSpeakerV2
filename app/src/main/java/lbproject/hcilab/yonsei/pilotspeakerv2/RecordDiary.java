package lbproject.hcilab.yonsei.pilotspeakerv2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordDiary extends AppCompatActivity {

    //view
    ImageView imgAsk;
    LinearLayout layoutOn;
    ImageView imgOn;
    Button btnFinish;


    private String filePath;
    private String fileName;

    private MediaPlayer mediaPlayer;
    private MediaRecorder mediaRecorder;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 뒤로 가기 막기
        Toast.makeText(this, "기록 진행중입니다.", Toast.LENGTH_LONG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_diary);

        //call view
        imgAsk = (ImageView)findViewById(R.id.img_record_diary_ask);
        layoutOn = (LinearLayout)findViewById(R.id.layout_record_diary_on);
        imgOn = (ImageView)findViewById(R.id.img_record_diary_on);
        btnFinish = (Button)findViewById(R.id.btn_record_diary_finish);


        try {
            // 사전 준비된 mp3 파일 재생 : 오늘은 무슨 일들이 있었나요?
            // Amazon Polly로 녹음
            playAudio(R.raw.message1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            public void run() {

                imgAsk.setVisibility(View.INVISIBLE);
                layoutOn.setVisibility(View.VISIBLE);

                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
                imgOn.setAnimation(animation);

                if ((ActivityCompat.checkSelfPermission(RecordDiary.this, Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)) {
                    recordOn();

                }

            }
        }, 2500);

        btnFinish.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                recordOff();
                Intent intent = new Intent(RecordDiary.this, AskDiaryKeyword.class);
                startActivity(intent);
            }
        });

    }

    public void setFileNameAndPath() {

        String filePathName = getString(R.string.filePathName);

        SimpleDateFormat timestampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        fileName = "diary" + timestampFormat.format(new Date()).toString() + ".mp4";
        filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + filePathName + "Diary/";

        File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public void recordOn() {

        Toast.makeText(RecordDiary.this, "Record",Toast.LENGTH_LONG).show();

        setFileNameAndPath();
        if (mediaRecorder != null) {
            mediaRecorder.release();
        }

        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setOutputFile(filePath + fileName);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);

        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (Exception e) {
            Log.e("Audio Recorder", "Exception", e);
        }

    }

    public void recordOff() {
        if (mediaRecorder == null) {
            return;
        }

        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaRecorder != null) {
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaRecorder = new MediaRecorder();
    }

    private void playAudio(int url) throws Exception {
        killMediaPlayer();

        mediaPlayer = MediaPlayer.create(RecordDiary.this, url);
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        killMediaPlayer();
    }

    private void killMediaPlayer() {
        if (mediaPlayer != null) {
            try {
                mediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                recordOn();
            } else {
                Toast.makeText(RecordDiary.this, "앱을 종료시켰다 다시 접속해주세요.", Toast.LENGTH_LONG).show();
                //User denied Permission.
            }
        }
    }

}
