package com.ben.selfadvocacy;

import android.content.Intent;
import android.provider.CalendarContract;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateEvent extends AppCompatActivity
{
    EnhancedEditText nameET, descET;
    EnhancedTextView timeTV, dateTV;
    EnhancedButton timeB, dateB, timeConB, dateConB;
    RelativeLayout timeLayout, dateLayout;
    CalendarView dateCV;
    TimePicker timeP;
    String selectedDate = "Error getting current date";
    String selectedTime = "13:00:00";

    //for use with editing
    String eventID;
    Boolean editing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        findViewById(R.id.submit).setOnClickListener(submit);

        nameET = findViewById(R.id.name);//for entering event name
        descET = findViewById(R.id.desc);//for entering event description

        //main page date views
        dateTV = findViewById(R.id.dateDisp);//for displaying the selected date
        dateB = findViewById(R.id.chooseDate);//to open calendar
        dateB.setOnClickListener(toggleCal);

        //overlay data views
        dateLayout = findViewById(R.id.dateLayout);//overlay layout
        dateLayout.setVisibility(View.GONE);//hidden by default

        dateCV = findViewById(R.id.date);//calendar for selecting the date
        dateCV.setOnDateChangeListener(selectedDateChange);

        dateConB = findViewById(R.id.confirmDate);//to confirm selection on the calendar
        dateConB.setOnClickListener(toggleCal);


        //main page time views
        timeTV = findViewById(R.id.timeDisp);//for displaying selected time

        timeB = findViewById(R.id.chooseTime);//for opening time picker
        timeB.setOnClickListener(toggleClock);

        //overlay time views
        timeLayout = findViewById(R.id.timeLayout);//overlay layout
        timeLayout.setVisibility(View.GONE);//hidden by default

        timeP = findViewById(R.id.time);//time picker for ... picking time
        timeP.setIs24HourView(true);
        timeP.setOnTimeChangedListener(selectedTimeChange);

        timeConB = findViewById(R.id.confirmTime);//for confirming chosen time
        timeConB.setOnClickListener(toggleClock);

        //get the intent used to start this activity
        Intent i = getIntent();

        //if extras passed in it means we are editing an event not creating a new one
        if(i.hasExtra("eventName"))
        {
            editing = true;//so we know we are in editing mode later
            eventID = i.getStringExtra("eventID");//for updating on the server

            //set the contents to the message being edited
            nameET.setText(i.getStringExtra("eventName"));
            descET.setText(i.getStringExtra("eventDesc"));

            //split datetime in to date and time
            String parts[] = i.getStringExtra("eventDate").split(" ");

            //these will be used later
            selectedDate = parts[0];
            selectedTime = parts[1];
        }
        else//otherwise set a date to current day and time to 1:00
        {
            Date d = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try
            {
                selectedDate = sdf.format(d);

                Log.d("TESTING", selectedDate);

            }
            catch(Exception e)
            {
                Log.d("TESTING", e.toString());
            }
        }

        dateTV.setText(selectedDate);
        timeTV.setText(selectedTime);


    }

    //toggle calendar overlay open or closed
    View.OnClickListener toggleCal = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(dateLayout.getVisibility() == View.GONE)
            {
                dateLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                dateLayout.setVisibility(View.GONE);
            }
        }
    };

    //toggle time picker overlay open or closed
    View.OnClickListener toggleClock = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(timeLayout.getVisibility() == View.GONE)
            {
                timeLayout.setVisibility(View.VISIBLE);
            }
            else
            {
                timeLayout.setVisibility(View.GONE);
            }
        }
    };

    //when date on calendar changed format it and update our variables/views
    CalendarView.OnDateChangeListener selectedDateChange = new CalendarView.OnDateChangeListener()
    {
        @Override
        public void onSelectedDayChange(@NonNull CalendarView v, int year, int month, int day)
        {

            String y = Integer.toString(year);
            String m = Integer.toString(month+1);
            String d = Integer.toString(day);

            selectedDate = y + "-" + m + "-" + d;
            dateTV.setText(selectedDate);
        }
    };


    //when time on picker changed format it and update our variables/views
    TimePicker.OnTimeChangedListener selectedTimeChange = new TimePicker.OnTimeChangedListener()
    {
        @Override
        public void onTimeChanged(TimePicker timePicker, int hour, int min)
        {
            String h = Integer.toString(hour);
            String m = Integer.toString(min);

            selectedTime = h + ":" + m + ":" + "00";
            timeTV.setText(selectedTime);
        }
    };

    //when submit clicked either create new event or update the existing with new data
    View.OnClickListener submit = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            String name = nameET.getText().toString();
            String desc = descET.getText().toString();

            //replace white space with %20 for the url
            name = name.replace(" ", "%20");
            desc = desc.replace(" ", "%20");

            //if all details are filled in
            if(!name.equals("") && !desc.equals("") && selectedDate != null && selectedTime != null)
            {
                Controller controller = new Controller(getApplicationContext());
                boolean created = false;

                //check if we are in editing mode and call the necessary method on the controller
                if(editing == true)
                {
                    created = controller.editEvent(eventID, name, desc, selectedDate, selectedTime);
                }
                else
                {
                    created = controller.createEvent(name, desc, selectedDate, selectedTime);
                }

                //when returned toast success or failure message
                if(created == true)
                {
                    Toast.makeText(getApplicationContext(), "Event created/updated", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(), Events.class));
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Error: Event not created/updated", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(), "Please Enter all the details", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onBackPressed()
    {
        //if the time or date layout is open when back is pressed hide them instead of going back.
        if(timeLayout.getVisibility() == View.VISIBLE || dateLayout.getVisibility() == View.VISIBLE)
        {
            timeLayout.setVisibility(View.GONE);
            dateLayout.setVisibility(View.GONE);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
