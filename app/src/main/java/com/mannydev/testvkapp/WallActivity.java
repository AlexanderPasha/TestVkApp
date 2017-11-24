package com.mannydev.testvkapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.gson.GsonBuilder;
import com.mannydev.testvkapp.model.Item;
import com.mannydev.testvkapp.model.VideoResponse;
import com.mannydev.testvkapp.view.VideoViewAdapter;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKScopes;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;



public class WallActivity extends AppCompatActivity {
    String[] scope = new String[]{VKScopes.WALL, VKScopes.PHOTOS, VKScopes.FRIENDS, VKScopes.VIDEO};

    public static final String APP_MEM = "app";
    public static final String TOKEN = "aToken";

    @BindView(R.id.rvVideos)
    RecyclerView rvVideos;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.progressBar2)
    ProgressBar progressBar2;
    SharedPreferences sharedPreferences;
    VideoViewAdapter adapter;
    public static String accesToken;
    List<Item> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        sharedPreferences = getSharedPreferences(APP_MEM,MODE_PRIVATE);
        accesToken = sharedPreferences.getString(TOKEN,null);

        progressBar2.setVisibility(View.INVISIBLE);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        rvVideos.setLayoutManager(mLayoutManager);

        items = new ArrayList<>();

        adapter = new VideoViewAdapter();
        adapter.setData(items);

        VKSdk.initialize(this);

        VKRequest vkRequest = new VKRequest("newsfeed.get", VKParameters.from(VKApiConst.FILTERS, "video", VKApiConst.COUNT, 100));
        vkRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                progressBar2.setVisibility(View.INVISIBLE);
                GsonBuilder builder = new GsonBuilder();
                VideoResponse videoResponse = builder.create()
                        .fromJson(String.valueOf(response.json), VideoResponse.class);
                List<Item> newItems = videoResponse.getResponse().getItems();
                for (Item newItem : newItems) {
                    items.add(newItem);
                }
                adapter.setData(items);
                rvVideos.setAdapter(adapter);
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                VKSdk.logout();
                Intent intent = new Intent(this,MainActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

}

