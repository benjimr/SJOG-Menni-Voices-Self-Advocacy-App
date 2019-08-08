package com.ben.selfadvocacy;

import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginStepOne extends AppCompatActivity
{
    private EnhancedButton register, next;
    private Controller controller;


    private CheckBox saveLoginCheckBox;

    public EnhancedEditText userInput;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step_one);
        controller = new Controller(this);

        prefs = getApplicationContext().getSharedPreferences("prefs", getApplicationContext().MODE_PRIVATE);

        register = findViewById(R.id.RegisterButton);
        next = findViewById(R.id.NextButton);
        saveLoginCheckBox = findViewById(R.id.saveLoginCheckBox);
        userInput = findViewById(R.id.NameInput);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPressed(view);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nextPressed(view);
            }
        });

        if(prefs.contains("saveLogin"))
        {
            if(prefs.getBoolean("saveLogin",false))
            {
                userInput.setText(prefs.getString("username",""));
                saveLoginCheckBox.setChecked(true);
            }
        }

    }//end onCreate()
    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void nextPressed(View v)
    {

        String userName = userInput.getText().toString();
        if(userInput.getText().toString().isEmpty())
        {
            toastMessage("Enter your Username!");
        }
        else
        {
            boolean UserAuth = controller.UserAuthentication(userName);

            //If the username is found in the database go to next activity
            if(UserAuth)
            {

                SharedPreferences.Editor edit = prefs.edit();

                edit.putString("username", userName);

                //if they want to remember
                if(saveLoginCheckBox.isChecked())
                {
                    edit.putBoolean("saveLogin", true);
                }
                else
                {
                    edit.clear();
                    edit.commit();
                    edit.remove("saveLogin");

                }

                edit.apply();

                Intent i = new Intent(this,LoginScreen.class);
                i.putExtra("username", userName);
                Toast.makeText(this,"UserFound",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
            else
            {
                Toast.makeText(this, "Username Not Found.", Toast.LENGTH_LONG).show();
                //If username is wrong restart activity
                //not sure if it actually works
                Intent i = getIntent();
                finish();
                startActivity(i);
            }//end else()

        }//end else()

    }//end nextPressed()

    public void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void registerPressed(View v)
    {
        Intent i = new Intent(getApplicationContext(),Register.class);
        startActivity(i);
    }
}//end LoginStepOne
