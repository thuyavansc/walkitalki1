//package au.com.softclient.signalrreceiver1;
//
//import android.Manifest;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.os.Build;
//
//import androidx.core.content.ContextCompat;
//
//import java.io.ByteArrayOutputStream;
//import java.nio.ByteBuffer;
//import java.nio.ByteOrder;
//import java.util.Arrays;
//
//public class AACEncoder {
//    private AudioRecord audioRecord;
//    private int bufferSize;
//
//    public AACEncoder(Context context) {
//        int sampleRate = 44100;  // Adjust based on your requirements
//        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//
////        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
////        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);
//        // Check if RECORD_AUDIO permission is granted
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                // Handle the case where permission is not granted
//                // You might want to request permission here or handle it in your UI
//                return;
//            }
//        }
//
//        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);
//    }
//
//    public byte[] encode() {
//        audioRecord.startRecording();
//
//        byte[] buffer = new byte[bufferSize];
//        int bytesRead = audioRecord.read(buffer, 0, bufferSize);
//
//        if (bytesRead > 0) {
//            // In a real-world scenario, you'd use a proper AAC encoding library here.
//            // This is a placeholder for simplicity, and actual encoding is much more complex.
//            return Arrays.copyOf(buffer, bytesRead);
//        } else {
//            return null;
//        }
//    }
//
//    public void release() {
//        audioRecord.stop();
//        audioRecord.release();
//    }
//}


package au.com.softclient.walkitalki1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Build;
import android.Manifest;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.Arrays;

/*
public class AACEncoder {
    private AudioRecord audioRecord;
    private int bufferSize;

    public AACEncoder(Context context) {
        int sampleRate = 44100;  // Adjust based on your requirements
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        // Check if RECORD_AUDIO permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Handle the case where permission is not granted
                // You might want to request permission here or handle it in your UI
                return;
            }
        }

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);
    }

    public byte[] encode() {
        // Check if audioRecord is initialized
        if (audioRecord == null) {
            return null;
        }

        audioRecord.startRecording();

        byte[] buffer = new byte[bufferSize];
        int bytesRead = audioRecord.read(buffer, 0, bufferSize);

        if (bytesRead > 0) {
            // In a real-world scenario, you'd use a proper AAC encoding library here.
            // This is a placeholder for simplicity, and actual encoding is much more complex.
            return Arrays.copyOf(buffer, bytesRead);
        } else {
            return null;
        }
    }

    public void release() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null; // Set to null after releasing
        }
    }
}
*/


public class AACEncoder {
    private static final String TAG = "AACEncoder";
    private AudioRecord audioRecord;
    private int bufferSize;

    public AACEncoder(Context context) {
        int sampleRate = 44100;  // Adjust based on your requirements
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        // Check if RECORD_AUDIO permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Handle the case where permission is not granted
                // You might want to request permission here or handle it in your UI
                Log.e(TAG, "RECORD_AUDIO permission not granted");
                return;
            }
        }

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);

        Log.d(TAG, "AACEncoder initialized");
    }

    public byte[] encode() {
        // Check if audioRecord is initialized
        if (audioRecord == null) {
            Log.e(TAG, "AudioRecord is not initialized");
            return null;
        }

        audioRecord.startRecording();

        byte[] buffer = new byte[bufferSize];
        int bytesRead = audioRecord.read(buffer, 0, bufferSize);

        if (bytesRead > 0) {
            // In a real-world scenario, you'd use a proper AAC encoding library here.
            // This is a placeholder for simplicity, and actual encoding is much more complex.
            Log.d(TAG, "Audio data encoded. Length: " + bytesRead);
            return Arrays.copyOf(buffer, bytesRead);
        } else {
            Log.w(TAG, "No audio data read");
            return null;
        }
    }

    public void release() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null; // Set to null after releasing
            Log.d(TAG, "AACEncoder-release:AACEncoder released");
        } else {
            Log.w(TAG, "AudioRecord is null");
        }
    }
}

/*
public class AACEncoder {
    private static final String TAG = "AACEncoder";
    private AudioRecord audioRecord;
    private int bufferSize;
    private FDK_AAC aacEncoder;

    public AACEncoder(Context context) {
        int sampleRate = 44100;  // Adjust based on your requirements
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        // Check if RECORD_AUDIO permission is granted
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                // Handle the case where permission is not granted
                // You might want to request permission here or handle it in your UI
                Log.e(TAG, "RECORD_AUDIO permission not granted");
                return;
            }
        }

        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);

        try {
            aacEncoder = new FDK_AAC();
            aacEncoder.initEncoder(sampleRate, channelConfig);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize FDK-AAC encoder", e);
        }

        Log.d(TAG, "AACEncoder initialized");
    }

    public byte[] encode() {
        // Check if audioRecord and aacEncoder are initialized
        if (audioRecord == null || aacEncoder == null) {
            Log.e(TAG, "AudioRecord or AACEncoder is not initialized");
            return null;
        }

        audioRecord.startRecording();

        byte[] buffer = new byte[bufferSize];
        int bytesRead = audioRecord.read(buffer, 0, bufferSize);

        if (bytesRead > 0) {
            // Encode audio data using FDK-AAC
            byte[] encodedData = aacEncoder.encode(buffer, bytesRead);
            Log.d(TAG, "Audio data encoded. Length: " + encodedData.length);
            return encodedData;
        } else {
            Log.w(TAG, "No audio data read");
            return null;
        }
    }

    public void release() {
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null; // Set to null after releasing
            Log.d(TAG, "AACEncoder-release:AACEncoder released");
        }

        if (aacEncoder != null) {
            aacEncoder.close();
            aacEncoder = null; // Set to null after releasing
            Log.d(TAG, "AACEncoder-release:FDK-AAC encoder released");
        }
    }
}

 */
