package com.ben.selfadvocacy;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Created by Asus on 20/03/2018.
 */

class Controller {
    private RemoteDAO database;
    Context context;

    //constructor for the controller class
    Controller(Context ctx)
    {
        this.context = ctx;
        database = new RemoteDAO(context);
    }

    //Connects to the database, checks if the user exists in the database and returns true or false
    //depending on the result
    boolean UserAuthentication(String username)
    {
        database = new RemoteDAO(context);
        boolean Authentication = database.checkUserExists(username);

        if(Authentication){
            //save username to shared prefs so it can be used later
            SharedPreferences prefs = context.getSharedPreferences("prefs", context.MODE_PRIVATE);
            SharedPreferences.Editor edit = prefs.edit();

            edit.putString("username", username);
            edit.commit();
            return true;
        }else{
            return false;
        }
    }//end UserAuthenti
    boolean UserLogin(int password)
    {
        //get username to be passed into the login method
        SharedPreferences prefs = context.getSharedPreferences("prefs", context.getApplicationContext().MODE_PRIVATE);
        String userName = prefs.getString("username", "default");

        //on login check is the user is an admin, and set the result(true or false) in shared preferences so we can use it later
        boolean isAdmin = isAdmin(userName);

        SharedPreferences.Editor edit = prefs.edit();
        edit.putBoolean("isAdmin",isAdmin);
        edit.commit();

        boolean LoginAuthentication = database.login(userName,password);

        if(LoginAuthentication)
        {
            return true;
        }else {
            return false;
        }
    }

    //check if the user passed in is an admin                   check admin
    boolean isAdmin(String userName)
    {
        boolean adminAuthentication = database.isAdmin(userName);

        if(adminAuthentication)
        {
            return true;
        }
        else
        {
            return false;
        }
    }//end isAdmin()



    boolean newRegister(String username, String firstname, String surname,  String password)
    {
        Log.e("Controller", username + firstname + surname );
        boolean result = database.newRegister(username, firstname, surname, password);

        if(result)
        {
            return true;
        }
        else
        {
            return false;
        }

    }//end newRegister()


    String profileDisplay(String username)
    {
        return database.returnUser(username);
    }//end profileDisplay()


    boolean changePassword(String username, String pass)      // ---------------------------------change password----------------------------------
    {
        return database.changePass(username, pass);

    }//end changePassword()




    //creates an event
    boolean createEvent(String name, String desc, String date, String time)
    {
        String datetime = date + "%20" + time;
        return database.createEvent(name, desc, datetime);
    }

    boolean deleteEvent(String id)
    {
        return database.deleteEvent(id);
    }

    //edits an existing event
    boolean editEvent(String id, String name, String desc, String date, String time)
    {
        String datetime = date + "%20" + time;
        return database.editEvent(id, name, desc, datetime);
    }

    //retrieves future event datand formats it in a way that makes it easier to display
    List<Map<String,String>> getEvents()
    {
        //get the result from the server
        String result = database.getEvents();//in json formatted string

        Log.d("TESTING"," " + result);

        try
        {
            //convert to json array
            JSONArray json = new JSONArray(result);

            //iterate through converting into list<Map<String,String>
            List<Map<String,String>> data = new ArrayList<>();

            //loop through json array
            for(int i = 0; i < json.length(); i++)
            {
                Map<String,String> mapEvent = new HashMap<>();
                JSONObject jsonEvent = json.getJSONObject(i);

                //put a single event data in to a map
                mapEvent.put("eventID", jsonEvent.getString("eventID"));
                mapEvent.put("eventName", jsonEvent.getString("eventName"));
                mapEvent.put("eventDesc", jsonEvent.getString("eventDesc"));
                mapEvent.put("eventDate", jsonEvent.getString("eventDate"));

                //add map to the list which will be used in the adapter
                data.add(mapEvent);
            }

            //sort by date before returning , simple bubble sort
            for(int i = 0; i < data.size(); i++)
            {
                for(int j = 1; j < (data.size()-i); j ++)
                {
                    //save some variables to make swapping/iterating easier
                    int pos1 = j-1;
                    int pos2 = j;

                    Map<String,String> event1 = data.get(pos1);
                    Map<String,String> event2 = data.get(pos2);

                    String date1 = event1.get("eventDate");
                    String date2 = event2.get("eventDate");

                    //convert dates from strings to Date types to make comparing easier
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    Date d1 = null;
                    Date d2 = null;

                    try
                    {
                        d1 = sdf.parse(date1);
                        d2 = sdf.parse(date2);
                    }
                    catch(Exception e)
                    {
                        Log.d("TESTING",e.toString());
                    }

                    //if the first date is after the second date swap the events
                    if(d1.after(d2))
                    {
                        data.set(pos1,event2);
                        data.set(pos2,event1);
                    }
                }
            }

            return data;//the formatted and sorted List<Map<String,String>>
        }
        catch(JSONException e)
        {
            Log.d("JSONException", e.toString());
            return null;
        }
    }

