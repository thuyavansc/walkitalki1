//package au.com.softclient.walkitalki1;
//
//
//import com.sun.jna.ptr.PointerByReference;
//
//import java.nio.IntBuffer;
//
//import com.sun.jna.ptr.IntByReference;
//import com.sun.jna.ptr.PointerByReference;
//
//import java.nio.ByteBuffer;
//import java.nio.IntBuffer;
//import java.nio.ShortBuffer;
//
//public class OpusExample {
//    public static void main(String[] args) {
//        // Initialize Opus encoder
//        int sampleRate = 48000;
//        int channels = 1;  // Mono
//        int application = au.com.softclient.walkitalki1.Opus.OPUS_APPLICATION_AUDIO;
//
//        IntBuffer error = IntBuffer.allocate(1);
//        PointerByReference encoder = au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encoder_create(sampleRate, channels, application, error);
//
//        if (error.get(0) != au.com.softclient.walkitalki1.Opus.OPUS_OK) {
//            System.out.println("Error initializing Opus encoder: " + au.com.softclient.walkitalki1.Opus.INSTANCE.opus_strerror(error.get(0)));
//            return;
//        }
//
//        // Initialize Opus decoder
//        PointerByReference decoder = au.com.softclient.walkitalki1.Opus.INSTANCE.opus_decoder_create(sampleRate, channels, error);
//
//        if (error.get(0) != au.com.softclient.walkitalki1.Opus.OPUS_OK) {
//            System.out.println("Error initializing Opus decoder: " + au.com.softclient.walkitalki1.Opus.INSTANCE.opus_strerror(error.get(0)));
//            au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encoder_destroy(encoder);  // Clean up encoder if decoder initialization fails
//            return;
//        }
//
//        // Simulate an input PCM signal (replace this with your actual PCM data)
//        short[] pcmInput = new short[]{1000, 2000, 3000, 4000, 5000};
//
//        // Encode the PCM signal
//        ShortBuffer pcmInputBuffer = ShortBuffer.wrap(pcmInput);
//        int frameSize = pcmInput.length;
//        byte[] encodedData = new byte[1024];  // Adjust the buffer size as needed
//
//        int encodedBytes = au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encode(encoder, pcmInputBuffer, frameSize, ByteBuffer.wrap(encodedData), encodedData.length);
//
//        if (encodedBytes < 0) {
//            System.out.println("Error encoding PCM: " + au.com.softclient.walkitalki1.Opus.INSTANCE.opus_strerror(encodedBytes));
//            au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encoder_destroy(encoder);
//            au.com.softclient.walkitalki1.Opus.INSTANCE.opus_decoder_destroy(decoder);
//            return;
//        }
//
//        // Decode the encoded data
//        ShortBuffer decodedPcm = ShortBuffer.allocate(frameSize);
//        int decodedSamples = au.com.softclient.walkitalki1.Opus.INSTANCE.opus_decode(decoder, encodedData, encodedBytes, decodedPcm, frameSize, 0);
//        if (decodedSamples < 0) {
//            System.out.println("Error decoding data: " + au.com.softclient.walkitalki1.Opus.INSTANCE.opus_strerror(decodedSamples));
//        } else {
//            System.out.println("Decoded PCM: " + decodedPcm.get());
//        }
//
//        // Clean up resources
//        au.com.softclient.walkitalki1.Opus.INSTANCE.opus_encoder_destroy(encoder);
//        au.com.softclient.walkitalki1.Opus.INSTANCE.opus_decoder_destroy(decoder);
//    }
//}
