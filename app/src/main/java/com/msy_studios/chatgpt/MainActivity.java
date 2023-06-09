package com.msy_studios.chatgpt;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.msy_studios.chatgpt.SpeechBubble;

public class MainActivity extends AppCompatActivity {
    private static final int RECORD_AUDIO_PERMISSION_REQUEST_CODE = 1;
    private SpeechRecognitionManager speechRecognitionManager;
    private SpeechBubble speechBubble;
    private LinearLayout speechLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speechRecognitionManager = new SpeechRecognitionManager();
        TextView speechBubbleTextView = findViewById(R.id.speech_bubble);
        String recognizedText = "Dies ist der erkannte Text.";
        speechBubbleTextView.setText(recognizedText);

        Button startButton = findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermissionAndStartSpeechRecognition();
            }
        });

        RelativeLayout mainLayout = findViewById(R.id.mainLayout);
        LinearLayout speechLayout = findViewById(R.id.speech_layout);
        speechBubble = new SpeechBubble(this); // Beispiel für die Initialisierung
        ViewGroup speechBubbleParent = (ViewGroup) speechBubble.getParent();
        if (speechBubbleParent != null) {
            speechBubbleParent.removeView(speechBubble);
        }
        speechLayout.addView(speechBubble);
    }

    public void showSpeechBubble(String text) {
        speechBubble.showSpeech(text);
    }

    private void checkPermissionAndStartSpeechRecognition() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)) {
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
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == RECORD_AUDIO_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSpeechRecognition();
            } else {
                Toast.makeText(this, "Aufnahmeberechtigung wurde abgelehnt", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void startSpeechRecognition() {
        speechRecognitionManager.startSpeechRecognition();
    }
}
