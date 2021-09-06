package com.example.callrecording;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "CallRecordTest";
    private final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean isPermissionToRecordAccepted = false;
    private MediaRecorder mMediaRecorder;
    private final String[] permissions = {Manifest.permission.CAPTURE_AUDIO_OUTPUT};

    private Button mStartRecordButton;
    private Button mStopRecordButton;
    private boolean isRecordingStarted;
    private final String FILE_FORMAT = ".3gp";

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_AUDIO_PERMISSION:
                isPermissionToRecordAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!isPermissionToRecordAccepted) finish();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        mStartRecordButton = (Button) findViewById(R.id.start_record_button);
        mStartRecordButton.setOnClickListener(this);
        mStopRecordButton = (Button) findViewById(R.id.stop_record_button);
        mStopRecordButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.start_record_button:
                startRecording();
                break;
            case R.id.stop_record_button:
                stopRecording();
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRecording();
    }

    private void startRecording() {
        if (!isRecordingStarted) {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_DOWNLINK);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mMediaRecorder.setOutputFile(getFilePath());
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            try {
                mMediaRecorder.prepare();
            } catch (IOException e) {
                Log.e(TAG, "prepare() failed");
            }
            mMediaRecorder.start();
            isRecordingStarted = true;
            Log.i(TAG, "startRecording: Started");
            Toast.makeText(this, "Call recording started", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFilePath() {
        String fileName;
        fileName = getExternalCacheDir().getAbsolutePath();
        fileName += "/audiorecordtest" + FILE_FORMAT;
        return fileName;
    }

    private void stopRecording() {
        if (isRecordingStarted) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            isRecordingStarted = false;
            Log.i(TAG, "stopRecording: Stopped");
            Toast.makeText(this, "Call recording stopped", Toast.LENGTH_SHORT).show();
        }
    }

}