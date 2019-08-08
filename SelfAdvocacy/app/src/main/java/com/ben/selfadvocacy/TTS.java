package com.ben.selfadvocacy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/*
    Handles setting up the tts object to reduce code repetition
*/

public abstract class TTS
{
    private static TextToSpeech tts = null;

    public static TextToSpeech getTTS(Context c)
    {
        if(tts == null)
        {
            tts = new TextToSpeech(c, new TextToSpeech.OnInitListener()
            {
                @Override
                public void onInit(int initResult)
                {
                    //check if initialisation succeeded
                    if(initResult == TextToSpeech.SUCCESS)
                    {
                        //set desired language
                        int langResult = tts.setLanguage(Locale.ENGLISH);

                        //check language set succeeded
                        if(langResult == TextToSpeech.LANG_NOT_SUPPORTED || langResult == TextToSpeech.LANG_MISSING_DATA)
                        {
                            Log.e("TTS", "Text to speech language error");
                        }
                    }
                    else
                    {
                        Log.e("TTS", "Text to speech initialisation error");
                    }

                    Log.d("TTS", "tts initialized");
                }
            });
        }

        return tts;
    }
}
