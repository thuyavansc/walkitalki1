package au.com.softclient.walkitalki1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.theeasiestway.opus.Opus;

import android.Manifest;

import au.com.softclient.walkitalki1.opus1.VoicePlayer;
import au.com.softclient.walkitalki1.opus1.VoiceRecorder;


import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.theeasiestway.opus.Constants;
import com.theeasiestway.opus.Opus;
import au.com.softclient.walkitalki1.opus1.ControllerAudio;

public class MainActivity3 extends AppCompatActivity {

    private final String TAG = "OpusActivity";
    private final String audioPermission = android.Manifest.permission.RECORD_AUDIO;
    private final String readPermission = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String writePermission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private SeekBar vSampleRateSeek;
    private TextView vSampleRate;
    private Button vPlay;
    private Button vStop;
    private RadioButton vBytes;
    private RadioButton vShorts;
    private RadioButton vMono;
    private RadioButton vStereo;
    private CheckBox vConvert;

    private Opus codec = new Opus();
    private Constants.Application APPLICATION = Constants.Application.Companion.audio();
    private int CHUNK_SIZE = 0;
    private Constants.SampleRate SAMPLE_RATE;
    private Constants.Channels CHANNELS;
    private Constants.FrameSize DEF_FRAME_SIZE;
    private Constants.FrameSize FRAME_SIZE_SHORT;
    private Constants.FrameSize FRAME_SIZE_BYTE;

    private boolean runLoop = false;
    private boolean needToConvert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        vSampleRateSeek = findViewById(R.id.vSampleRateSeek);
        vSampleRate = findViewById(R.id.vSampleRate);

        vPlay = findViewById(R.id.vPlay);
        vStop = findViewById(R.id.vStop);

        vBytes = findViewById(R.id.vHandleBytes);
        vShorts = findViewById(R.id.vHandleShorts);
        vMono = findViewById(R.id.vMono);
        vStereo = findViewById(R.id.vStereo);
        vConvert = findViewById(R.id.vConvert);

        vSampleRateSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SAMPLE_RATE = getSampleRate(progress);
                String labelText = SAMPLE_RATE.toString() + " Hz";
                vSampleRate.setText(labelText);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        vSampleRateSeek.setProgress(0);

        vPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vPlay.setVisibility(View.GONE);
                vStop.setVisibility(View.VISIBLE);
                requestPermissions();
            }
        });

        vStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        vConvert.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                needToConvert = isChecked;
            }
        });
    }

    private void recalculateCodecValues() {
        DEF_FRAME_SIZE = getDefaultFrameSize(SAMPLE_RATE.getV());
        CHANNELS = vMono.isChecked() ? Constants.Channels.Companion.mono() : Constants.Channels.Companion.stereo();
        CHUNK_SIZE = DEF_FRAME_SIZE.getV() * CHANNELS.getV() * 2;
        FRAME_SIZE_SHORT = Constants.FrameSize.Companion.fromValue(CHUNK_SIZE / CHANNELS.getV());
        FRAME_SIZE_BYTE = Constants.FrameSize.Companion.fromValue(CHUNK_SIZE / 2 / CHANNELS.getV());
    }

    private Constants.SampleRate getSampleRate(int v) {
        switch (v) {
            case 0:
                return Constants.SampleRate.Companion._8000();
            case 1:
                return Constants.SampleRate.Companion._12000();
            case 2:
                return Constants.SampleRate.Companion._16000();
            case 3:
                return Constants.SampleRate.Companion._24000();
            case 4:
                return Constants.SampleRate.Companion._48000();
            default:
                throw new IllegalArgumentException();
        }
    }

    private Constants.FrameSize getDefaultFrameSize(int v) {
        switch (v) {
            case 8000:
                return Constants.FrameSize.Companion._160();
            case 12000:
                return Constants.FrameSize.Companion._240();
            case 16000:
                return Constants.FrameSize.Companion._160();
            case 24000:
                return Constants.FrameSize.Companion._240();
            case 48000:
                return Constants.FrameSize.Companion._120();
            default:
                throw new IllegalArgumentException();
        }
    }

    private void stopRecording() {
        vStop.setVisibility(View.GONE);
        vPlay.setVisibility(View.VISIBLE);
        stopLoop();
        ControllerAudio.stopRecord();
        ControllerAudio.stopTrack();
        vSampleRateSeek.setEnabled(true);
        vBytes.setEnabled(true);
        vShorts.setEnabled(true);
        vMono.setEnabled(true);
        vStereo.setEnabled(true);
    }

    private void startLoop() {
        stopLoop();

        vSampleRateSeek.setEnabled(false);
        vBytes.setEnabled(false);
        vShorts.setEnabled(false);
        vMono.setEnabled(false);
        vStereo.setEnabled(false);

        boolean handleShorts = vShorts.isChecked();
        recalculateCodecValues();

        codec.encoderInit(SAMPLE_RATE, CHANNELS, APPLICATION);
        codec.decoderInit(SAMPLE_RATE, CHANNELS);

        ControllerAudio.initRecorder(this,SAMPLE_RATE.getV(), CHUNK_SIZE, CHANNELS.getV() == 1);
        ControllerAudio.initTrack(SAMPLE_RATE.getV(), CHANNELS.getV() == 1);
        ControllerAudio.startRecord();
        runLoop = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runLoop) {
                    if (handleShorts) handleShorts();
                    else handleBytes();
                }
                if (!runLoop) {
                    codec.encoderRelease();
                    codec.decoderRelease();
                }
            }
        }).start();
    }

    private void stopLoop() {
        runLoop = false;
    }

    private void handleShorts() {
        short[] frame = ControllerAudio.getFrameShort();
        if (frame == null) return;
        short[] encoded = codec.encode(frame, FRAME_SIZE_SHORT);
        if (encoded == null) return;
        Log.d(TAG, "encoded: " + frame.length + " shorts of " + (CHANNELS.getV() == 1 ? "MONO" : "STEREO") + " audio into " + encoded.length + " shorts");
        short[] decoded = codec.decode(encoded, FRAME_SIZE_SHORT);
        if (decoded == null) return;
        Log.d(TAG, "decoded: " + decoded.length + " shorts");

        if (needToConvert) {
            byte[] converted = codec.convert(decoded);
            if (converted == null) return;
            Log.d(TAG, "converted: " + decoded.length + " shorts into " + converted.length + " bytes");
            ControllerAudio.write(converted);
        } else ControllerAudio.write(decoded);
        Log.d(TAG, "===========================================");
    }

    private void handleBytes() {
        byte[] frame = ControllerAudio.getFrame();
        if (frame == null) return;
        byte[] encoded = codec.encode(frame, FRAME_SIZE_BYTE);
        if (encoded == null) return;
        Log.d(TAG, "encoded: " + frame.length + " bytes of " + (CHANNELS.getV() == 1 ? "MONO" : "STEREO") + " audio into " + encoded.length + " bytes");
        byte[] decoded = codec.decode(encoded, FRAME_SIZE_BYTE);
        if (decoded == null) return;
        Log.d(TAG, "decoded: " + decoded.length + " bytes");

        if (needToConvert) {
            short[] converted = codec.convert(decoded);
            if (converted == null) return;
            Log.d(TAG, "converted: " + decoded.length + " bytes into " + converted.length + " shorts");
            ControllerAudio.write(converted);
        } else ControllerAudio.write(decoded);
        Log.d(TAG, "===========================================");
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) startLoop();
        else if (checkSelfPermission(audioPermission) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(readPermission) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(writePermission) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{audioPermission, readPermission, writePermission}, 123);
        } else startLoop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions[0].equals(audioPermission) &&
                permissions[1].equals(readPermission) &&
                permissions[2].equals(writePermission) &&
                requestCode == 123) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[2] == PackageManager.PERMISSION_GRANTED) startLoop();
            else
                Toast.makeText(this, "App doesn't have enough permissions to continue", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRecording();
    }
}



