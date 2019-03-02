package com.example.entryapp;

import com.google.gson.annotations.SerializedName;

public class EntryRequestStatusResponse {
    public enum EntryRequestStatus { PENDING, GRANTED, REJECTED };

    @SerializedName("entryRequestStatus")
    private EntryRequestStatus entryRequestStatus;

    public EntryRequestStatus getEntryRequestStatus() {
        return entryRequestStatus;
    }
}
