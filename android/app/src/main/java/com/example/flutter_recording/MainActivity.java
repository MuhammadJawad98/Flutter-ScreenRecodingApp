package com.example.flutter_recording;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
    private static final String CHANNEL = "flutter.native/helper";
    private static final int REQUEST_CODE = 100;
    MediaRecorder mediaRecorder;

    private static final int REQUEST_PERMISSION = 10;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;
    private int screenDensity;
    private MediaProjectionManager mediaProjectionManager;
    private MediaProjection mediaProjection;
    private VirtualDisplay virtualDisplay;
    private MediaProjectionCallback mediaProjectionCallback;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 270);
        ORIENTATION.append(Surface.ROTATION_270, 180);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenDensity = metrics.densityDpi;
        mediaRecorder = new MediaRecorder();
        mediaProjectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), CHANNEL).setMethodCallHandler((call, result) -> {
            if (call.method.equals("startScreenRecord")) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        Snackbar.make(findViewById(R.id.content), "Permissions", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ENABLE", v1 -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                                }, REQUEST_PERMISSION)).show();


                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                        }, REQUEST_PERMISSION);
                    }
                } else {
                    String output = onShareScreen(true);
                    result.success(output);
                }
            }
            if (call.method.equals("stopScreenRecord")) {

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) +
                        ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            || ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.RECORD_AUDIO)) {
                        Snackbar.make(findViewById(R.id.content), "Permissions", Snackbar.LENGTH_INDEFINITE)
                                .setAction("ENABLE", v1 -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                                }, REQUEST_PERMISSION)).show();


                    } else {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO
                        }, REQUEST_PERMISSION);
                    }
                } else {
                    String output = onShareScreen(false);
                    result.success(output);
                }
//                    stopService(new Intent(this, MediaProjectionService.class));
//                    String stopResult = stopScreenRecord();
//                    result.success(stopResult);


            }
        });
    }

//    private String stopScreenRecord() {
//        try {
//            mediaRecorder.stop();
//            mediaRecorder.reset();
//            mediaRecorder.release();
//            return "Success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.d("TAG", ">>>> falied" + e.toString());
//            return "Failed";
//        }
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    private String startScreenRecord() {
//
//        try {
//            Log.v("TAG", "startRecording:");
//            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
//            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////            String videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/MizdahRecord_" + getCurSysDate() + ".mp4";
//            @SuppressLint("SimpleDateFormat") String videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    + "/ScreenRecorder_" + new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss")
//                    .format(new Date()) +
//                    ".mp4";
//            mediaRecorder.setOutputFile(videoUri);
//            mediaRecorder.setVideoSize(720, 1280);
//            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
//            mediaRecorder.setVideoEncodingBitRate(3000000);
//            mediaRecorder.setVideoFrameRate(30);
//            int rotation = getWindowManager().getDefaultDisplay().getRotation();
//            int orientation = ORIENTATION.get(rotation + 90);
//            mediaRecorder.setOrientationHint(orientation);
//            mediaRecorder.prepare();
//            return "Success";
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Failed";
//        }
////        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
////        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
////        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
////        @SuppressLint("SimpleDateFormat") String videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
////                + "/ScreenRecorder_" + new SimpleDateFormat("dd-MM-yyyy-hh_mm_ss")
////                .format(new Date()) +
////                ".mp4";
////        mediaRecorder.setOutputFile(videoUri);
//////Start......................................VideoSettings
////        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
////        mediaRecorder.setVideoSize(720, 1280);
////        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
////        mediaRecorder.setVideoEncodingBitRate(512 * 1000);
////        mediaRecorder.setVideoFrameRate(60);
//////End......................................VideoSettings
////        try {
////            mediaRecorder.prepare();
////            mediaRecorder.start();
////
////            return "Success";
////        } catch (IOException e) {
////            e.printStackTrace();
////        }
////        return "Fail";
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private String onShareScreen(boolean isChecked) {
        if (isChecked) {
            startService(new Intent(getApplicationContext(), MediaProjectionService.class));
            initRecording();
            shareScreen();
            return "Success";
        } else {
            stopService(new Intent(getApplicationContext(), MediaProjectionService.class));
            mediaRecorder.stop();
            mediaRecorder.reset();
            stopScreenSharing();
            return "Failed";

        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getCurSysDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
    }

    private void initRecording() {
        try {
            Log.v("TAG", "startRecording:");
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            String videoUri = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/ScreenRecord_" + getCurSysDate() + ".mp4";
            mediaRecorder.setOutputFile(videoUri);
            mediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setVideoEncodingBitRate(3000000);
            mediaRecorder.setVideoFrameRate(30);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATION.get(rotation + 90);
            mediaRecorder.setOrientationHint(orientation);
            mediaRecorder.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //    // Create launcher variable inside onAttach or onCreate or global
//    ActivityResultLauncher<Intent> launchSomeActivity = registerForActivityResult(
//            new ActivityResultContracts.StartActivityForResult(),
//            new ActivityResultCallback<Instrumentation.ActivityResult>() {
//                @Override
//                public void onActivityResult(Instrumentation.ActivityResult result) {
//                    System.out.println(">>>>>>" + result.getData());
//                    if (result.getResultCode() != RESULT_OK) {
//                        Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
//                        toggleButton.setChecked(false);
//                        return;
//                    }
//                    mediaProjectionCallback = new MediaProjectionCallback();
//                    mediaProjection = mediaProjectionManager.getMediaProjection(result.getResultCode(), result.getData());
//                    mediaProjection.registerCallback(mediaProjectionCallback, null);
//                    virtualDisplay = createVirtualDispaly();
//                    mediaRecorder.start();
//                }
//            });
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode != REQUEST_CODE) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
//        toggleButton.setChecked(false);
            return;
        }
        mediaProjectionCallback = new MediaProjectionCallback();
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
        mediaProjection.registerCallback(mediaProjectionCallback, null);
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void shareScreen() {
        if (mediaProjection == null) {

//            launchSomeActivity.launch(mediaProjectionManager.createScreenCaptureIntent());
            startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        virtualDisplay = createVirtualDisplay();
        mediaRecorder.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private VirtualDisplay createVirtualDisplay() {
        return mediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mediaRecorder.getSurface(), null, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void stopScreenSharing() {
        if (virtualDisplay == null) return;
        virtualDisplay.release();
        destroyMediaProjection();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void destroyMediaProjection() {
        if (mediaProjection != null) {
            mediaProjection.unregisterCallback(mediaProjectionCallback);
            mediaProjection.stop();
            mediaProjection = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {

            mediaRecorder.stop();
            mediaRecorder.reset();
            mediaProjection = null;
            stopScreenSharing();
            super.onStop();
        }
    }

}

