package com.msy_studios.chatgpt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.msy_studios.chatgpt.SpeechBubble;

public class MainActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1;
    private SpeechRecognitionManager speechRecognitionManager;
    public SpeechBubble speechBubble;
    private LinearLayout speechLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechRecognitionManager = new SpeechRecognitionManager();
        speechBubble = new SpeechBubble(getApplicationContext());

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndStartSpeechRecognition();
            }
        });

        RelativeLayout mainLayout = findViewById(R.id.mainLayout); // Ändere "mainLayout" entsprechend dem Namen deines Root-Layouts
        speechLayout = speechBubble.getSpeechLayout();
        mainLayout.addView(speechBubble.getSpeechLayout());
    }

    public void showSpeechBubble(String text) {
        speechBubble.showSpeech(text);
    }

    private void checkPermissionAndStartSpeechRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
                // Zeigen Sie eine Erklärung an, warum die Aufnahmeberechtigung erforderlich ist
                // Optional: Hier kannst du eine Dialogbox anzeigen, um dem Benutzer den Zweck der Berechtigung zu erklären
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Berechtigung erforderlich");
                builder.setMessage("Die App benötigt die Aufnahmeberechtigung, um Spracheingaben zu erkennen.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                RECORD_AUDIO_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        RECORD_AUDIO_PERMISSION_REQUEST_CODE);
            }
        } else {
            startSpeechRecognition();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // Basisklassenimplementierung aufrufen

        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognition();
            } else {
                // Die Berechtigung wurde abgelehnt. Hier kannst du entsprechend reagieren
                Toast.makeText(this, "Aufnahmeberechtigung wurde abgelehnt", Toast.LENGTH_SHORT).show();
            }

        }

    }


    private void startSpeechRecognition() {
        speechRecognitionManager.startSpeechRecognition();
    }
}
