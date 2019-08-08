package com.ben.selfadvocacy;

/*
 * Created by benjm on 15-Mar-18.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class RemoteDAO
{


    private String ip = "35.230.136.76";
    private String port = "5000";
    private String link = "http://" + ip + ":" + port;
    private static Context context;
    private static boolean resultReceived = false;
    private static String result = null;
    private static Bitmap image = null;

    RemoteDAO(Context c)
    {
        context = c;
    }

    //Check if the user is an admin
    boolean isAdmin(String userName)
    {
        String url = link + "/isAdmin" + "/" + userName;

        if(checkConnection())
        {
            resultReceived = false;
            result = null;
            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(resultReceived == false || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("TRUE"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //check if a user exists in a system
    boolean checkUserExists(String userName)
    {
        String url = link + "/checkUserExists" + "/" + userName;

        if(checkConnection())
        {
            resultReceived = false;
            result = null;
            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //log a user in
    boolean login(String userName, int password)
    {
        String url = link + "/login" + "/" + userName + "/" + password;

        if (checkConnection() == true)
        {
            resultReceived = false;
            result = null;
            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(resultReceived == false || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //update version of register
    boolean newRegister(String username, String firstname, String surname, String password)
    {
        Log.e("RemoteDAO", username + firstname + surname );
        String url = link + "/newRegister" + "/" + username + "/" + firstname + "/" + surname +  "/" + password;

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }//end newRegister()

    //change password
    boolean changePass(String username, String newPass)
    {
        String url = link + "/changePassword/" + username + "/" + newPass;

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        return result.contains("SUCCESS");
    }

    //get a specific user
    String returnUser(String username)
    {
        String url = link + "/returnUsers/" + username;

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return null;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("FAILURE"))
        {
            return null;
        }

        Log.d("USERRRR", result);

        return result;

    }//end returnUser()


    //create a new event
    boolean createEvent(String name, String desc, String datetime)
    {
        Log.d("TESTING", name + " " + desc + " "+ datetime);

        String url = link + "/createEvent/" + name + "/" + desc + "/" + datetime;

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }//end createEvent()

    //edit an event
    boolean editEvent(String id, String name, String desc, String datetime)
    {
        String url = link + "/editEvent/" + id + "/"+ name + "/" + desc + "/" + datetime;

        Log.d("TESTING", url);
        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }//end editEvent()

    //delete an event
    boolean deleteEvent(String id)
    {
        String url = link + "/deleteEvent/" + id;

        if(checkConnection())
        {
            resultReceived = false;
            result = "";

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //downloads all of the future events on the server, no way of accessing past events,
    //also if there are a lot of event on slow connection it could take a while
    //could consider adding a paging system so it limits how many to download
    String getEvents()
    {
        String url = link + "/getEvents";

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return null;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("FAILURE"))
        {
            return null;
        }

        return result;
    }

    //get all users;
    String getUsers()
    {
        String url = link + "/getUsers";

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return null;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("FAILURE"))
        {
            return null;
        }

        return result;
    }

    //change password by admin
    Boolean changeUsersPassword(String selectedID, String newPassword)
    {
        String url = link + "/changeUsersPassword/" + selectedID + "/" + newPassword;

        if (checkConnection() == true)
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }

    }//end changeUsersPassword

    //delete user
    Boolean deleteUser(String selectedID)
    {
        String url = link + "/deleteUser/" + selectedID;

        if (checkConnection() == true)
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //toggle admin status on a user
    Boolean toggleAdmin(String selectedID)
    {
        String url = link + "/toggleAdmin/" + selectedID;

        if (checkConnection() == true)
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return false;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("SUCCESS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    //get list of videos
    String getVideos()
    {
        String url = link + "/getVideos";

        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessGet().execute(url);
        }
        else
        {
            return null;
        }

        while(!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        if(result.contains("FAILURE"))
        {
            return null;
        }

        return result;
    }

    //get users profile image
    Bitmap getProfileImg(String id)
    {
        String url = link + "/getProfileImg/" + id;

        if (checkConnection())
        {
            resultReceived = false;
            image = null;
            Log.d("IMGTEST", url);
            new DatabaseAccessGetFile().execute(url);
        }
        else
        {
            return null;
        }

        while(!resultReceived)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING","waiting...");
        }

        return image;
    }

    //talk to server in separate thread, executes simple get requests
    private static class DatabaseAccessGet extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);

                InputStreamReader isr = new InputStreamReader(url.openStream());
                BufferedReader br = new BufferedReader(isr);

                String r = "";
                String line;

                //store response in result
                while ((line = br.readLine()) != null)
                {
                    r += line;
                }

                isr.close();
                br.close();

                resultReceived = true;
                result = r;

                return r;
            }
            catch (IOException e)
            {
                return e.toString();
            }
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    //talk to server in separate thread, used to retrieve bitmaps
    private static class DatabaseAccessGetFile extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);

                HttpURLConnection client = (HttpURLConnection)url.openConnection();
                client.setDoInput(true);
                client.connect();

                InputStream fileIn = client.getInputStream();
                Bitmap img = BitmapFactory.decodeStream(fileIn);

                resultReceived = true;
                image = img;
                return "";
            }
            catch (Exception e)
            {
                Log.d("IMGTEST", e.toString());
                return e.toString();
            }
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }

    //upload image to the server
    boolean uploadImage(String file, String userID)
    {
        String url = link + "/uploadImage/" + userID;


        if (checkConnection())
        {
            resultReceived = false;
            result = null;

            new DatabaseAccessPost().execute(new String[]{url,file});
        }
        else
        {
            return false;
        }

        while (!resultReceived || result == null)//hold main thread until network thread updates resultReceived and result, not the best way to do this but it works for now.
        {
            Log.d("WAITING", "waiting...");
        }

        if (result.contains("SUCCESS")) {
            return true;
        }

        return false;

    }

    //used to upload images to the server
    private static class DatabaseAccessPost extends AsyncTask<String, Void, String>
    {
        protected String doInBackground(String... urls)
        {
            HttpURLConnection client;
            try
            {
                URL url = new URL(urls[0]);

                client = (HttpURLConnection) url.openConnection();
                client.setRequestMethod("POST");
                client.setDoOutput(true);
                client.setDoOutput(true);
                client.setUseCaches(false);
                client.connect();

                BufferedOutputStream fileOut = new BufferedOutputStream(client.getOutputStream());
                File file = new File(urls[1]);

                BufferedInputStream fileIn = new BufferedInputStream(new FileInputStream(file));

                byte[] buffer = new byte[1024];
                int len;

                while((len = fileIn.read(buffer)) != -1)
                {
                    fileOut.write(buffer,0, len);
                }

                InputStream respIn = client.getInputStream();
                BufferedReader respInBuf = new BufferedReader(new InputStreamReader(respIn));
                String line;
                StringBuffer resp = new StringBuffer();

                while((line = respInBuf.readLine())!= null)
                {
                    resp.append(line + "\n");
                }

                respIn.close();
                respInBuf.close();
                fileOut.flush();
                fileOut.close();
                fileIn.close();
                client.disconnect();
            }
            catch(Exception e)
            {
                Log.d("POSTTEST", e.toString());
            }

            String r = "";


            resultReceived = true;
            result = r;

            return r;
        }

        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }



    //ensure we have internet access
    private boolean checkConnection()
    {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
