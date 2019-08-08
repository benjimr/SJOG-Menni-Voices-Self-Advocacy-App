package com.ben.selfadvocacy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VideoList extends AppCompatActivity
{
    List<String> links = new ArrayList<>();
    static List<Bitmap> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        Controller controller = new Controller(getApplicationContext());
        List<Map<String,String>> videos = controller.getVideos();

        for(int i = 0; i < videos.size(); i++)
        {
            Map<String,String> video = videos.get(i);

            String name = video.get("name");
            String link = video.get("link");
            String imgLink = video.get("imgLink");

            titles.add(name);
            links.add(link);
            new getBitmaps().execute(imgLink);

        }

        while(images.size() != links.size())
        {
            Log.d("VIDEOS", "IMAGE:  " + images.size() + "LINKS:    " + links.size());
        };

        Log.d("VIDEOS", "after while");
        ListView list = findViewById(R.id.list);
        ListAdapter la = new ListAdapter(getApplicationContext(),R.layout.videorow,titles);
        list.setAdapter(la);

        ListView.OnItemClickListener listListener = new ListView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View v, int pos, long id)
            {
                String link = links.get(pos);
                Intent i = new Intent(getApplicationContext(), DisplayVideo.class);
                i.putExtra("link", link);
                startActivity(i);
            }
        };

        ListView.OnItemLongClickListener listLongListener = new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long id)
            {
                TextToSpeech tts = TTS.getTTS(getApplicationContext());
                String text = titles.get(pos);
                tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                return true;
            }
        };

        list.setOnItemClickListener(listListener);
        list.setOnItemLongClickListener(listLongListener);
    }

    public class ListAdapter extends ArrayAdapter<String>
    {
        List<String> data;
        Context context;

        public ListAdapter(Context c, int rowLayout, List<String> data)
        {
            super(c, rowLayout, data);
            context = c;
            this.data = data;
        }

        public View getView(int pos, View view, ViewGroup vg)
        {
            if(view == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                view = inflater.inflate(R.layout.videorow, vg,false);
            }

            String text = data.get(pos);
            Bitmap image = images.get(pos);

            ImageView dispImg = view.findViewById(R.id.image);
            TextView dispName = view.findViewById(R.id.name);

            dispName.setText(text);
            dispImg.setImageBitmap(image);

            return view;
        }
    }

    //get Bitmap for the image thumbnail
    private static class getBitmaps extends AsyncTask<String, Void, Bitmap>
    {
        protected Bitmap doInBackground(String... urls)
        {
            try
            {
                URL url = new URL(urls[0]);
                Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                Log.d("VIDEOS", "after decode");
                images.add(image);
                return image;
            }
            catch (Exception e)
            {
                Log.d("VIDEOS", e.toString());
                return null;
            }
        }

        protected void onPostExecute(Bitmap image)
        {
            super.onPostExecute(image);

        }
    }
}
