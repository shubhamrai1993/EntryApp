package com.example.entryapp;

import com.google.gson.annotations.SerializedName;

public class EntryRequestUpdateResponse {
    @SerializedName("updated")
    private Boolean updated;

    public Boolean getUpdated() {
        return updated;
    }
}
