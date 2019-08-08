package com.ben.selfadvocacy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    private String password ="";
    private int counter =0;
    private Controller controller;
    EnhancedImageButton delete;
    TextView passwordBox;
    Bundle info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        controller = new Controller(this);

        passwordBox = findViewById(R.id.passwordText);
        delete = findViewById(R.id.delete);

        delete.setOnClickListener(deletePressed);

        findViewById(R.id.one).setOnClickListener(this);
        findViewById(R.id.two).setOnClickListener(this);
        findViewById(R.id.three).setOnClickListener(this);
        findViewById(R.id.four).setOnClickListener(this);
        findViewById(R.id.five).setOnClickListener(this);
        findViewById(R.id.six).setOnClickListener(this);
        findViewById(R.id.seven).setOnClickListener(this);
        findViewById(R.id.eight).setOnClickListener(this);
        findViewById(R.id.nine).setOnClickListener(this);

        info = getIntent().getExtras();
    }//end onCreate()

    @Override
    protected void onResume()
    {
        password = "";
        counter = 0;
        passwordBox.setText("");
        super.onResume();
    }

    View.OnClickListener deletePressed = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if(counter != 0)
            {
                counter--;
                password = password.substring(0, password.length()-1);
                //passwordBox.setText(password);
                passwordBox.setText(password);
            }
        }
    };

    public void onClick(View v)
    {

        //Concatenate a string of numbers that will make up the password and increment the counter
        password = password + v.getTag();
        String star = "****";
        passwordBox.setText(star);

        counter++;

        if(counter == 4 )
        {
            int pass = Integer.parseInt(password);//parse string password to an int
            boolean login = controller.UserLogin(pass); //try to log in the user
            //if the login attempt was successful go to next intent
            if(login)
            {
                if(info != null)
                {
                    String name = info.getString("username");

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(this, MainMenu.class);
                    i.putExtra("username", name);
                    String password = String.valueOf(pass);
                    i.putExtra("password", password);
                    startActivity(i);
                }//end if
            }
            else
            {//else restart the activity
                Toast.makeText(this, "Invalid Login", Toast.LENGTH_SHORT).show();
                Intent i = getIntent();
                finish();
                startActivity(i);
            }//end else()
        }//end if()
    }//end onClick()


}//end LoginScreen()
