package ro.pub.cs.systems.eim.practicaltest02v5;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PracticalTest02v5MainActivity extends AppCompatActivity {

    EditText portServer, addressClient, portClient, key, value;
    Spinner actionSpinner;

    Button startServer, startClient;
    EditText result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_practial_test02v5_main);

        portServer = findViewById(R.id.editTextText);
        addressClient = findViewById(R.id.editTextText2);
        portClient = findViewById(R.id.editTextText3);
        key = findViewById(R.id.editTextText4);
        value = findViewById(R.id.editTextText5);
        actionSpinner = findViewById(R.id.info_spinner);
        startServer = findViewById(R.id.button);
        startClient = findViewById(R.id.button2);
        result = findViewById(R.id.textView4);

        startServer.setOnClickListener(v -> {
            ServerThread serverThread = new ServerThread(Integer.parseInt(portServer.getText().toString()));
            Log.e(Constants.TAG, "Server started on port: " + portServer.getText().toString());
            serverThread.start();
            startServer.setText("Server started");
        });

        startClient.setOnClickListener(v -> {
            ClientThread clientThread = new ClientThread(addressClient.getText().toString(), Integer.parseInt(portClient.getText().toString()), actionSpinner.getSelectedItem().toString(), key.getText().toString(), value.getText().toString(), result);
            clientThread.start();
        });
    }
}