package au.com.softclient.walkitalki1.opus1;

import android.media.AudioFormat;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;

import com.theeasiestway.opus.Constants;
import com.theeasiestway.opus.Opus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class VoicePlayer {

    private static final int SAMPLE_RATE = 16000; // Adjust as needed
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);

    private AudioTrack audioTrack;
    private Opus opus;

    public VoicePlayer(Opus opus) {
        this.opus = opus;
        audioTrack = new AudioTrack(AudioTrack.MODE_STREAM, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
    }

    public void startPlaying() {
        audioTrack.play();
        byte[] buffer = new byte[BUFFER_SIZE];
        FileInputStream fileInputStream = null;
        try {
            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/VoiceRecorder");
            File file = new File(directory, "encoded_voice.opus");
            fileInputStream = new FileInputStream(file);

            // Initialize Opus decoder
            opus.decoderInit(Constants.SampleRate.Companion._16000(), Constants.Channels.Companion.mono());

            while (fileInputStream.read(buffer) > 0) {
                // Decode the audio data
                byte[] decodedData = opus.decode(buffer, Constants.FrameSize.Companion._120());
                audioTrack.write(decodedData, 0, decodedData.length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void stopPlaying() {
        audioTrack.stop();
        audioTrack.release();

        // Release Opus decoder resources
        opus.decoderRelease();
    }
}

//public class VoicePlayer {
//
//    private static final int SAMPLE_RATE = 16000; // Adjust as needed
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioTrack audioTrack;
//    private Opus opus;
//
//    public VoicePlayer(Opus opus) {
//        this.opus = opus;
//        audioTrack = new AudioTrack(AudioTrack.MODE_STREAM, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
//    }
//
//    public void startPlaying() {
//        audioTrack.play();
//        byte[] buffer = new byte[BUFFER_SIZE];
//        FileInputStream fileInputStream = null;
//        try {
//            File directory = new File(Environment.getExternalStorageDirectory().getPath() + "/VoiceRecorder");
//            File file = new File(directory, "encoded_voice.opus");
//            fileInputStream = new FileInputStream(file);
//
//            while (fileInputStream.read(buffer) > 0) {
//                byte[] decodedData = opus.decode(buffer, Constants.FrameSize.Companion._120());
//                audioTrack.write(decodedData, 0, decodedData.length);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (fileInputStream != null) {
//                try {
//                    fileInputStream.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//
//    public void stopPlaying() {
//        audioTrack.stop();
//        audioTrack.release();
//    }
//}


//public class VoicePlayer {
//    private static final int SAMPLE_RATE = 16000; // Adjust as needed
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_OUT_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int BUFFER_SIZE = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//
//    private AudioTrack audioTrack;
//    private Opus opus;
//
//    public VoicePlayer(Opus opus) {
//        this.opus = opus;
//        audioTrack = new AudioTrack(AudioTrack.MODE_STREAM, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, BUFFER_SIZE, AudioTrack.MODE_STREAM);
//    }
//
//    public void startPlaying() {
//        audioTrack.play();
//        byte[] buffer = new byte[BUFFER_SIZE];
//        while (true) {
//            // Receive encodedData from server or any other source
//            // For this example, we'll assume you have received encodedData
//            byte[] encodedData = receiveEncodedDataFromServer();
//            if (encodedData != null) {
//                byte[] decodedData = opus.decode(encodedData, Constants.FrameSize.Companion._120());
//                audioTrack.write(decodedData, 0, decodedData.length);
//            }
//        }
//    }
//
//    public void stopPlaying() {
//        audioTrack.stop();
//        audioTrack.release();
//    }
//
//    // For this example, simulate receiving encoded data from a server
//    private byte[] receiveEncodedDataFromServer() {
//        // Replace this with your logic to receive encoded data from a server
//        return null;
//    }
//}

