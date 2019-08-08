package com.ben.selfadvocacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainMenu extends AppCompatActivity
{



    public String welcomeUser, usersPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        SharedPreferences prefs = getApplicationContext().getSharedPreferences("prefs", getApplicationContext().MODE_PRIVATE);
        String userName = prefs.getString("username", "default");
        Boolean isAdmin = prefs.getBoolean("isAdmin", false);

        EnhancedImageButton admin = findViewById(R.id.Admin);
        admin.setOnClickListener(goToAdmin);

        if(isAdmin)
        {
            admin.setVisibility(View.VISIBLE);
        }
        else
        {
            admin.setVisibility(View.GONE);
        }

        EnhancedImageButton rights = findViewById(R.id.HumanRights);
        rights.setOnClickListener(rightListener);

        EnhancedImageButton videos = findViewById(R.id.Videos);
        videos.setOnClickListener(videoListener);
        final Bundle info;
        info = getIntent().getExtras();

        EnhancedTextView welcome = findViewById(R.id.welcome);
        if(info != null)
        {
            String usersName = info.getString("username");
            String usersPassword = info.getString("password");
            welcome.setText(String.format("Welcome %s", usersName));
        }//end if()



        EnhancedImageButton events = findViewById(R.id.Events);
        events.setOnClickListener(goToEvents);

        //profile page
        EnhancedImageButton goToProfile = findViewById(R.id.profileButton);
        goToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                String newUser;
                String newPass;
                if (info != null)
                {
                    newUser = info.getString("username");
                    newPass = info.getString("password");


                    Intent i = new Intent(MainMenu.this, Profile.class);
                    i.putExtra("username", newUser);
                    i.putExtra("password", newPass);
                    Log.d("From LoginScreen", "---INSIDE MAIN MENU--------" + newUser + "-----------!");
                    Log.d("From LoginScreen", "---INSIDE MAIN MENU----------" + newPass + "---------!");
                    startActivity(i);
                }//end if

            }//end onClick()
        });

    }//end onCreate()

    View.OnClickListener rightListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            HumanRightsClicked(view);
        }
    };

    View.OnClickListener videoListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Toast.makeText(getApplicationContext(),"Loading Videos", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), VideoList.class);
            startActivity(i);
        }
    };

    View.OnClickListener goToAdmin  = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(getApplicationContext(), AdministratorTools.class);
            startActivity(i);
        }
    };


    View.OnClickListener goToEvents = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(getApplicationContext(), Events.class);
            startActivity(i);
        }
    };



    public void HumanRightsClicked(View v)
    {
        Intent i = new Intent(getApplicationContext(), HumanRightsList.class);
        startActivity(i);
    }
}



/* TODO
*
* ###THIS IS THE CODE FOR FETCHING A VID#####

        setContentView(R.layout.activity_vimeo);

        WebView wv=(WebView)findViewById(R.id.webView1);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.getSettings().setAppCacheEnabled(true);
        wv.getSettings().setDomStorageEnabled(true);

        wv.loadUrl("http://player.vimeo.com/video/[VIDEO ID GOES HERE]");


###THIS IS THE LAYOUT TO DISPLAY THE VID###

<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/activity_main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context="com.vimeo.android.networking.example.MainActivity">

    <WebView

        android:id="@+id/webView1"
        android:layout_height="match_parent"
        android:layout_width="match_parent" />
</RelativeLayout>
*
* */


//TODO SERVER LOGS IN TO VIMEO ACCOUNT OF SJOG
// TODO SEARCHES MENNI VOICES, GETS THE ID'S OF THE VIDEOS
//TODO SAVE THEM INTO THE DATABASE, GET THE ID's OF THE VIDEOS IN THE APP AND DISPLAY THE VIDEOS


