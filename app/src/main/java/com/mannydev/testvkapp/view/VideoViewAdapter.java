package com.mannydev.testvkapp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mannydev.testvkapp.R;
import com.mannydev.testvkapp.model.Item;

import java.util.List;


public class VideoViewAdapter extends RecyclerView.Adapter<VideoViewHolder> {
    private List<Item>list;

    public VideoViewAdapter(){
    }

    public void setData(List<Item> list) {
        this.list = list;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video_view, parent, false);
        return new VideoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        holder.bindVideoHolder(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
