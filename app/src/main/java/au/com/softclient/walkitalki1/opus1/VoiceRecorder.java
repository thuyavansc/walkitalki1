
package au.com.softclient.walkitalki1.opus1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.theeasiestway.opus.Constants;
import com.theeasiestway.opus.Opus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import com.theeasiestway.opus.Opus.Companion;




public class VoiceRecorder {

    private Context context;
    private static final int SAMPLE_RATE = 16000;
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioRecord audioRecord;
    private Opus opus;

    public VoiceRecorder(Context context, Opus opus) {
        this.context = context;
        this.opus = opus;
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
            initializeAudioRecord();
            initializeOpusEncoder();
        } else {
            // Handle the case where the permission is not granted
            // You can request the permission here or handle it in the calling code.
        }
    }

    private void initializeAudioRecord() {
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Handle permission request
            Log.e("VoiceRecorder", "Permission to record audio not granted.");
            return;
        }

        audioRecord = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                SAMPLE_RATE,
                CHANNEL_CONFIG,
                AUDIO_FORMAT,
                BUFFER_SIZE
        );

        if (audioRecord.getState() != AudioRecord.STATE_INITIALIZED) {
            Log.e("VoiceRecorder", "AudioRecord initialization failed.");
        }
    }

    private void initializeOpusEncoder() {
        int result = opus.encoderInit(Constants.SampleRate.Companion._16000(), Constants.Channels.Companion.mono(), Constants.Application.Companion.voip());

//        if (result != Constants.OpusError.OPUS_OK) {
//            Log.e("VoiceRecorder", "Opus encoder initialization failed with error code: " + result);
//            // Handle initialization error, if needed
//        }
    }

    public void startRecording() {
        if (audioRecord == null) {
            initializeAudioRecord();
            initializeOpusEncoder();
        }

        if (audioRecord != null) {
            audioRecord.startRecording();
            byte[] buffer = new byte[BUFFER_SIZE];
            while (true) {
                int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
                if (bytesRead > 0) {
                    byte[] encodedData = opus.encode(buffer, Constants.FrameSize.Companion._120());
                    if (encodedData != null) {
                        saveToFile(encodedData, "encoded_voice.opus");
                        // Send encodedData to server or perform any other actions
                    } else {
                        Log.e("VoiceRecorder", "Opus encoding failed.");
                    }
                }
            }
        } else {
            // Handle the case where the AudioRecord is not properly initialized
            Log.e("VoiceRecorder", "AudioRecord is not initialized");
        }
    }

    public void stopRecording() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
        }
    }

    private void saveToFile(byte[] data, String fileName) {
        try {
            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/VoiceRecorder");
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
        } catch (IOException e) {
            Log.e("VoiceRecorder", "Error saving file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


//public class VoiceRecorder {
//
//    private Context context;
//    private static final int SAMPLE_RATE = 16000;
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioRecord audioRecord;
//    private Opus opus;
//
//    public VoiceRecorder(Context context, Opus opus) {
//        this.context = context;
//        this.opus = opus;
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
//                == PackageManager.PERMISSION_GRANTED) {
//            initializeAudioRecord();
//            initializeOpusEncoder();
//        } else {
//            // Handle the case where the permission is not granted
//            // You can request the permission here or handle it in the calling code.
//        }
//    }
//
//    private void initializeAudioRecord() {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Handle permission request
//            return;
//        }
//
//        audioRecord = new AudioRecord(
//                MediaRecorder.AudioSource.MIC,
//                SAMPLE_RATE,
//                CHANNEL_CONFIG,
//                AUDIO_FORMAT,
//                BUFFER_SIZE
//        );
//    }
//
//    private void initializeOpusEncoder() {
//        int result = opus.encoderInit(Constants.SampleRate.Companion._16000(), Constants.Channels.Companion.mono(), Constants.Application.Companion.voip());
//        Log.e("VoiceRecorder", "Opus encoder initialization failed with error code: " + result);
//
////        if (result != Constants.Application.Companion.audio().getV()) {
////            // Handle initialization error
////        }
//    }
//
//    public void startRecording() {
//        if (audioRecord == null) {
//            initializeAudioRecord();
//            initializeOpusEncoder();
//        }
//
//        if (audioRecord != null) {
//            audioRecord.startRecording();
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while (true) {
//                int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
//                if (bytesRead > 0) {
//                    byte[] encodedData = opus.encode(buffer, Constants.FrameSize.Companion._120());
//                    saveToFile(encodedData, "encoded_voice.opus");
//                    // Send encodedData to server or perform any other actions
//                }
//            }
//        } else {
//            // Handle the case where the AudioRecord is not properly initialized
//            Log.e("VoiceRecorder", "AudioRecord is not initialized");
//        }
//    }
//
//    public void stopRecording() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
//    }
//
//
//    private void saveToFile(byte[] data, String fileName) {
//        try {
//            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/VoiceRecorder");
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            File file = new File(directory, fileName);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(data);
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//}

//public class VoiceRecorder {
//
//    private Context context;
//    private static final int SAMPLE_RATE = 16000;
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioRecord audioRecord;
//    private Opus opus;
//
//    public VoiceRecorder(Context context, Opus opus) {
//        this.context = context;
//        this.opus = opus;
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
//                == PackageManager.PERMISSION_GRANTED) {
//            initializeAudioRecord();
//        } else {
//            // Handle the case where the permission is not granted
//            // You can request the permission here or handle it in the calling code.
//        }
//    }
//
//    private void initializeAudioRecord() {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Handle permission request
//            return;
//        }
//
//        audioRecord = new AudioRecord(
//                MediaRecorder.AudioSource.MIC,
//                SAMPLE_RATE,
//                CHANNEL_CONFIG,
//                AUDIO_FORMAT,
//                BUFFER_SIZE
//        );
//    }
//
//    public void startRecording() {
//        if (audioRecord == null) {
//            initializeAudioRecord();
//        }
//
//        if (audioRecord != null) {
//            audioRecord.startRecording();
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while (true) {
//                int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
//                if (bytesRead > 0) {
//                    opus en = opus.encoderInit()
//                    byte[] encodedData = opus.encode(buffer, Constants.FrameSize.Companion._120());
//                    saveToFile(encodedData, "encoded_voice.opus");
//                    // Send encodedData to server or perform any other actions
//                }
//            }
//        } else {
//            // Handle the case where the AudioRecord is not properly initialized
//            Log.e("VoiceRecorder", "AudioRecord is not initialized");
//        }
//    }
//
//    public void stopRecording() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
//    }
//
//    private void saveToFile(byte[] data, String fileName) {
//        try {
//            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/VoiceRecorder");
//            if (!directory.exists()) {
//                directory.mkdirs();
//            }
//
//            File file = new File(directory, fileName);
//            FileOutputStream fos = new FileOutputStream(file);
//            fos.write(data);
//            fos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}

//public class VoiceRecorder {
//
//    private Context context;
//    private static final int SAMPLE_RATE = 16000;
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioRecord audioRecord;
//    private Opus opus;
//
//    public VoiceRecorder(Context context, Opus opus) {
//        this.context = context;
//        this.opus = opus;
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
//                == PackageManager.PERMISSION_GRANTED) {
//            initializeAudioRecord();
//        } else {
//            // Handle the case where the permission is not granted
//            // You can request the permission here or handle it in the calling code.
//        }
//    }
//
//    private void initializeAudioRecord() {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        audioRecord = new AudioRecord(
//                MediaRecorder.AudioSource.MIC,
//                SAMPLE_RATE,
//                CHANNEL_CONFIG,
//                AUDIO_FORMAT,
//                BUFFER_SIZE
//        );
//    }
//
//    public void startRecording() {
//        if (audioRecord == null) {
//            initializeAudioRecord();
//        }
//
//        if (audioRecord != null) {
//            audioRecord.startRecording();
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while (true) {
//                int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
//                if (bytesRead > 0) {
//                    // Process the recorded data using opus.encode
//                    // byte[] encodedData = opus.encode(buffer, Constants.FrameSize._120());
//                    byte[] encodedData = opus.encode(buffer, Constants.FrameSize.Companion._120());
//                    // Send encodedData to server or perform any other actions
//                }
//            }
//        } else {
//            // Handle the case where the AudioRecord is not properly initialized
//            Log.e("VoiceRecorder", "AudioRecord is not initialized");
//        }
//    }
//
//    public void stopRecording() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
//    }
//}



//package au.com.softclient.walkitalki1.opus1;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//
//import com.theeasiestway.opus.Constants;
//import com.theeasiestway.opus.Opus;
//
//public class VoiceRecorder {
//
//    private Context context;
//    private static final int SAMPLE_RATE = 16000;
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioRecord audioRecord;
//    private Opus opus;
//
//    public VoiceRecorder(Context context, Opus opus) {
//        this.context = context;
//        this.opus = opus;
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
//                == PackageManager.PERMISSION_GRANTED) {
//            initializeAudioRecord();
//        } else {
//            // Handle the case where the permission is not granted
//            // You can request the permission here or handle it in the calling code.
//        }
//    }
//
//    private void initializeAudioRecord() {
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        audioRecord = new AudioRecord(
//                MediaRecorder.AudioSource.MIC,
//                SAMPLE_RATE,
//                CHANNEL_CONFIG,
//                AUDIO_FORMAT,
//                BUFFER_SIZE
//        );
//    }
//
//    public void startRecording() {
//        if (audioRecord != null) {
//            audioRecord.startRecording();
//            byte[] buffer = new byte[BUFFER_SIZE];
//            while (true) {
//                int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
//                if (bytesRead > 0) {
//                    // Process the recorded data using opus.encode
//                    // byte[] encodedData = opus.encode(buffer, Constants.FrameSize._120());
//                    // Send encodedData to server or perform any other actions
//                }
//            }
//        } else {
//            // Handle the case where the AudioRecord is not properly initialized
//            Log.e("VoiceRecorder", "AudioRecord is not initialized");
//        }
//    }
//
//    public void stopRecording() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//        }
//    }
//}



//package au.com.softclient.walkitalki1;
//
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.util.Log;
//
//import androidx.core.app.ActivityCompat;
//
//import com.theeasiestway.opus.Constants;
//import com.theeasiestway.opus.Opus;
//
//public class VoiceRecorder {
//    private static final int SAMPLE_RATE = 16000; // Adjust as needed
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioRecord audioRecord;
//    private Opus opus;
//
//    public VoiceRecorder(Context context, Opus opus) {
//        this.opus = opus;
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.RECORD_AUDIO)
//                != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Handle the case where the permission is not granted
//        }
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE);
//    }
//
//    public VoiceRecorder(MainActivity3 context, com.theeasiestway.opus.Opus opus) {
//    }
//
//    public void startRecording() {
//        audioRecord.startRecording();
//        byte[] buffer = new byte[BUFFER_SIZE];
//        while (true) {
//            int bytesRead = audioRecord.read(buffer, 0, BUFFER_SIZE);
//            if (bytesRead > 0) {
//                byte[] encodedData = opus.encode(buffer, Constants.FrameSize.Companion._120());
//                // Send encodedData to server or perform any other actions
//            }
//        }
//    }
//
//    public void stopRecording() {
//        audioRecord.stop();
//        audioRecord.release();
//    }
//}