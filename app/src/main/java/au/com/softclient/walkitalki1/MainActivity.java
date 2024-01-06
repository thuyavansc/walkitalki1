package au.com.softclient.walkitalki1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

//package au.com.softclient.signalrreceiver1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.microsoft.signalr.OnClosedCallback;
import java.util.concurrent.TimeUnit;

/*
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private HubConnection hubConnection;
    private TextView messageTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        messageTextView = findViewById(R.id.messageTextView);

        hubConnection = HubConnectionBuilder.create("http://192.168.8.157:5106/chathub")
                .build();

        hubConnection.on("ReceiveMessage", (userName, message) -> {
            runOnUiThread(() -> {
                String receivedMessage = userName + ": " + message + "\n";
                Log.d(TAG, "onCreate: "+ receivedMessage);
                messageTextView.append(receivedMessage);
                messageTextView.setText(receivedMessage);
            });
        }, String.class, String.class);

        try {
            hubConnection.start().timeout(3000, TimeUnit.SECONDS).blockingAwait();

            if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                // Connection successful
                Log.d(TAG,"Connection successful");

                hubConnection.on("ReceiveMessage", (userName, message) -> {
                    runOnUiThread(() -> {
                        String receivedMessage = userName + ": " + message + "\n";
                        Log.d(TAG, "onCreate: "+ receivedMessage);
                        messageTextView.append(receivedMessage);
                        messageTextView.setText(receivedMessage);
                    });
                }, String.class, String.class);

            } else {
                // Handle the case where the connection is not active
                Log.d(TAG,"Connection Not successful");

            }
        } catch (Exception e) {
            // Handle exceptions, including connection timeout
            e.printStackTrace();
            Log.d(TAG, "onCreate: "+ e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            hubConnection.stop();
        }
    }
}

 */

// MainActivity.java
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private HubConnection hubConnection;
    private TextView messageTextView;
    private TextView userNameTextView; // Add this line

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, start Activity2
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
            }
        });

        messageTextView = findViewById(R.id.messageTextView);
        userNameTextView = findViewById(R.id.userNameTextView); // Initialize the TextView

        hubConnection = HubConnectionBuilder.create("http://192.168.8.168:5120/chathub")
                .build();

//        hubConnection.on("ReceiveMessage", (userName, message) -> {
//            runOnUiThread(() -> {
//                String receivedMessage = userName + ": " + message + "\n";
//                Log.d(TAG, "Received message: " + receivedMessage);
//                messageTextView.append(receivedMessage);
//
//                // Update the userNameTextView with the constant username
//                userNameTextView.setText("Constant Username: " + userName);
//                Log.d(TAG,userName + "---"+message);
//            });
//        }, String.class, String.class);
//        hubConnection.on("ReceiveMessage", (message) -> {
//            runOnUiThread(() -> {
//                String receivedMessage = "Received message: " + message + "\n";
//                Log.d(TAG, "Received message:"+ receivedMessage);
//                messageTextView.append(receivedMessage);
//
//                // Update the userNameTextView with a constant message
//                //userNameTextView.setText("Constant Message: Hello, I am .Net");
//            });
//        }, String.class);
        hubConnection.on("ReceiveMessage", (message) -> {
            runOnUiThread(() -> {
                try {
                    String receivedMessage = "Received message: " + message + "\n";
                    Log.d(TAG, receivedMessage);
                    messageTextView.append(receivedMessage);
                    Log.d(TAG, "Received message:"+ receivedMessage);

                    // Update the userNameTextView with a constant message
                    userNameTextView.setText("Constant Message: Hello, I am .Net");
                } catch (Exception e) {
                    Log.e(TAG, "Exception in on callback", e);
                }
            });
        }, String.class);


        try {
            hubConnection.start().timeout(3000, TimeUnit.SECONDS).blockingAwait();

            if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
                Log.d(TAG, "Connection successful");

                // Assuming you want to send a message when the connection is successful
                hubConnection.send("SendMessage", "Hello from Android!");
            } else {
                // Handle the case where the connection is not active
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "onCreate: " + e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (hubConnection.getConnectionState() == HubConnectionState.CONNECTED) {
            hubConnection.stop();
        }
    }
}

