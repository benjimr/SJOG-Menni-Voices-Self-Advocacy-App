package com.ben.selfadvocacy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class AdministratorTools extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator_tools);

        findViewById(R.id.createEvent).setOnClickListener(goToCreateEvent);
        findViewById(R.id.manageUsers).setOnClickListener(goToManageUsers);
    }

    View.OnClickListener goToCreateEvent = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(getApplicationContext(), CreateEvent.class);
            startActivity(i);
        }
    };

    View.OnClickListener goToManageUsers = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            Intent i = new Intent(getApplicationContext(), ManageUsers.class);
            startActivity(i);
        }
    };
}
