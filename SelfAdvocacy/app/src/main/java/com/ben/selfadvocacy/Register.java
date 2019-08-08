package com.ben.selfadvocacy;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class Register extends AppCompatActivity
{
    private EditText username, firstname, surname,  password, password2;

    private Controller controller;
    private boolean result;

    private String uName, fName, sName, pass, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerr);
        controller = new Controller(this);
        username = (EnhancedEditText) findViewById(R.id.usernameField);
        firstname = (EnhancedEditText) findViewById(R.id.nameField);
        surname = (EnhancedEditText) findViewById(R.id.surnameField);
        password = (EnhancedEditText) findViewById(R.id.passField);
        password2 = (EnhancedEditText) findViewById(R.id.passField2);

        //statusMessage = (EnhancedTextView) findViewById(R.id.statusMessage);

        //check if the REGISTER button is pressed

        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //check if the fields are empty or not
                if(username.getText().toString().isEmpty()
                        || firstname.getText().toString().isEmpty()
                        || surname.getText().toString().isEmpty()
                        || password.getText().toString().isEmpty()
                        || password2.getText().toString().isEmpty())
                {
                    toastMessage("Please fill in all fields!");
                }//end if
                else
                {
                    uName = username.getText().toString();
                    fName = firstname.getText().toString();
                    sName = surname.getText().toString();
                    pass = password.getText().toString();
                    pass2 = password2.getText().toString();

                    Log.e("Register-------", uName + fName + sName );

                    int length = pass.length();

                    if(pass.equals(pass2))
                    {
                        if(length < 4 || length > 4)
                        {
                            toastMessage("Password must be 4 digits only.");
                        }
                        else
                        {
                            // inserting the user into database through controller
                            result = controller.newRegister(uName, fName, sName, pass);
                            if(result)
                            {
                                toastMessage("Registration successful!");
                                Intent i = new Intent(Register.this, LoginStepOne.class);
                                startActivity(i);
                                finish();
                            }
                            else
                            {
                                toastMessage("Registration Failed.");
                            }


                        }//end else()

                    }//end if()

                    else
                    {
                        toastMessage("Passwords do not match!");
                    }//end else()

                }//end outer else()

            }//end onClick()
        });

        Button backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                username.setText("");
                firstname.setText("");
                surname.setText("");
                password.setText("");
                password2.setText("");

                Intent i = new Intent(Register.this, LoginStepOne.class);
                startActivity(i);
                finish();

            }
        });



    }//end onCreate()


    /*
    // OLD REGISTER FUNCTIONALITY
    public void padClicked(View v)
    {
        password = password + v.getTag();
        int len = password.length();

        if(len == 6)
        {
            username = userInput.getText().toString();

            if(username.equals(null))
            {
                Toast.makeText(this,"Please enter your username",Toast.LENGTH_SHORT).show();
            }
            else
            {
                result = controller.UserRegister(username,Integer.parseInt(password));

                if(result == true)
                {
                    Intent i = new Intent(this,LoginStepOne.class);
                    Toast.makeText(this,"Successfully Registered",Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }else{
                    Toast.makeText(this, "Error Registering", Toast.LENGTH_SHORT).show();
                    Intent i = getIntent();
                    finish();
                    startActivity(i);
                }

            }
        }
    }

    //END OLD REGISTER
    */

    public void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }






}
