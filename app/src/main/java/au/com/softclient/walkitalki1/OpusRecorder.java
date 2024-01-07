//package au.com.softclient.walkitalki1;
//
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.AudioTrack;
//import android.media.MediaRecorder;
//import android.util.Log;
//
//import com.sun.jna.ptr.IntByReference;
//import com.sun.jna.ptr.PointerByReference;
//
//import java.nio.ByteBuffer;
//import java.nio.ShortBuffer;
//
//public class OpusRecorder {
//
//    private static final int SAMPLE_RATE = 48000;
//    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
//    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
//    private static final int FRAME_LENGTH = 960;
//
//    private AudioRecord recorder;
//    private AudioTrack track;
//    private Opus.OpusEncoder encoder;
//    private Opus.OpusDecoder decoder;
//
//    public void startRecordingAndPlaying() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                initRecorder();
//                initTrack();
//                initEncoder();
//                initDecoder();
//
//                startRecordingAndPlayback();
//            }
//        }).start();
//    }
//
//    private void startRecordingAndPlayback() {
//        short[] pcmInput = new short[FRAME_LENGTH];
//        ByteBuffer encodedBuffer = ByteBuffer.allocateDirect(FRAME_LENGTH * 2);
//
//        recorder.startRecording();
//        track.play();
//
//        while (true) {
//            int read = recorder.read(pcmInput, 0, pcmInput.length);
//            if (read > 0) {
//                ShortBuffer pcmInputBuffer = ShortBuffer.wrap(pcmInput);
//
//                int encoded = Opus.INSTANCE.opus_encode(encoder, pcmInputBuffer, read, encodedBuffer, encodedBuffer.capacity());
//                if (encoded > 0) {
//                    encodedBuffer.position(0);  // Reset position for reading
//                    ShortBuffer decodedBuffer = ShortBuffer.allocate(FRAME_LENGTH);
//                    int decoded = Opus.INSTANCE.opus_decode(decoder, encodedBuffer, encoded, decodedBuffer, FRAME_LENGTH, 0);
//                    track.write(toByteArray(decodedBuffer), 0, decoded * 2);
//                }
//            }
//        }
//    }
//
//    private void startRecordingAndPlayback() {
//        short[] pcmInput = new short[FRAME_LENGTH];
//        ByteBuffer encodedBuffer = ByteBuffer.allocateDirect(FRAME_LENGTH * 2);
//
//        recorder.startRecording();
//        track.play();
//
//        while (true) {
//            int read = recorder.read(pcmInput, 0, pcmInput.length);
//            if (read > 0) {
//                ShortBuffer pcmInputBuffer = ShortBuffer.wrap(pcmInput);
//
//                int encoded = Opus.INSTANCE.opus_encode(encoder, pcmInputBuffer, read, encodedBuffer, encodedBuffer.capacity());
//                int encodedBytes = au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encode(encoder, pcmInputBuffer, frameSize, ByteBuffer.wrap(encodedData), encodedData.length);
//
//                if (encoded > 0) {
//                    encodedBuffer.position(0);  // Reset position for reading
//                    ShortBuffer decodedBuffer = ShortBuffer.allocate(FRAME_LENGTH);
//                    int decoded = Opus.INSTANCE.opus_decode(decoder, encodedBuffer, encoded, decodedBuffer, FRAME_LENGTH, 0);
//                    track.write(toByteArray(decodedBuffer), 0, decoded * 2);
//                }
//            }
//        }
//    }
//
//    private byte[] toByteArray(ShortBuffer shortBuffer) {
//        shortBuffer.rewind();
//        int remaining = shortBuffer.remaining();
//        byte[] byteArray = new byte[remaining * 2];
//        for (int i = 0; i < remaining; i++) {
//            short sample = shortBuffer.get(i);
//            byteArray[i * 2] = (byte) (sample & 0xFF);
//            byteArray[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
//        }
//        return byteArray;
//    }
//
//
//    private void initRecorder() {
//        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//        recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
//    }
//
//    private void initTrack() {
//        int bufferSize = AudioTrack.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
//        track = new AudioTrack(android.media.AudioManager.STREAM_MUSIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize, AudioTrack.MODE_STREAM);
//    }
//
//    private void initEncoder() {
//        int error = 0;
//        int encoderSize = Opus.INSTANCE.opus_encoder_get_size(1);
//        PointerByReference encoderRef = new PointerByReference();
//        encoder = Opus.INSTANCE.opus_encoder_create(SAMPLE_RATE, 1, Opus.OPUS_APPLICATION_VOIP, encoderRef, error);
//        if (error != Opus.OPUS_OK) {
//            Log.e("OpusRecorder", "Encoder creation failed: " + Opus.INSTANCE.opus_strerror(error));
//        }
//    }
//
//    private void initDecoder() {
//        int error = 0;
//        int decoderSize = Opus.INSTANCE.opus_decoder_get_size(1);
//        PointerByReference decoderRef = new PointerByReference();
//        decoder = Opus.INSTANCE.opus_decoder_create(SAMPLE_RATE, 1, decoderRef, error);
//        if (error != Opus.OPUS_OK) {
//            Log.e("OpusRecorder", "Decoder creation failed: " + Opus.INSTANCE.opus_strerror(error));
//        }
//    }
//
//    private byte[] toByteArray(ShortBuffer shortBuffer) {
//        shortBuffer.rewind();
//        int remaining = shortBuffer.remaining();
//        byte[] byteArray = new byte[remaining * 2];
//        for (int i = 0; i < remaining; i++) {
//            short sample = shortBuffer.get(i);
//            byteArray[i * 2] = (byte) (sample & 0xFF);
//            byteArray[i * 2 + 1] = (byte) ((sample >> 8) & 0xFF);
//        }
//        return byteArray;
//    }
//}
