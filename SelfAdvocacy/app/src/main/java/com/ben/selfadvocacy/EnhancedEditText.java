package com.ben.selfadvocacy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/*
    Adds a listener to regular button view that reads the name of the button when long clicked
*/

public class EnhancedEditText extends AppCompatEditText
{
    Context context;
    TextToSpeech tts;

    public EnhancedEditText(Context c, AttributeSet as)
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
            //get string to be read
            EnhancedEditText v = (EnhancedEditText)view;
            String text = v.getHint().toString();

            //read it
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            Log.d("TTS", "Read: " + text);

            return true;
        }
    };
}
