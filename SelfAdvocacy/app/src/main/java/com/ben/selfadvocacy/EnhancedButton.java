package com.ben.selfadvocacy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/*
    Adds a listener to regular button view that reads the name of the button when long clicked
*/

public class EnhancedButton extends androidx.appcompat.widget.AppCompatButton
{
    Context context;
    TextToSpeech tts;

    public EnhancedButton(Context c, AttributeSet as)
    {
        super(c, as);
        context = c;

        tts = TTS.getTTS(c);
        this.setOnLongClickListener(readAloudListener);
    }

    OnLongClickListener readAloudListener = new OnLongClickListener()
    {
        @Override
        public boolean onLongClick(View view)
        {
            //get the string to be read
            EnhancedButton v = (EnhancedButton)view;
            String text = v.getText().toString();

            //read it
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            Log.d("TTS", "Read: " + text);

            return true;
        }
    };
}
