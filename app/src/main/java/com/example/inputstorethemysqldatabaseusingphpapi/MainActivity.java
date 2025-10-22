package com.example.inputstorethemysqldatabaseusingphpapi;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.StrictMode;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editName, editEmail;
    Button btnSubmit;
    TextView textResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        btnSubmit = findViewById(R.id.btnSubmit);
        textResult = findViewById(R.id.textResult);

        // Allow network on main thread (for testing purpose)
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendDataToServer();
            }
        });
    }

    private void sendDataToServer() {
        String name = editName.getText().toString();
        String email = editEmail.getText().toString();

        try {

            URL url = new URL("http://10.0.2.2/myapi/insert_user.php");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            String data = "name=" + name + "&email=" + email;

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            textResult.setText("Server Response: " + response.toString());
            conn.disconnect();

        } catch (Exception e) {
            textResult.setText("Error: " + e.getMessage());
        }
    }
}
