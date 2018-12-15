package com.example.user.kvs;

import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;

public class KVSSpeech {
    private static KVSSpeech LAST_INSTANCE;
    final private TextToSpeech textToSpeech1;
    final private String speak;
    public KVSSpeech(Context cont, String speakStr) {
        if (LAST_INSTANCE!=null){
            try{
                LAST_INSTANCE.textToSpeech1.stop();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        LAST_INSTANCE = this;

        this.speak = speakStr;
        textToSpeech1 = new TextToSpeech(cont, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // Log.d("speech", "Text to speech!");
                    textToSpeech1.speak(speak, TextToSpeech.QUEUE_FLUSH, null, null);
                    // API 20
                }
                else{
                    textToSpeech1.speak(speak, TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
    }
}