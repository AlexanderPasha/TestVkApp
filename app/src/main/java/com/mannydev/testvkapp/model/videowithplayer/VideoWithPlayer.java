
package com.mannydev.testvkapp.model.videowithplayer;

import com.google.gson.annotations.SerializedName;
import com.google.gson.annotations.Expose;

public class VideoWithPlayer {

    @SerializedName("response")
    @Expose
    private Response response;

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

}
