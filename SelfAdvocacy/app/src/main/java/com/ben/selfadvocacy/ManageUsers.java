package com.ben.selfadvocacy;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class ManageUsers extends AppCompatActivity
{
    LinearLayout overlay;
    ListView list;
    EnhancedButton changePassword, deleteUser, toggleAdmin;
    EnhancedEditText newPassword;
    String selectedID;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        //get user data
        final Controller controller = new Controller(getApplicationContext());
        List<Map<String,String>> data = controller.getUsers();

        //setup list
        list = findViewById(R.id.userList);
        list.setAdapter(new UserAdapter(getApplicationContext(), R.layout.user_row, data));

        //setup overlay
        overlay = findViewById(R.id.overlay);
        overlay.setVisibility(View.GONE);

        changePassword = findViewById(R.id.changePassword);
        deleteUser = findViewById(R.id.delete);
        toggleAdmin = findViewById(R.id.toggleAdmin);
        newPassword = findViewById(R.id.newPassword);

        changePassword.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String pw = newPassword.getText().toString();

                if(pw.length() == 4)
                {
                    controller.changeUsersPassword(selectedID, pw);
                    Intent i = new Intent(getApplicationContext(), ManageUsers.class);
                    finish();
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password must be 4 numbers", Toast.LENGTH_SHORT).show();
                }
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controller.deleteUser(selectedID);
                Intent i = new Intent(getApplicationContext(), ManageUsers.class);
                finish();
                startActivity(i);
            }
        });

        toggleAdmin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                controller.toggleAdmin(selectedID);
                Intent i = new Intent(getApplicationContext(), ManageUsers.class);
                finish();
                startActivity(i);
            }
        });
    }

    public class UserAdapter extends ArrayAdapter<Map<String, String>>
    {
        Context context;
        List<Map<String,String>> data;
        int rowLayout;

        public UserAdapter(Context c, int rl, List<Map<String,String>> d)
        {
            super(c, rl, d);
            this.context = c;
            this.data = d;
            this.rowLayout = rl;
        }

        public View getView(int pos, View view, ViewGroup vg)
        {
            if(view == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(rowLayout, vg, false);
            }

            //get data
            //get one users
            final Map<String,String> user = data.get(pos);

            String un = user.get("userName");
            String fn = user.get("firstName");
            String sn = user.get("surname");
            String full = fn + " " + sn;
            String ps = user.get("password");
            final String lv = user.get("level");

            //setup views
            EnhancedTextView userName, fullName, password, level;

            userName = view.findViewById(R.id.userName);
            fullName = view.findViewById(R.id.fullName);
            password = view.findViewById(R.id.password);
            level = view.findViewById(R.id.level);

            EnhancedButton edit = view.findViewById(R.id.editUser);

            edit.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if(Integer.parseInt(lv) == 1)
                    {
                        toggleAdmin.setText("Remove Admin Status");
                    }
                    else
                    {
                        toggleAdmin.setText("Grant Admin Status");
                    }

                    overlay.setVisibility(View.VISIBLE);
                    list.setVisibility(View.GONE);
                    selectedID = user.get("userID");
                }
            });



            //format
            userName.setText(un);
            fullName.setText(full);
            password.setText(ps);

            if(Integer.parseInt(lv) == 1)
            {
                level.setText("Administrator");
            }
            else
            {
                level.setText("Regular");
            }

            return view;
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
}
