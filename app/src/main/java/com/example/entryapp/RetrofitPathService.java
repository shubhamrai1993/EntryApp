package com.example.entryapp;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitPathService {
    @PUT("users")
    Call<UserCreationResponse> createUser(@Body User user);

    @GET("entryRequest")
    Call<EntryRequest> getEntryRequest();

    @POST("entryRequest/{entryRequestId}/users/{userId}/{entryRequestStatus}")
    Call<EntryRequestUpdateResponse> updateEntryRequest(
            @Path("entryRequestId") UUID entryRequestId,
            @Path("userId") UUID userId,
            @Path("entryRequestStatus") EntryRequestStatusResponse.EntryRequestStatus entryRequestStatus
    );
}
