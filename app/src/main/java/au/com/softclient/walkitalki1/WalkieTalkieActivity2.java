////package au.com.softclient.signalrreceiver1;
////
////import androidx.appcompat.app.AppCompatActivity;
////
////import android.os.Bundle;
////
////// WalkieTalkieActivity.java
////import android.media.AudioFormat;
////import android.media.AudioRecord;
////import android.media.MediaRecorder;
////import android.os.Bundle;
////import android.view.View;
////import android.widget.Button;
////import androidx.appcompat.app.AppCompatActivity;
////import com.microsoft.signalr.HubConnection;
////import com.microsoft.signalr.HubConnectionBuilder;
////import com.microsoft.signalr.HubConnectionState;
////import java.nio.ByteBuffer;
////import java.nio.ByteOrder;
////import java.nio.charset.StandardCharsets;
////import java.util.concurrent.ExecutorService;
////import java.util.concurrent.Executors;
////
////public class WalkieTalkieActivity extends AppCompatActivity {
////    private HubConnection hubConnection;
////    private boolean isTalking = false;
////    private AudioRecord audioRecord;
////    private ExecutorService executorService;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_walkie_talkie);
////
////        executorService = Executors.newSingleThreadExecutor();
////
////        hubConnection = HubConnectionBuilder.create("http://192.168.8.157:5106/walkietalkiehub")
////                .withAutomaticReconnect()
////                .build();
////
////        hubConnection.on("ReceiveVoiceData", (data) -> {
////            // Handle received voice data (e.g., play it)
////        }, byte[].class);
////    }
////
////    public void startListening(View view) {
////        // Implement logic for starting listening
////        // Update UI accordingly
////    }
////
////    public void startTalking(View view) {
////        if (!isTalking) {
////            startAudioRecording();
////            isTalking = true;
////            ((Button) view).setText("Stop");
////        } else {
////            stopAudioRecording();
////            isTalking = false;
////            ((Button) view).setText("Talk");
////        }
////    }
////
////    private void startAudioRecording() {
////        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
////        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
////
////        audioRecord.startRecording();
////
////        executorService.execute(() -> {
////            byte[] buffer = new byte[bufferSize];
////            OpusEncoder encoder = new OpusEncoder();
////
////            while (isTalking) {
////                int bytesRead = audioRecord.read(buffer, 0, bufferSize);
////                if (bytesRead > 0) {
////                    byte[] encodedData = encoder.encode(buffer, bytesRead);
////                    hubConnection.send("SendVoiceData", encodedData);
////                }
////            }
////
////            audioRecord.stop();
////            audioRecord.release();
////        });
////    }
////
////    private void stopAudioRecording() {
////        isTalking = false;
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
////            hubConnection.start().blockingAwait();
////        }
////    }
////
////    @Override
////    protected void onStop() {
////        super.onStop();
////        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
////            hubConnection.stop();
////        }
////    }
////}
//
//package au.com.softclient.walkitalki1;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.media.AudioFormat;
//import android.media.AudioRecord;
//import android.media.MediaRecorder;
//import android.os.Build;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//
//import com.microsoft.signalr.HubConnection;
//import com.microsoft.signalr.HubConnectionBuilder;
//import com.microsoft.signalr.HubConnectionState;
//
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
////
////public class WalkieTalkieActivity extends AppCompatActivity {
////    private static final String TAG = "WalkieTalkieActivity";
////    private static final int PERMISSION_REQUEST_CODE = 123;
////
////    private HubConnection hubConnection;
////    private boolean isTalking = false;
////    private AudioRecord audioRecord;
////    private ExecutorService executorService;
////    private AACEncoder aacEncoder;
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_walkie_talkie);
////
////        executorService = Executors.newSingleThreadExecutor();
////
////        aacEncoder = new AACEncoder(this);
////
////        // Check for and request the RECORD_AUDIO permission at runtime
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
////                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
////            } else {
////                initializeHubConnection();
////            }
////        } else {
////            initializeHubConnection();
////        }
////    }
////
////    private void initializeHubConnection() {
////        hubConnection = HubConnectionBuilder.create("http://192.168.8.157:5106/walkietalkiehub")
////                .build();
////
////        hubConnection.on("ReceiveVoiceData", (data) -> {
////            // Handle received voice data (e.g., play it)
////            Log.i(TAG, "Received voice data, length: " + data.length);
////        }, byte[].class);
////
////        hubConnection.on("ReceiveMessage", (message) -> {
////            // Handle received messages
////            Log.i(TAG, "Received message: " + message);
////        }, String.class);
////
////        // Handle reconnection manually
////        hubConnection.onClosed(exception -> {
////            if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
////                // Reconnect logic
////                hubConnection.start().blockingAwait();
////            }
////        });
////    }
////
////
////    @Override
////    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
////        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
////
////        if (requestCode == PERMISSION_REQUEST_CODE) {
////            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
////                Log.i(TAG, "Permission granted for RECORD_AUDIO");
////                initializeHubConnection();
////                //aacEncoder = new AACEncoder();
////                //aacEncoder = new AACEncoder(this);
////                if (aacEncoder != null) {
////                    Log.i(TAG, "AACEncoder initialized");
////                } else {
////                    Log.e(TAG, "WalkieTalkieActivity-onRequestPermissionsResult:AACEncoder is null");
////                }
////            } else {
////                Log.e(TAG, "Permission denied for RECORD_AUDIO");
////                // Permission denied, handle accordingly (e.g., show a message to the user)
////            }
////        }
////    }
////
////
////    public void startListening(View view) {
////        // Implement logic for starting listening
////        // Update UI accordingly
////    }
////
////
////
////    /*
////    public void startTalking(View view) {
////        if (!isTalking) {
////            startAudioRecording();
////            isTalking = true;
////            ((Button) view).setText("Stop");
////
////            // Send a message to the server that the client started talking
////            hubConnection.send("SendMessage", "Client started talking!");
////        } else {
////            stopAudioRecording();
////            isTalking = false;
////            ((Button) view).setText("Talk");
////            if (aacEncoder != null) {
////                aacEncoder.release();
////                aacEncoder = null;  // Set to null after releasing
////                Log.i(TAG, "AACEncoder released");
////            } else {
////                Log.w(TAG, "WalkieTalkieActivity-startTalking:AACEncoder is null");
////            }
////
////            // Send a message to the server that the client stopped talking
////            hubConnection.send("SendMessage", "Client stopped talking");
////        }
////    }
////     */
////
////    public void startTalking(View view) {
////        if (!isTalking) {
////            startAudioRecording();
////            isTalking = true;
////            ((Button) view).setText("Stop");
////
////            // Send a message to the server that the client started talking
////            hubConnection.send("SendMessage", "Client started talking!");
////        } else {
////            stopAudioRecording();
////            isTalking = false;
////            ((Button) view).setText("Talk");
////
////            // Send a message to the server that the client stopped talking
////            hubConnection.send("SendMessage", "Client stopped talking");
////        }
////    }
////
////
////
////    /*
////    private void startAudioRecording() {
////        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
////            Log.e(TAG, "RECORD_AUDIO permission not granted");
////            return;
////        }
////        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
////
////        audioRecord.startRecording();
////
////        executorService.execute(() -> {
////            byte[] buffer = new byte[bufferSize];
////
////            while (isTalking) {
////                int bytesRead = audioRecord.read(buffer, 0, bufferSize);
////                if (bytesRead > 0) {
////                    if (aacEncoder != null) {
////                        byte[] encodedData = aacEncoder.encode(); // Use AACEncoder for encoding
////                        Log.i(TAG, "Encoded data length: " + encodedData.length);
////                        hubConnection.send("SendVoiceData", encodedData);
////                    } else {
////                        Log.e(TAG, "WalkieTalkieActivity-startAudioRecording:AACEncoder is null");
////                    }
////                }
////            }
////
////            audioRecord.stop();
////            audioRecord.release();
////        });
////    }
////    */
////
////    private void startAudioRecording() {
////        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
////        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
////            Log.e(TAG, "RECORD_AUDIO permission not granted");
////            return;
////        }
////        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
////
////        audioRecord.startRecording();
////
////        executorService.execute(() -> {
////            byte[] buffer = new byte[bufferSize];
////
////            while (isTalking) {
////                int bytesRead = audioRecord.read(buffer, 0, bufferSize);
////                if (bytesRead > 0) {
////                    if (aacEncoder != null) {
////                        byte[] encodedData = aacEncoder.encode(); // Use AACEncoder for encoding
////                        hubConnection.send("SendVoiceData", encodedData);
////                    } else {
////                        Log.e(TAG, "WalkieTalkieActivity-startAudioRecording:AACEncoder is null");
////                    }
////                }
////            }
////
////            audioRecord.stop();
////            audioRecord.release();
////
////            if (aacEncoder != null) { // Move AACEncoder release to stopAudioRecording
////                aacEncoder.release();
////                aacEncoder = null;
////            }
////        });
////    }
////
////    private void stopAudioRecording() {
////        isTalking = false;
////    }
////
////    @Override
////    protected void onResume() {
////        super.onResume();
////        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
////            hubConnection.start().blockingAwait();
////        }
////    }
////
////    @Override
////    protected void onStop() {
////        super.onStop();
////        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
////            hubConnection.stop();
////        }
////    }
////}
////*/
//
////import microsoft.aspnet.signalr.client.Credentials;
////import microsoft.aspnet.signalr.client.HubConnection;
////import microsoft.aspnet.signalr.client.HubConnectionBuilder;
////import microsoft.aspnet.signalr.client.HubConnectionState;
////import microsoft.aspnet.signalr.client.Logger;
////import microsoft.aspnet.signalr.client.SignalRFuture;
////import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
////import microsoft.aspnet.signalr.client.http.android.AndroidLogger;
//
//import com.score.rahasak.utils;
//public class WalkieTalkieActivity2 extends AppCompatActivity {
//    private static final String TAG = "WalkieTalkieActivity";
//    private static final int PERMISSION_REQUEST_CODE = 123;
//
//    private HubConnection hubConnection;
//    private boolean isTalking = false;
//    private AudioRecord audioRecord;
//    private ExecutorService executorService;
//    private OpusEncoder opusEncoder;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_walkie_talkie);
//
//        executorService = Executors.newSingleThreadExecutor();
//
//        opusEncoder = new OpusEncoder(this);
//
//        // Check for and request the RECORD_AUDIO permission at runtime
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, PERMISSION_REQUEST_CODE);
//            } else {
//                initializeHubConnection();
//            }
//        } else {
//            initializeHubConnection();
//        }
//    }
//
//    private void initializeHubConnection() {
//        hubConnection = HubConnectionBuilder.create("http://192.168.8.168:5120/walkietalkiehub")
//                .build();
//
//        hubConnection.on("ReceiveVoiceData", (data) -> {
//            // Handle received voice data (e.g., play it)
//            Log.i(TAG, "Received voice data, length: " + data.length);
//        }, byte[].class);
//
//        hubConnection.on("ReceiveMessage", (message) -> {
//            // Handle received messages
//            Log.i(TAG, "Received message: " + message);
//        }, String.class);
//
//        // Handle reconnection manually
//        hubConnection.onClosed(exception -> {
//            if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
//                // Reconnect logic
//                hubConnection.start().blockingAwait();
//            }
//        });
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.i(TAG, "Permission granted for RECORD_AUDIO");
//                initializeHubConnection();
//                if (opusEncoder != null) {
//                    Log.i(TAG, "OpusEncoder initialized");
//                } else {
//                    Log.e(TAG, "WalkieTalkieActivity-onRequestPermissionsResult: OpusEncoder is null");
//                }
//            } else {
//                Log.e(TAG, "Permission denied for RECORD_AUDIO");
//                // Permission denied, handle accordingly (e.g., show a message to the user)
//            }
//        }
//    }
//
//    public void startListening(View view) {
//        // Implement logic for starting listening
//        // Update UI accordingly
//    }
//
//    public void startTalking(View view) {
//        if (!isTalking) {
//            startAudioRecording();
//            isTalking = true;
//            ((Button) view).setText("Stop");
//
//            // Send a message to the server that the client started talking
//            hubConnection.send("SendMessage", "Client started talking!");
//        } else {
//            stopAudioRecording();
//            isTalking = false;
//            ((Button) view).setText("Talk");
//
//            // Send a message to the server that the client stopped talking
//            hubConnection.send("SendMessage", "Client stopped talking");
//        }
//    }
//
//    private void startAudioRecording() {
//        int bufferSize = AudioRecord.getMinBufferSize(16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
//            Log.e(TAG, "RECORD_AUDIO permission not granted");
//            return;
//        }
//        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 16000, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize);
//
//        audioRecord.startRecording();
//
//        executorService.execute(() -> {
//            byte[] buffer = new byte[bufferSize];
//
//            while (isTalking) {
//                int bytesRead = audioRecord.read(buffer, 0, bufferSize);
//                if (bytesRead > 0) {
//                    if (opusEncoder != null) {
//                        byte[] encodedData = opusEncoder.encode(); // Use OpusEncoder for encoding
//                        hubConnection.send("SendVoiceData", encodedData);
//                    } else {
//                        Log.e(TAG, "WalkieTalkieActivity-startAudioRecording: OpusEncoder is null");
//                    }
//                }
//            }
//
//            audioRecord.stop();
//            audioRecord.release();
//        });
//    }
//
//    private void stopAudioRecording() {
//        isTalking = false;
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (hubConnection.getConnectionState() == HubConnectionState.DISCONNECTED) {
//            hubConnection.start().blockingAwait();
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
//            hubConnection.stop();
//        }
//    }
//}
