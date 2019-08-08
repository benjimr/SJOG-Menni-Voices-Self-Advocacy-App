package com.ben.selfadvocacy;

import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class HumanRightsList extends AppCompatActivity {

    HumanRightsDao db = new HumanRightsDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_human_rights_list);

        try{
            db.clear();
            db.open();
            Toast.makeText(this,"added",Toast.LENGTH_LONG).show();
            db.addHumanRight("Right To Voice","To have a voice","I have the right:" +
                    "\nTo be listened to       " +
                    "\nTo be heard" +
                    "\nTo talk to others" +
                    "\nTo talk in private" +
                    "\nTo say what I am thinking" +
                    "\nTo communicate and be understood");
            db.addHumanRight("Responsibility","I have the responsibility","I have the responsibility:" +
                    "\nTo speak up for myself, to tell staff my needs." +
                    "\nTo communicate and be understood");
            db.addHumanRight("Responsibility 1","I have the responsibility","I have the responsibility:" +
                    "\nTo speak up for myself,to tell staff my needs." +
                    "\nTo say what I am thinking and feeling" +
                    "\nTo ask questions and give my opinion" +
                    "\nTo let people know if I am having problems" +
                    "\nTo respect others' views and opinions");
            db.addHumanRight("Right to know my Rights","I have the rights to know my rights","" +
                    "I have the right:" +
                    "\nTo have people understand the rights that are important to me." +
                    "\nTo have information on rights" +
                    "\nTo get help to understand my rights");
            db.addHumanRight("Responsibility 2","I have the responsibility","" +
                    "I have the responsibility:" +
                    "\nTo learn about my rights." +
                    "\nTo learn about what is expected of me" +
                    "\nTo learn about decision-making");
            db.addHumanRight("Right to Choose","I have the right to choose","I have the right to:" +
                    "\nWork" +
                    "\nDaily Activity" +
                    "\nLeisure"+
                    "\nReligion"+
                    "\nAccommodation"+
                    "\n");
            db.addHumanRight("Responsibility 3","I have the responsibility","I have the responsibility:" +
                    "\nTo state who it is that I feel comfortable talking to." +
                    "\nTo take part in meetings and discussions which affect my life." +
                    "\nTo say when I want to particular information to be kept private. ");
            db.addHumanRight("Right to freedom from abuse and neglect",
                                "I have the right to freedom from abuse and neglect",
                    "I have the right:" +
                            "\nTo be safe." +
                            "\nTo be free from physical,sexual,verbal or emotional abuse." +
                            "\nTo know what to do if I am abused/neglected." +
                            "\nTo be safe from being taken advantage of.");
            db.addHumanRight("Responsibility","I have the responsibility","" +
                            "\nTo be safe from being taken advantage of.");
            db.addHumanRight("Responsibility 4","I have the responsibility","" +
                    "I have the responsibility:" +
                    "\nTo take care of my personal well-being and safety" +
                    "\nTo be kind and considerate to others" +
                    "\nTo make known my particular safety needs or concerns");
            db.addHumanRight("Right to advocate","I have the right to advocate","" +
                    "I have the right to:" +
                    "\nTo have an advocate if I want one" +
                    "\nTo speak up for myself" +
                    "\nTo get help when I need to advocate" +
                    "\nTo choose someone who will speak up for me");
            db.addHumanRight("Responsibility 5","I have the responsibility to","" +
                    "I have the responsibility to:" +
                    "\nTo speak up for myself when I can" +
                    "\nTo respect other peoples' right to speak for themselves" +
                    "\nTo identify someone who will represent me if I am not able to speak for myself");
            db.addHumanRight("Right to a person centred plan","I have the right to a person centred plan","" +
                    "I have the right:" +
                    "\nTo have a plan stating my goals and the support I need" +
                    "\nTo be involved in decisions made about me" +
                    "\nTo be treated as an individual" +
                    "\nTo know, see and understand what is written about me" +
                    "\nTo give or not to give my consent" +
                    "\nTo have continuity of service" +
                    "\nTo have staff who are committed to me");
            db.addHumanRight("Responsibility 6","I have the responsibility to:","" +
                    "I have the responsibility:" +
                    "\nTo participate in the development of my personal plan" +
                    "\nTo do what I agree to in the plan" +
                    "\nTo use the supports provided to help me with my plan");
            db.addHumanRight("Right to access","I have the right to access","" +
                    "I have the right :" +
                    "\nTo be able to go to all public places easily" +
                    "\nTo on-going education and training" +
                    "\nTo travel independently" +
                    "\nTo use all services in the community" +
                    "\nTo information on anything I need, in a way that I can understand");
            db.addHumanRight("Responsibility 7","I have the responsibility ","" +
                    "I have the responsibility:" +
                    "\nTo learn the safety measures I need to take to keep safe" +
                    "\nTo take part in safety training if necessary" +
                    "\nTo let someone know of any difficulties I experience" +
                    "\nTo respect other members of the community" +
                    "\nTo have information I need to use community resources");
            db.addHumanRight("Right to friends and relationships","I have the right to friends and relationships","" +
                    "I have the right:" +
                    "\nTo have necessary supports to make and have friendships of my choice" +
                    "\nTo choose my friends" +
                    "\nTo have friends and family visit" +
                    "\nTo personal relationships");
            db.addHumanRight("Responsibility 8","I have the responsibility","" +
                    "I have the responsibility:" +
                    "\nTo behave in an appropriate manner" +
                    "\nTo respect the rights and privacy of others" +
                    "\nTo maintain my personal appearance" +
                    "\nTo take part in activities which encourage and maintain friendships" +
                    "\nTo choose my friends" +
                    "\nNot to bully or put pressure on other people");
            db.addHumanRight("Right to privacy","I have the right to privacy","" +
                    "I have the right:" +
                    "\nTo have my privacy needs known" +
                    "\nTo have my privacy respected at all times" +
                    "\nTo personal space" +
                    "\nTo my own space - for personal belongings" +
                    "\nTo spend time alone" +
                    "\nTo make and receive phone calls in private" +
                    "\nTo receive visitors" +
                    "\nTo receive and read my mail in private");
            db.addHumanRight("Responsibility","I have the responsibility","" +
                    "I have the responsibility:" +
                    "\nTo respect the privacy of others" +
                    "\nTo tell people my privacy needs" +
                    "\nTo respect my personal space" +
                    "\nTo take responsibility for my health and safety wen alone" +
                    "\nTo respect others' need for confidentiality");
            db.addHumanRight("Right to health care","I have the right to health care","" +
                    "I have the right:" +
                    "\nTo have all my health care needs known and met" +
                    "\nTo make appointments" +
                    "\nTo have my doctor talk to me" +
                    "\nTo have someone with me if I want to" +
                    "\nTo be told about my medication");
            db.addHumanRight("Responsibility 10","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo look after my health care needs" +
                    "\nTo ask for the support necessary to meet my health care needs");
            db.addHumanRight("Right to take an informed risk","I have the right to take an informed risk","" +
                    "I have the right:" +
                    "\nTo know what is involved in taking a risk" +
                    "\nTo get the information and advice I need in taking a risk" +
                    "\nTo be helped to understand what will,or could happen,If I take a risk" +
                    "\nTo be supported in my decisions");
            db.addHumanRight("Responsibility 11","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo understand and accept the risk that may be involved if I make certain choices or decisions" +
                    "\nTo act on agreed action to prevent risk" +
                    "\nTo follow rules and regulations that are in place");
            db.addHumanRight("Right to vote","I have the right to vote","" +
                    "I have the right:" +
                    "\nTo register to vote" +
                    "\nTo learn about who is going for election" +
                    "\nTo learn about the issues" +
                    "\nTo learn how to vote" +
                    "\nTo travel to vote");
            db.addHumanRight("Responsibility 12","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo learn why voting is important" +
                    "\nTo prepare to take part in the voting" +
                    "\nTo look for help to register and participate in the voting process, if necessary");
            db.addHumanRight("Right to meaningful daily activity","I have the right to meaningful daily activity","" +
                    "I have the right:" +
                    "\nTo work in a job of my choice" +
                    "\nTo try and learn new jobs" +
                    "\nTo be paid for a job" +
                    "\nTo choose how to spend my day" +
                    "\nTo have my needs and abilities supported and met");
            db.addHumanRight("Responsibility 13","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo take part in training and engage in employment, as agreed" +
                    "\nTo do my job/participate i training or other activity to the best of my ability");
            db.addHumanRight("Right to complain","I have the right to complain","" +
                    "I have the right:" +
                    "\nTo know what to do if I have a complaint" +
                    "\nTo be supported in making a complaint" +
                    "\nTo feel safe about making a complaint" +
                    "\nTo have professional people speak for me");
            db.addHumanRight("Responsibility 14","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo discuss any problem I have with staff before it becomes too serious" +
                    "\nTo understand that people may see things from different points of view" +
                    "\nTo listen to other people if they make a complaint about me" +
                    "\nTo learn about the steps I need to take if I want to make a complaint" +
                    "\nTo ask for support in making or dealing with a complaint");
            db.addHumanRight("Right to control my own money","I have the right to control my own money","" +
                    "I have the right:" +
                    "\nTo decide how to spend my money" +
                    "\nTo benefits/allowances" +
                    "\nTo a minimum wage" +
                    "\nTo have a post office/bank account" +
                    "\nTo have the help I need in managing my money");
            db.addHumanRight("Responsibility 15","I have the responsibility to","" +
                    "I have the responsibility:" +
                    "\nTo manage my money in accordance with my means and needs" +
                    "\nTo prioritise how I spend my money in an informed way" +
                    "\nTo ask for support if I need it to allow me to take care of my money");
            populateList();
            db.close();
        }
        //Else it catches an exception and displays an error
        catch (Exception e)
        {
            Toast.makeText(this,String.valueOf(e),Toast.LENGTH_LONG).show();
            Log.e("error", String.valueOf(e));
        }
    }

    //When this method is called the database is closed
    @Override
    protected void onDestroy()
    {
        db.close();
        super.onDestroy();
    }

    //A method that will populate the List
    public void populateList()
    {
        // call the method that will return a list
        //of all the Human rights in the database
        Cursor cursor = db.ListHumanRights();
        //Declare two parallel arrays that will act as a key value pair
        String[] rights = new String[] {db.COLUMN_HEADING, db.COLUMN_SHORT_DESC};//This Array will contain the values from the cursor we want displayed
        int[] viewIDs = new int[] {R.id.Heading,R.id.Desription};//THis array contains the view ID's where we want to display the cursor results
        //declare an adapter that will be used to display the information to the list View
        SimpleCursorAdapter myCursor = new SimpleCursorAdapter(this,R.layout.cust_row_hr,cursor,rights,viewIDs,0);
        //Get a reference to the list view in the activity
        ListView RightsList = (ListView)findViewById(R.id.HumanRightsList);
        //Apply the adapter to the list view
        RightsList.setAdapter(myCursor);

    }

    public void viewRight(View v){
        EnhancedTextView right = (EnhancedTextView)((View)v.getParent()).findViewById(R.id.Heading);
        String heading = right.getText().toString();
        Toast.makeText(this,heading,Toast.LENGTH_SHORT).show();

        Intent i = new Intent(this,ReadRight.class);
        i.putExtra("Right_Name",heading);
        startActivity(i);
    }

    public void SearchRight(View v){
        EditText searchInput = (EditText) findViewById(R.id.SearchInput);
        String search = searchInput.getText().toString();

        if(search != null && !search.isEmpty()) {
            try{
                db.open();
            }catch(Exception e){
                e.printStackTrace();
            }
            Cursor cursor = db.getRight(search);
            if(cursor != null) {
                //Declare two parallel arrays that will act as a key value pair
                String[] rights = new String[]{db.COLUMN_HEADING, db.COLUMN_SHORT_DESC};//This Array will contain the values from the cursor we want displayed
                int[] viewIDs = new int[]{R.id.Heading, R.id.Desription};//THis array contains the view ID's where we want to display the cursor results
                //declare an adapter that will be used to display the information to the list View
                SimpleCursorAdapter myCursor = new SimpleCursorAdapter(this, R.layout.cust_row_hr, cursor, rights, viewIDs, 0);
                //Get a reference to the list view in the activity
                ListView RightsList = (ListView) findViewById(R.id.HumanRightsList);
                //Apply the adapter to the list view
                RightsList.setAdapter(myCursor);
            }else{
                Toast.makeText(this,"No results found",Toast.LENGTH_SHORT).show();
                populateList();

            }
        }else{
            populateList();
        }

        Toast.makeText(this,search,Toast.LENGTH_SHORT).show();
    }
}
