package com.example.entryapp;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class EntryRequest {
    public enum EntryRequestStatus { PENDING, GRANTED, REJECTED };

    @SerializedName("requestId")
    private UUID requestId = UUID.randomUUID();
    @SerializedName("ttl")
    private long ttl;
    @SerializedName("pictureUrl")
    private String pictureUrl;
    @SerializedName("entryRequestStatus")
    private EntryRequestStatus entryRequestStatus = EntryRequestStatus.PENDING;
    @SerializedName("requestTimestamp")
    private long requestTimestamp = System.currentTimeMillis();

    public UUID getRequestId() {
        return requestId;
    }

    public long getTtl() {
        return ttl;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public EntryRequestStatus getEntryRequestStatus() {
        return entryRequestStatus;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }
}
