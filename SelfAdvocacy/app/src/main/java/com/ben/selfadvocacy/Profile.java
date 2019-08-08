package com.ben.selfadvocacy;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;


/*
 * Created by Ahmet_Celtik on 12/04/2018.
 */

public class Profile extends AppCompatActivity
{
    public String username, originalPassword, oldPasswordEntered, newPassword, newPassword2;
    public EnhancedTextView theUser;
    public String password;
    public EnhancedTextView fullnameView;
    public EnhancedButton changePassword;
    public EditText oldpass, newpass, newpass2;
    ImageView profileImage;

    final static int IMAGE_REQUEST = 1;
    String id;

    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        // TODO: A Remember me option, so when a user logs in once, they will not have to type in anything else

        // TODO: Login page where user will not be required to type, once they register

        controller = new Controller(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final Bundle user = getIntent().getExtras();
        //Controller controller = new Controller(getApplicationContext());
        String userDetails = controller.profileDisplay(user.getString("username"));

        String parts[] = userDetails.split(" ");
        id = parts[1];
        String firstName = parts[3];
        String surname = parts[4];

        String fullname = firstName + " " + surname;

        fullnameView = findViewById(R.id.fullName);
        fullnameView.setText(fullname);

        //TODO get image from server if exists

        if(user != null)
        {
            username = user.getString("username");
            originalPassword = user.getString("password");

            //greet user
            theUser = findViewById(R.id.userName);
            theUser.setText(String.format("Hello %s", username));
        }//end if()
        else
        {
            Log.d("User IS NULL", "--------------------!");
        }

        // change password of the user
        changePassword = findViewById(R.id.changePasswordButton);
        changePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                oldpass = findViewById(R.id.oldPassField);
                newpass = findViewById(R.id.newPassField);
                newpass2 = findViewById(R.id.newPassField2);

                // if any of the fields are empty
                if(oldpass.getText().toString().isEmpty()
                        || newpass.getText().toString().isEmpty()
                        || newpass2.getText().toString().isEmpty() )
                {
                    // if the fields are empty, create an AlertDialog to display error message
                    AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                    View mView = getLayoutInflater().inflate(R.layout.activity_fill_in_fields, null);

                    //create the dialog box using the mView
                    mBuilder.setView(mView);
                    final AlertDialog dialog = mBuilder.create();
                    dialog.show();

                    //find the okay buttons ID using mView, because its a custom Dialog box
                    EnhancedButton okay = mView.findViewById(R.id.fillFieldsButton);

                    okay.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View view)
                        {
                            dialog.dismiss();
                        }
                    });

                }//end if

                else
                {
                    // read in the values as numbers
                    int oldPass = Integer.parseInt(oldPasswordEntered = oldpass.getText().toString());
                    int newPass = Integer.parseInt(newPassword = newpass.getText().toString());
                    int newPass2 = Integer.parseInt(newPassword2 = newpass2.getText().toString());

                    // convert the passwords to Strings
                    oldPasswordEntered = String.valueOf(oldPass);
                    newPassword = String.valueOf(newPass);
                    newPassword2 = String.valueOf(newPass2);


                    // if the originalPassword does not match the password entered in by the user display error
                    if(!originalPassword.equals(oldPasswordEntered))
                    {

                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                        View mView = getLayoutInflater().inflate(R.layout.activity_old_pass_wrong, null);

                        //create the dialog box using the mView
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        //find the okay buttons ID using mView, because its a custom Dialog box
                        EnhancedButton okay = mView.findViewById(R.id.oldPassWrongButton);

                        okay.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                dialog.dismiss();
                            }
                        });//end clickListener

                    }//end inner if()

                    else if(!newPassword.equals(newPassword2))
                    {
                        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                        View mView = getLayoutInflater().inflate(R.layout.activity_pass_dont_match, null);

                        //create the dialog box using the mView
                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        //find the okay buttons ID using mView, because its a custom Dialog box
                        EnhancedButton okay = mView.findViewById(R.id.passDontMatchButton);

                        okay.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View view)
                            {
                                dialog.dismiss();
                            }
                        });

                    }//end inner else if()

                    else // the password fields are all correct and now proceed to update password
                    {

                        String username1 = user.getString("username");
                        String password1 = newPassword2;

                        boolean isChanged;
                        isChanged = controller.changePassword(username1, password1);
                        if (isChanged) {

                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(Profile.this);
                            View mView = getLayoutInflater().inflate(R.layout.activity_pass_success, null);

                            //create the dialog box using the mView
                            mBuilder.setView(mView);
                            final AlertDialog dialog = mBuilder.create();
                            dialog.show();

                            //find the okay buttons ID using mView, because its a custom Dialog box
                            EnhancedButton okay = mView.findViewById(R.id.passChangeSuccess);

                            okay.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    originalPassword = newpass.getText().toString();
                                    oldpass.setText("");
                                    newpass.setText("");
                                    newpass2.setText("");
                                    dialog.dismiss();
                                }
                            });//end okay.setOnClickListener()

                        }//end if()

                    }//end inner else()

                }//end outer else()

            }
        });// end changePassword.setOnClickListener()



        View.OnClickListener uploadImageListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i, IMAGE_REQUEST);
            }
        };

        profileImage = findViewById(R.id.imageView);
        profileImage.setOnClickListener(uploadImageListener);

        Bitmap pimg = controller.getProfileImg(id);
        if(pimg != null)
        {
            profileImage.setImageBitmap(pimg);
        }
    }//end onCreate()

    @Override
    protected void onActivityResult(int req, int res, Intent i) {
        if (req == IMAGE_REQUEST && res == RESULT_OK && i != null)
        {

            try
            {
                Uri imgUri = i.getData();
                Bitmap img = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                profileImage.setImageBitmap(img);
                String imgLoc = saveBitmap(img);

                controller.uploadImage(imgLoc, id);
            }
            catch(Exception e)
            {
                Log.d("POSTTEST", e.toString());
            }
        }
    }

    public String saveBitmap(Bitmap img)
    {
        UUID imageName = UUID.randomUUID();
        String id = imageName.toString();
        id = id.replace("-", "");

        File imgDir = new File(getFilesDir(), "profileImages");

        if (!imgDir.exists())
        {
            imgDir.mkdir();
        }

        try
        {
            File image = new File(imgDir, id);

            FileOutputStream os = new FileOutputStream(image);
            img.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.flush();
            os.close();
            return image.getAbsolutePath();
        }
        catch (Exception e)
        {
            Log.d("IMAGE", "IMAGE CATCH " + e.toString());
        }

        return null;
    }


    public void toastMessage(String message)
    {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


}//end Profile()
