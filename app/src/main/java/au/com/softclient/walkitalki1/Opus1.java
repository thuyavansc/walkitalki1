//package au.com.softclient.walkitalki1;
//
//import com.sun.jna.Library;
//import com.sun.jna.Native;
//import com.sun.jna.Pointer;
//import com.sun.jna.ptr.FloatByReference;
//import com.sun.jna.ptr.IntByReference;
//
//import java.nio.ByteBuffer;
//import java.nio.ShortBuffer;
//
//public interface Opus1 extends Library {
//    Opus1 INSTANCE = Native.load("opus", Opus1.class);
//
//    int OPUS_APPLICATION_AUDIO = 2049;
//    int OPUS_SET_BITRATE_REQUEST = 4002;
//    int OPUS_GET_BITRATE_REQUEST = 4003;
//    int OPUS_SET_COMPLEXITY_REQUEST = 4010;
//    int OPUS_SET_SIGNAL_REQUEST = 4024;
//    int OPUS_RESET_STATE = 4028;
//    int OPUS_OK = 0;
//
//    int opus_encoder_get_size(int channels);
//
//    Pointer opus_encoder_create(int Fs, int channels, int application, IntByReference error);
//
//    int opus_encoder_ctl(Pointer st, int request, Object... varargs);
//
//    int opus_encode(Pointer st, ShortBuffer pcm, int frame_size, ByteBuffer data, int max_data_bytes);
//
//    int opus_decoder_get_size(int channels);
//
//    Pointer opus_decoder_create(int Fs, int channels, IntByReference error);
//
//    int opus_decoder_ctl(Pointer st, int request, Object... varargs);
//
//    int opus_decode(Pointer st, ByteBuffer data, int len, ShortBuffer pcm, int frame_size, int decode_fec);
//
//    void opus_encoder_destroy(Pointer st);
//
//    void opus_decoder_destroy(Pointer st);
//
//    void opus_pcm_soft_clip(FloatByReference pcm, int frame_size, int channels, FloatByReference softclip_mem);
//}
//
