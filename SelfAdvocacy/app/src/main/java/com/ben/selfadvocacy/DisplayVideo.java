package com.ben.selfadvocacy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;
import java.util.Map;


public class DisplayVideo extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        WebView wv=(WebView)findViewById(R.id.webView1);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);

        Intent i = getIntent();
        Bundle extras = i.getExtras();

        if(extras != null && extras.containsKey("link"))
        {
            String link = extras.getString("link");
            Log.d("VIDEOS",link);
            wv.loadUrl(link);
            String password = "Voices2018" ;
            wv.loadUrl("javascript:(function() { document.getElementById('password').value = '" + password + "'; ;})()");
            wv.loadUrl("javascript:(function() { var z = document.getElementById('submit').click(); })()");
        }


    }


}
