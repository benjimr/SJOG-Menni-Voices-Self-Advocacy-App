package com.ben.selfadvocacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Events extends AppCompatActivity
{
    LinearLayout overlay;
    ListView list;
    EnhancedTextView name;
    EnhancedTextView desc;
    EnhancedTextView date;
    String selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        //overlay views
        name = findViewById(R.id.nameOverlay);
        desc =findViewById(R.id.descOverlay);
        date = findViewById(R.id.dateOverlay);
        overlay = findViewById(R.id.Overlay);
        overlay.setVisibility(View.GONE);

        //get the event data
        final Controller controller = new Controller(getApplicationContext());
        List<Map<String,String>> data = controller.getEvents();

        //using retrieved data set adapter on list
        CustomAdapter a = new CustomAdapter(getApplicationContext(), R.layout.event_row, data);
        list = findViewById(R.id.list);
        list.setAdapter(a);

        //check if logged in user is an admin
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        Boolean isAdmin = prefs.getBoolean("isAdmin", false);

        EnhancedButton editEventButton = findViewById(R.id.editEvent);
        EnhancedButton deleteEventButton = findViewById(R.id.deleteEvent);

        //if they are an admin set listener on the button
        if(isAdmin)
        {
            View.OnClickListener editEvent = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent i = new Intent(getApplicationContext(), CreateEvent.class);
                    i.putExtra("eventID", selectedID);
                    i.putExtra("eventName", name.getText());
                    i.putExtra("eventDesc", desc.getText());
                    i.putExtra("eventDate", formatDateForStorage(date.getText().toString()));

                    finish();
                    startActivity(i);
                }
            };

            View.OnClickListener deleteEvent = new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    controller.deleteEvent(selectedID);
                    Intent i = getIntent();
                    finish();
                    startActivity(i);
                }
            };

            deleteEventButton.setOnClickListener(deleteEvent);
            editEventButton.setOnClickListener(editEvent);
        }
        else//otherwise hide the button
        {
            editEventButton.setVisibility(View.GONE);
            deleteEventButton.setVisibility(View.GONE);
        }
    }


    @Override
    public void onBackPressed()
    {
        //when back is pressed and overlay is open close it instead of going back a screen
        if(overlay.getVisibility() == View.VISIBLE)
        {
            overlay.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public class CustomAdapter extends ArrayAdapter<Map<String,String>>
    {
        Context c;
        List<Map<String,String>> data;

        public CustomAdapter(Context c, int rowLayout, List<Map<String,String>> data)
        {
            super(c,rowLayout,data);
            this.c =c;
            this.data = data;
        }

        public View getView(int pos, View view, ViewGroup vg)
        {
            if(view == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.event_row, vg,false);
            }

            final Map<String,String> event = data.get(pos);

            //used to show more details and an edit button in overlay
            EnhancedButton details = view.findViewById(R.id.details);
            details.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    //get the clicked event details and put it on the overlay, make it visible
                    name.setText(event.get("eventName"));
                    desc.setText(event.get("eventDesc"));
                    date.setText(formatDateForDisplay(event.get("eventDate")));
                    selectedID = event.get("eventID");

                    overlay.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                }
            });

            //display name and date in the row
            EnhancedTextView eventName = view.findViewById(R.id.name);
            EnhancedTextView eventDate = view.findViewById(R.id.date);

            //TODO edit the datetime format to make it more readable

            eventName.setText(event.get("eventName"));
            eventDate.setText(formatDateForDisplay(event.get("eventDate")));

            return view;
        }

        public String formatDateForDisplay(String datetime)
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date d = null;

            try
            {
                d = sdf.parse(datetime);
                sdf.applyPattern("EEEE, MMMM d, yyyy KK:mm aa");
            }
            catch(Exception e)
            {
                Log.d("TESTING"," " + e.toString());
            }

            return sdf.format(d);
        }
    }

    public String formatDateForStorage(String datetime)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy KK:mm aa");
        Date d = null;

        try
        {
            d = sdf.parse(datetime);
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception e)
        {
            Log.d("DEBUGMSG", "FormatDateForStorage: " + e.toString());
        }

        return sdf.format(d);
    }


}