    List<Map<String,String>> getUsers()
    {
        String result = database.getUsers();

        try
        {
            JSONArray json = new JSONArray(result);
            List<Map<String,String>> data = new ArrayList<>();

            for(int i = 0; i < json.length(); i++)
            {
                JSONObject jsonUser = json.getJSONObject(i);
                Map<String,String> mapUser = new HashMap<>();

                Log.d("TESTING",jsonUser.getString("userName") );

                mapUser.put("userID", jsonUser.getString("userID"));
                mapUser.put("userName", jsonUser.getString("userName"));
                mapUser.put("firstName", jsonUser.getString("firstName"));
                mapUser.put("surname", jsonUser.getString("surname"));
                mapUser.put("password", jsonUser.getString("password"));
                mapUser.put("level", jsonUser.getString("level"));

                data.add(mapUser);
            }

            return data;
        }
        catch(Exception e)
        {
            Log.d("TESTING", e.toString());
            return null;
        }
    }

    Boolean changeUsersPassword(String selectedID, String newPassword)
    {
        return database.changeUsersPassword(selectedID, newPassword);
    }

    Boolean deleteUser(String selectedID)
    {
        return database.deleteUser(selectedID);
    }

    Boolean toggleAdmin(String selectedID)
    {
        return database.toggleAdmin(selectedID);
    }

    List<Map<String,String>> getVideos()
    {
        /* WORKING VERSION TO GET JUST IDS
        String result = database.getVideos();
        List<String> ids = new ArrayList<>();
        String parts[] = result.split(" ");

        for(int i = 0; i < parts.length; i++)
        {
            ids.add(parts[i]);
        }

        return ids;
       */
        try
        {
            String result = database.getVideos();
            JSONArray json = new JSONArray(result);

            List<Map<String,String>> videos = new ArrayList<>();
            for(int i = 0; i < json.length(); i++)
            {
                JSONObject obj = json.getJSONObject(i);
                Map<String,String> video = new HashMap<>();

                video.put("name", obj.getString("name"));

                String link = obj.getString("link");

                //2 types of links returned, need to edit one of them
                if(!link.contains("sjog"))
                {
                    link = link.substring(0,8) + "player." + link.substring(8,18) + "video/" + link.substring(18,link.length());
                }

                video.put("link", link);

                String pictures = obj.getString("pictures");
                JSONObject pics = new JSONObject(pictures);
                JSONArray sizes = pics.getJSONArray("sizes");
                JSONObject img = sizes.getJSONObject(0);
                String imgLink = img.getString("link");
                video.put("imgLink", imgLink);
                videos.add(video);
            }

            return videos;
        }
        catch(Exception e)
        {
            Log.d("VIDEOS", e.toString());
        }

        return null;
    }

    public Boolean uploadImage(String filePath, String userID)
    {
        return database.uploadImage(filePath, userID);
    }

    public Bitmap getProfileImg(String id)
    {
        return database.getProfileImg(id);
    }
}
