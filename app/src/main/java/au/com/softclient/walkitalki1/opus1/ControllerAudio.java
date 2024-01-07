package au.com.softclient.walkitalki1.opus1;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.*;
import android.media.audiofx.AutomaticGainControl;
import android.media.audiofx.NoiseSuppressor;
import android.util.Log;

import androidx.core.app.ActivityCompat;

public class ControllerAudio {

    private static final String TAG = "ControllerAudio";
    private static int frameSize = -1;
    private static AudioRecord recorder;
    private static boolean micEnabled = false;
    private static AudioTrack track;
    private static boolean trackReady = false;
    private static NoiseSuppressor noiseSuppressor;
    private static AutomaticGainControl automaticGainControl;

    //
    // Record
    //

    public static void initRecorder(Context context, int sampleRate, int frameSize, boolean isMono) {
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, isMono ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);

        for (int i = 0; i <= 5; i++) {
            try {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                recorder = new AudioRecord(
                        MediaRecorder.AudioSource.MIC,
                        sampleRate,
                        isMono ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize
                );

                ControllerAudio.frameSize = frameSize;

                if (NoiseSuppressor.isAvailable()) {
                    try {
                        noiseSuppressor = NoiseSuppressor.create(recorder.getAudioSessionId());
                        if (noiseSuppressor != null) noiseSuppressor.setEnabled(true);
                    } catch (Exception e) {
                        Log.e(TAG, "[initRecorder] unable to init noise suppressor: " + e);
                    }
                }

                if (AutomaticGainControl.isAvailable()) {
                    try {
                        automaticGainControl = AutomaticGainControl.create(recorder.getAudioSessionId());
                        if (automaticGainControl != null) automaticGainControl.setEnabled(true);
                    } catch (Exception e) {
                        Log.e(TAG, "[initRecorder] unable to init automatic gain control: " + e);
                    }
                }
                onMicStateChange(true);
                break;
            } catch (Exception e) {
                Log.e(TAG, "[initRecorder] error: " + e);
            }
        }
    }

    public static void startRecord() {
        if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
            recorder.startRecording();
            micEnabled = true;
        }
    }

    public static byte[] getFrame() {
        byte[] frame = new byte[frameSize];
        int offset = 0;
        int remained = frame.length;
        while (remained > 0) {
            int read = recorder.read(frame, offset, remained);
            offset += read;
            remained -= read;
        }
        if (remained <= 0) return frame;
        return null;
    }

    public static short[] getFrameShort() {
        short[] frame = new short[frameSize];
        int offset = 0;
        int remained = frame.length;
        while (remained > 0) {
            int read = recorder.read(frame, offset, remained);
            offset += read;
            remained -= read;
        }
        if (remained <= 0) return frame;
        return null;
    }

    public static void onMicStateChange(boolean micEnabled) {
        ControllerAudio.micEnabled = micEnabled;
    }

    public static void stopRecord() {
        try {
            if (recorder.getState() == AudioTrack.STATE_INITIALIZED) recorder.stop();
            recorder.release();
            if (noiseSuppressor != null) noiseSuppressor.release();
            if (automaticGainControl != null) automaticGainControl.release();
        } catch (Exception e) {
            Log.e(TAG, "[stopRecord] error: " + e);
        }
    }

    //
    // Play
    //

    public static void initTrack(int sampleRate, boolean isMono) {
        int bufferSize = AudioRecord.getMinBufferSize(sampleRate, isMono ? AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT);
        for (int i = 0; i <= 5; i++) {
            try {
                track = new AudioTrack(
                        AudioManager.STREAM_MUSIC,
                        sampleRate,
                        isMono ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO,
                        AudioFormat.ENCODING_PCM_16BIT,
                        bufferSize,
                        AudioTrack.MODE_STREAM
                );

                if (track.getState() == AudioRecord.STATE_INITIALIZED) {
                    track.play();
                    trackReady = true;
                    break;
                }
            } catch (Exception e) {
                Log.e(TAG, "[initTrack] error: " + e);
            }
        }
    }

    public static void write(short[] frame) {
        if (!trackReady) return;
        track.write(frame, 0, frame.length);
    }

    public static void write(byte[] frame) {
        if (!trackReady) return;
        track.write(frame, 0, frame.length);
    }

    public static void stopTrack() {
        if (!trackReady) return;
        if (track.getState() == AudioTrack.STATE_INITIALIZED) track.stop();
        track.flush();
        trackReady = false;
    }

    public static void destroy() {
        stopRecord();
        stopTrack();
    }
}