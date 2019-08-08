package com.ben.selfadvocacy;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by benjm on 22-Mar-18.
 */

public class EnhancedImageButton extends androidx.appcompat.widget.AppCompatImageButton
{
    Context context;
    TextToSpeech tts;

    public EnhancedImageButton(Context c, AttributeSet as)
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
            EnhancedImageButton v = (EnhancedImageButton)view;
            String text = v.getTag().toString();

            //read it
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            Log.d("TTS", "Read: " + text);

            return true;
        }
    };

}
