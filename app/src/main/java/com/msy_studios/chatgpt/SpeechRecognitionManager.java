package com.msy_studios.chatgpt;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import com.google.cloud.speech.v1.RecognitionAudio;
import com.google.cloud.speech.v1.RecognitionConfig;
import com.google.cloud.speech.v1.RecognizeResponse;
import com.google.cloud.speech.v1.SpeechClient;
import com.google.cloud.speech.v1.SpeechRecognitionAlternative;
import com.google.cloud.speech.v1.SpeechRecognitionResult;
import com.google.protobuf.ByteString;

import java.io.IOException;


public class SpeechRecognitionManager {

    private static final int SAMPLE_RATE = 16000; // Beispiel: Abtastrate von 16 kHz
    private static final int CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO; // Beispiel: Mono-Kanal
    private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT; // Beispiel: 16-Bit-PCM-Codierung

    public void startSpeechRecognition() {
        // Erstellen Sie eine SpeechClient-Instanz
        try (SpeechClient speechClient = SpeechClient.create()) {
            // Konfigurieren Sie die Spracherkennung
            RecognitionConfig recognitionConfig =
                    RecognitionConfig.newBuilder()
                            .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                            .setSampleRateHertz(16000)
                            .setLanguageCode("en-US")
                            .build();

            // Erfassen Sie die Spracheingabe vom Mikrofon
            // Hier müssen Sie den Code entsprechend Ihrer Android-Audioerfassungslogik anpassen
            byte[] audioData = captureAudioData();

            // Konvertieren Sie das Audio in ByteString
            ByteString audioBytes = ByteString.copyFrom(audioData);

            // Erstellen Sie eine RecognitionAudio-Instanz
            RecognitionAudio recognitionAudio =
                    RecognitionAudio.newBuilder().setContent(audioBytes).build();

            // Senden Sie die Spracheingabe an das Speech-to-Text-API
            RecognizeResponse response = speechClient.recognize(recognitionConfig, recognitionAudio);

            // Verarbeiten Sie die API-Antwort und erhalten Sie die erkannten Texte
            for (SpeechRecognitionResult result : response.getResultsList()) {
                SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
                String transcript = alternative.getTranscript();
                // Verarbeiten Sie den erkannten Text entsprechend Ihrer Anforderungen
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] captureAudioData() {
        // Initialisierung der Variablen
        int bufferSize = AudioRecord.getMinBufferSize(SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT);
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, SAMPLE_RATE, CHANNEL_CONFIG, AUDIO_FORMAT, bufferSize);
        byte[] audioData = new byte[bufferSize];

        // Start der Audioaufnahme
        audioRecord.startRecording();

        // Erfassen der Audiodaten
        int bytesRead = audioRecord.read(audioData, 0, bufferSize);

        // Stoppen der Audioaufnahme
        audioRecord.stop();
        audioRecord.release();

        // Rückgabe der erfassten Audiodaten
        return audioData;
    }


}
