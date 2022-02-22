package com.example.flutter_recording;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;

import androidx.annotation.RequiresApi;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MediaRecorderHelper {
    MediaRecorder mediaRecorder ;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    private Activity activity;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    MediaRecorderHelper(Activity activity) {
        this.activity = activity;
     mediaRecorder = new MediaRecorder();
    }

    public String stopScreenRecord() {
        mediaRecorder.stop();
        mediaRecorder.reset();
        mediaRecorder.release();
        return "Success";
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public String startScreenRecord() {

        try {
            Log.v("TAG", "startRecording:");
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            @SuppressLint("SimpleDateFormat") String videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    + "/ScreenRecorder_" + new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss")
                    .format(new Date()) +
                    ".mp4";
            mediaRecorder.setOutputFile(videoUri);
            mediaRecorder.setVideoSize(720, 1280);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncodingBitRate(3000000);
            mediaRecorder.setVideoFrameRate(30);
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATION.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
            return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
    }

}
