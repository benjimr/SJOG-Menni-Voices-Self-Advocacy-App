package com.ben.selfadvocacy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.AdapterView;

/*
    Adds a listener to regular text view that reads the contents of the text view when long clicked
*/

public class EnhancedTextView extends androidx.appcompat.widget.AppCompatTextView
{
    Context context;
    TextToSpeech tts;

    public EnhancedTextView(Context c, AttributeSet as)
    {
        super(c, as);
        context = c;

        tts = TTS.getTTS(c);
        this.setOnLongClickListener(readAloudListener);
   }

    OnLongClickListener readAloudListener = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View view) {
            //get string to be read
            EnhancedTextView v = (EnhancedTextView) view;
            String text = v.getText().toString();

            //read it
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            Log.d("TTS", "Read: " + text);

            return true;
        }
    };
}
