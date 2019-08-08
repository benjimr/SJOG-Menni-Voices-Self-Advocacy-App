package com.ben.selfadvocacy;

import android.database.Cursor;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.SQLException;

public class ReadRight extends AppCompatActivity {

    HumanRightsDao db= new HumanRightsDao(this);
    EnhancedTextView heading , description;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_right);

        heading = (EnhancedTextView) findViewById(R.id.Right_Heading);
        description = (EnhancedTextView)findViewById(R.id.Rights_Desc);

        String rightToDisplay = getIntent().getStringExtra("Right_Name");
        try{
            db.open();
            Cursor cursor = db.getRight(rightToDisplay);
            if(cursor.moveToFirst()){
                String RightHeading = cursor.getString(1);
                String RightDesc = cursor.getString(3);
                heading.setText(RightHeading);
                description.setText(RightDesc);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
