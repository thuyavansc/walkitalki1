package au.com.softclient.walkitalki1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity2 extends AppCompatActivity {

    private HubConnection hubConnection;
    private int count = 0;  // Counter variable

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button buttonWT = findViewById(R.id.btnWT);
        buttonWT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // When the button is clicked, start Activity2
                Intent intent = new Intent(MainActivity2.this, WalkieTalkieActivity.class);
                startActivity(intent);
            }
        });

        // Initialize SignalR Hub Connection
        hubConnection = HubConnectionBuilder.create("http://192.168.8.168:5120/counthub")
                .build();

        // Register method to receive updated total count
        hubConnection.on("ReceiveCount", (totalCount) ->
                        runOnUiThread(() -> updateCountTextView((Integer) totalCount, findViewById(R.id.textView)))
                , Integer.class);

        // Start SignalR Connection
        Completable.fromAction(() -> hubConnection.start().blockingAwait())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("SignalR", "Connected"),
                        throwable -> Log.e("SignalR", "Error: " + throwable.getMessage())
                );

        // Set Button Click Listener
        Button button = findViewById(R.id.button2);
        button.setOnClickListener(v -> sendCountToServer());
    }

    private void sendCountToServer() {
        // Increment the count each time the button is clicked
        //count++;
        count=1;

        // Send count to the server
        Completable.fromAction(() -> hubConnection.invoke("PostCount", count).blockingAwait())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        () -> Log.d("SignalR", "Count sent to server"),
                        throwable -> Log.e("SignalR", "Error sending count to server: " + throwable.getMessage())
                );
    }

    private void updateCountTextView(int count, TextView textView) {
        // Update TextView with the received count
        textView.setText("Total Count: " + count);
    }
}
