//package au.com.softclient.walkitalki1;
//
////import com.jcraft.jopus.OpusEncoder;
////import au.com.softclient.signalrreceiver1.OpusClass;
////import com.theeasiestway.opus;
////import com.theeasiestway.opus.Opus;
////import tomp2p.opuswrapper.Opus;
//import android.Manifest;
//import android.content.pm.PackageManager;
//
//import android.content.Context;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.os.Build;
//import android.util.Log;
//
//import java.util.Arrays;
//
//
///*
//import java.util.Arrays;
//
//public class OpusEncoder {
//    private OpusEncoder opusEncoder;
//
//
//    public OpusEncoder() {
//        opusEncoder = new OpusEncoder(16000, 1, OpusEncoder.OPUS_APPLICATION_VOIP);
//    }
//
//    public byte[] encode(byte[] pcmData, int pcmDataLength) {
//        int maxEncodedLength = pcmData.length / 2; // Rough estimation
//        byte[] encodedData = new byte[maxEncodedLength];
//
//        int encodedLength = opusEncoder.encode(pcmData, 0, pcmDataLength / 2, encodedData, 0, maxEncodedLength);
//        return Arrays.copyOf(encodedData, encodedLength);
//    }
//}
//
// */
//
////
////package au.com.softclient.signalrreceiver1;
////
////import com.jcraft.jopus.OpusEncoder;
////
////import java.util.Arrays;
////
////public class OpusEncoder {
////    private OpusEncoder opusEncoder;
////
////    public OpusEncoder() {
////        opusEncoder = new OpusEncoder(16000, 1, OpusEncoder.OPUS_APPLICATION_VOIP);
////    }
////
////    public byte[] encode(byte[] pcmData, int pcmDataLength) {
////        int maxEncodedLength = pcmDataLength * 2; // Rough estimation
////        byte[] encodedData = new byte[maxEncodedLength];
////
////        int encodedLength = opusEncoder.encode(pcmData, 0, pcmDataLength, encodedData, 0, maxEncodedLength);
////        return Arrays.copyOf(encodedData, encodedLength);
////    }
////}
//
//
////package au.com.softclient.signalrreceiver1;
////
////import com.jcraft.jopus.OpusEncoder;
////
////import java.util.Arrays;
////
////public class OpusEncoder {
////    private OpusEncoder opusEncoder;
////
////    public OpusEncoder() {
////        opusEncoder = new OpusEncoder(16000, 1, OpusEncoder.OPUS_APPLICATION_VOIP);
////    }
////
////    public byte[] encode(byte[] pcmData, int pcmDataLength) {
////        int maxEncodedLength = pcmDataLength * 2; // Rough estimation
////        byte[] encodedData = new byte[maxEncodedLength];
////
////        int encodedLength = opusEncoder.encode(pcmData, 0, pcmDataLength, encodedData, 0, maxEncodedLength);
////        return Arrays.copyOf(encodedData, encodedLength);
////    }
////}
////
////package au.com.softclient.signalrreceiver1;
////
////import java.util.Arrays;
////
////import com.jcraft.opus.OpusEncoder;
////
////public class OpusEncoder {
////    private OpusEncoder encoder;
////
////    public OpusEncoder() {
////        try {
////            encoder = new OpusEncoder(48000, 20, 1); // Sample rate, frame size, channels
////        } catch (OpusException e) {
////            e.printStackTrace();
////        }
////    }
////
////    public byte[] encode(byte[] pcmData, int pcmDataLength) {
////        int encodedLength = encoder.encode(pcmData, 0, pcmDataLength, new byte[pcmDataLength / 2], 0);
////        return Arrays.copyOf(new byte[encodedLength], encodedLength);
////    }
////}
//
////package au.com.softclient.signalrreceiver1;
////
////import java.io.ByteArrayOutputStream;
////import java.nio.ByteBuffer;
////import java.nio.ByteOrder;
////
////public class OpusEncoder {
////    private static final int SAMPLE_RATE = 16000;
////    private static final int CHANNELS = 1; // Mono
////    private static final int FRAME_SIZE = 960; // 60ms frame size
////
////    private OpusEncoder() {
////        // Private constructor to prevent instantiation
////    }
////
////    public static byte[] encode(byte[] pcmData, int pcmDataLength) {
////        if (pcmDataLength % 2 != 0) {
////            throw new IllegalArgumentException("PCM data length must be even");
////        }
////
////        short[] pcmSamples = bytesToShortArray(pcmData);
////        short[] encodedData = new short[pcmDataLength / 2]; // Rough estimation
////
////        JavaOpus.opusEncode(pcmSamples, pcmDataLength / 2, encodedData);
////
////        return shortArrayToByteArray(encodedData);
////    }
////
////    private static short[] bytesToShortArray(byte[] bytes) {
////        short[] shorts = new short[bytes.length / 2];
////        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
////        return shorts;
////    }
////
////    private static byte[] shortArrayToByteArray(short[] shorts) {
////        byte[] bytes = new byte[shorts.length * 2];
////        ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().put(shorts);
////        return bytes;
////    }
////}
//
////
////package au.com.softclient.signalrreceiver1;
////
////import android.util.Log;
////
////import java.io.ByteArrayOutputStream;
////import java.io.IOException;
////import java.nio.ByteBuffer;
////import java.nio.ByteOrder;
////
////import aocate.utils.RawAudioDataSource;
////
////public class OpusEncoder {
////    private static final String TAG = "OpusEncoder";
////
////    public static byte[] encode(byte[] pcmData, int sampleRate, int channels) {
////        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
////
////        try {
////            RawAudioDataSource audioDataSource = new RawAudioDataSource(
////                    new ByteArrayInputStream(pcmData),
////                    sampleRate,
////                    channels,
////                    RawAudioDataSource.SAMPLE_SIZE_16_BIT,
////                    false
////            );
////
////            OpusWriter opusWriter = new OpusWriter.Builder()
////                    .setBitrate(16000)
////                    .setSampleRate(sampleRate)
////                    .setChannels(channels)
////                    .setApplication(OpusConstants.APPLICATION_VOIP)
////                    .setOutput(outputStream)
////                    .build();
////
////            opusWriter.prepare();
////
////            ByteBuffer buffer = ByteBuffer.allocateDirect(2048);
////            buffer.order(ByteOrder.nativeOrder());
////
////            int bytesRead;
////            while ((bytesRead = audioDataSource.read(buffer)) != -1) {
////                buffer.limit(bytesRead);
////                buffer.position(0);
////                opusWriter.write(buffer);
////                buffer.clear();
////            }
////
////            opusWriter.release();
////            audioDataSource.close();
////        } catch (IOException e) {
////            Log.e(TAG, "Error encoding audio", e);
////        }
////
////        return outputStream.toByteArray();
////    }
////}
//
//
//
//
///*
//public class OpusEncoder {
//    private static final String TAG = "OpusEncoder";
//    private AudioRecord audioRecord;
//    private int bufferSize;
//    private YourOpusEncoderClass opusEncoder; // Replace YourOpusEncoderClass with your actual Opus encoder class
//
//    public OpusEncoder(Context context) {
//        int sampleRate = 44100;  // Adjust based on your requirements
//        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//
//        // Check if RECORD_AUDIO permission is granted
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                // Handle the case where permission is not granted
//                // You might want to request permission here or handle it in your UI
//                Log.e(TAG, "RECORD_AUDIO permission not granted");
//                return;
//            }
//        }
//
//        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);
//
//        try {
//            opusEncoder = new YourOpusEncoderClass(); // Replace YourOpusEncoderClass with your actual Opus encoder class
//            opusEncoder.initEncoder(sampleRate, channelConfig);
//        } catch (IOException e) {
//            Log.e(TAG, "Failed to initialize Opus encoder", e);
//        }
//
//        Log.d(TAG, "OpusEncoder initialized");
//    }
//
//    public byte[] encode() {
//        // Check if audioRecord and opusEncoder are initialized
//        if (audioRecord == null || opusEncoder == null) {
//            Log.e(TAG, "AudioRecord or OpusEncoder is not initialized");
//            return null;
//        }
//
//        audioRecord.startRecording();
//
//        byte[] buffer = new byte[bufferSize];
//        int bytesRead = audioRecord.read(buffer, 0, bufferSize);
//
//        if (bytesRead > 0) {
//            // Encode audio data using Opus
//            byte[] encodedData = opusEncoder.encode(buffer, bytesRead);
//            Log.d(TAG, "Audio data encoded. Length: " + encodedData.length);
//            return encodedData;
//        } else {
//            Log.w(TAG, "No audio data read");
//            return null;
//        }
//    }
//
//    public void release() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//            audioRecord = null; // Set to null after releasing
//            Log.d(TAG, "OpusEncoder-release: AudioRecord released");
//        }
//
//        if (opusEncoder != null) {
//            opusEncoder.close();
//            opusEncoder = null; // Set to null after releasing
//            Log.d(TAG, "OpusEncoder-release: Opus encoder released");
//        }
//    }
//}
//
// */
//
//
//public class OpusEncoder {
//    private static final String TAG = "OpusEncoder";
//    private AudioRecord audioRecord;
//    private int bufferSize;
//    private Opus opusEncoder;
//
//    public OpusEncoder(Context context) {
//        int sampleRate = 44100;  // Adjust based on your requirements
//        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
//        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
//
//        // Check if RECORD_AUDIO permission is granted
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (context.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                // Handle the case where permission is not granted
//                // You might want to request permission here or handle it in your UI
//                Log.e(TAG, "RECORD_AUDIO permission not granted");
//                return;
//            }
//        }
//
//        bufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, bufferSize);
//
//        // Initialize your Opus encoder
//        opusEncoder = Opus.INSTANCE;
//
//        Log.d(TAG, "OpusEncoder initialized");
//    }
//
//    public byte[] encode() {
//        // Check if audioRecord and opusEncoder are initialized
//        if (audioRecord == null || opusEncoder == null) {
//            Log.e(TAG, "AudioRecord or OpusEncoder is not initialized");
//            return null;
//        }
//
//        audioRecord.startRecording();
//
//        byte[] buffer = new byte[bufferSize];
//        int bytesRead = audioRecord.read(buffer, 0, bufferSize);
//
//        if (bytesRead > 0) {
//            // Encode audio data using Opus
//            // Note: Replace the following line with actual Opus encoding method
//            // int encodedLength = opusEncoder.opus_encode(...);
//            int encodedLength = bytesRead;
//
//            Log.d(TAG, "Audio data encoded. Length: " + encodedLength);
//            return Arrays.copyOf(buffer, encodedLength);
//        } else {
//            Log.w(TAG, "No audio data read");
//            return null;
//        }
//    }
//
//    public void release() {
//        if (audioRecord != null) {
//            audioRecord.stop();
//            audioRecord.release();
//            audioRecord = null; // Set to null after releasing
//            Log.d(TAG, "OpusEncoder-release: AudioRecord released");
//        } else {
//            Log.w(TAG, "AudioRecord is null");
//        }
//    }
//}
