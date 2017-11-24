package com.mannydev.testvkapp.view;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.GsonBuilder;
import com.mannydev.testvkapp.R;
import com.mannydev.testvkapp.WallActivity;
import com.mannydev.testvkapp.WebViewActivity;
import com.mannydev.testvkapp.model.Item;
import com.mannydev.testvkapp.model.videowithplayer.VideoWithPlayer;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    TextView textTitle, textDuration;
    ImageView imageVideo;
    public static Context ctx;

    public VideoViewHolder(View itemView) {
        super(itemView);
        ctx = itemView.getContext().getApplicationContext();

        textTitle = itemView.findViewById(R.id.textTitle);
        textDuration = itemView.findViewById(R.id.textDuration);
        imageVideo = itemView.findViewById(R.id.imageVideo);
    }

    public void bindVideoHolder(Item item){
        textTitle.setText(item.getVideo().getItems().get(0).getTitle());

        final String playerUrl = getVideoPlayerUrl(item);

        String duration = getDuration(item);
        textDuration.setText(duration);

        String imageUrl = item.getVideo().getItems().get(0).getPhoto320();
        Picasso.with(ctx).load(imageUrl).fit().centerCrop().into(imageVideo);
        imageVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetPlayer getPlayer = new GetPlayer();
                getPlayer.execute(playerUrl);
            }
        });
    }

    String getDuration(Item item){
        int dur = item.getVideo().getItems().get(0).getDuration();
        int hh = dur-dur%3600;
        int mm = (dur-dur%60)/60;
        int ss = dur%60;
        String hours;
        String minuts;
        String seconds;

        if(hh<10){
            hours = "0"+hh;
        }else hours= String.valueOf(hh);
        if(mm<10){
            minuts = "0"+mm;
        }else minuts= String.valueOf(mm);
        if(ss<10){
            seconds = "0"+ss;
        }else seconds= String.valueOf(ss);

        return hours+":"+minuts+":"+seconds;
    }

    String getVideoPlayerUrl(Item item){
        String OWNER_ID = item.getVideo().getItems().get(0).getOwnerId().toString();
        String ID = item.getVideo().getItems().get(0).getId().toString();
        String ACCES_KEY = item.getVideo().getItems().get(0).getAccessKey();
        String ACCES_TOKEN = WallActivity.accesToken;
        return OWNER_ID+"_"+ID+"_"+ACCES_KEY+"&access_token="+ACCES_TOKEN;
    }


}

class GetPlayer extends AsyncTask<String, Void, String> {

    @Override
    protected void onPreExecute() {}

    @Override
    protected String doInBackground(String... strings) {
        String url = strings[0];
        String videoWithPlayerJson = getVideoInJSON(url);
        return videoWithPlayerJson;
    }

    @Override
    protected void onPostExecute(String result) {
        GsonBuilder builder = new GsonBuilder();
        VideoWithPlayer videoWithPlayer = builder.create()
                .fromJson(result, VideoWithPlayer.class);
        String player = videoWithPlayer.getResponse().getItems().get(0).getPlayer();
        Intent intent = new Intent(VideoViewHolder.ctx, WebViewActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("url", player);
        VideoViewHolder.ctx.getApplicationContext().startActivity(intent);
    }

    private String getVideoInJSON(String myUrl) {
        StringBuilder response = new StringBuilder();
        String sourceUrl = "https://api.vk.com/method/video.get?v=5.69&videos="
                +myUrl;
        try {
            URL url = new URL(sourceUrl);
            HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
            if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader input;
                input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()));
                String strLine;
                while ((strLine = input.readLine()) != null) {
                    response.append(strLine);
                }
                input.close();
            }
            httpconn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

}