//public class MainActivity3 extends AppCompatActivity {
//    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123;
//
//    private VoiceRecorder voiceRecorder;
//    private VoicePlayer voicePlayer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);
//
//        Opus opus = new Opus(); // Initialize Opus here
//
//        voiceRecorder = new VoiceRecorder(this, opus);
//        voicePlayer = new VoicePlayer(opus);
//
//        Button btnStartRecording = findViewById(R.id.btnStartRecording);
//        Button btnStopRecording = findViewById(R.id.btnStopRecording);
//        Button btnPlay = findViewById(R.id.btnPlay);
//        Button btnStop = findViewById(R.id.btnStop);
//
//        btnStartRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStartRecordingClick();
//            }
//        });
//
//        btnStopRecording.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStopRecordingClick();
//            }
//        });
//
//        btnPlay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onPlayClick();
//            }
//        });
//
//        btnStop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onStopClick();
//            }
//        });
//    }
//
//    public void onStartRecordingClick() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                voiceRecorder.startRecording();
//            }
//        }).start();
//    }
//
//    public void onStopRecordingClick() {
//        voiceRecorder.stopRecording();
//    }
//
//    public void onPlayClick() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                voicePlayer.startPlaying();
//            }
//        }).start();
//    }
//
//    public void onStopClick() {
//        voicePlayer.stopPlaying();
//    }
//
//    // Handle permission request result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, you can start recording
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message)
//            }
//        }
//    }
//}




//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);
//
////        // Initialize the Opus codec
////        Opus opus = new Opus();
////        opus.encoderInit(16000, 1, 2048); // Adjust parameters based on your requirements
////        opus.decoderInit(16000, 1);
////
////        // Example: Encode and Decode
////        byte[] pcmData = new byte[1024]; // Replace with your PCM audio data
////        byte[] encodedData = opus.encode(pcmData, pcmData.length);
////        byte[] decodedData = opus.decode(encodedData, encodedData.length);
////
////        // Release Opus resources when done
////        opus.encoderRelease();
////        opus.decoderRelease();
//        Opus opus = new Opus();
//
//        // Example: Encode and Decode
//        byte[] pcmData = new byte[1024]; // Replace with your PCM audio data
////        byte[] encodedData = opus.encode(pcmData, Constants.FrameSize._120());
////        byte[] decodedData = opus.decode(encodedData, Constants.FrameSize._120());
//
//        byte[] encodedData = opus.encode(pcmData, FrameSize.Companion._120());
//        byte[] decodedData = opus.decode(encodedData, FrameSize.Companion._120());
//
//        // Release Opus resources when done
//        opus.encoderRelease();
//        opus.decoderRelease();
//    }


//private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1;
//    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 123; // You can use any integer value
//
//    private Opus opus;
//    private VoiceRecorder voiceRecorder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main3);
//
//        opus = new Opus();
//        voiceRecorder = new VoiceRecorder(this, opus);
//
//        Button startButton = findViewById(R.id.startButton);
//        Button stopButton = findViewById(R.id.stopButton);
//
//        startButton.setOnClickListener(v -> startRecording());
//        stopButton.setOnClickListener(v -> stopRecording());
//
//        // Request audio recording permission if not granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.RECORD_AUDIO},
//                    RECORD_AUDIO_PERMISSION_REQUEST_CODE);
//        }
//    }
//
//    private void startRecording() {
//        voiceRecorder.startRecording();
//    }
//
//    private void stopRecording() {
//        voiceRecorder.stopRecording();
//    }
//
//    // Handle permission request result
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, you can start recording
//            } else {
//                // Permission denied, handle accordingly (e.g., show a message)
//            }
//        }
//    }