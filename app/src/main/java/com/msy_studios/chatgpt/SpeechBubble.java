package com.msy_studios.chatgpt;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

public class SpeechBubble extends View {
    private Context context;
    private LinearLayout speechLayout;
    private TextView speechText;
    private String recognizedText;

    public SpeechBubble(Context context) {
        super(context);
        this.context = context;
        initializeViews();
    }

    public SpeechBubble(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initializeViews();
    }

    public SpeechBubble(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initializeViews();
    }

    private void init() {
        // Initialisierungscode hier
        recognizedText = "";
    }

    public void setRecognizedText(String text) {
        recognizedText = text;
        invalidate(); // Benachrichtigt die View, dass sie neu gezeichnet werden muss
    }

    private void initializeViews() {
        speechLayout = new LinearLayout(context);
        speechLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        speechLayout.setOrientation(LinearLayout.VERTICAL);

        speechText = new TextView(context);
        speechText.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        speechText.setTextColor(Color.BLACK);
        speechText.setPadding(16, 8, 16, 8);
        speechText.setBackground(ContextCompat.getDrawable(context, R.drawable.speech_bubble));

        speechLayout.addView(speechText);
    }

    public void showSpeech(String text) {
        speechText.setText(text);

        // Hier können Sie weitere Anpassungen an der Sprechblase vornehmen

        // Fügen Sie die Sprechblase zu Ihrer Activity hinzu
        // Beispiel: parentLayout.addView(speechLayout);
    }

    public void hideSpeech() {
        // Entfernen Sie die Sprechblase aus Ihrer Activity
        // Beispiel: parentLayout.removeView(speechLayout);
    }

    public LinearLayout getSpeechLayout() {
        return speechLayout;
    }
}
