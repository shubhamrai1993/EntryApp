package com.example.entryapp;

import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.util.Strings;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    private ImageView mImageView;
    private Button mAcceptButton;
    private Button mRejectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = findViewById(R.id.imageView);
        mAcceptButton = findViewById(R.id.buttonLeft);
        mRejectButton = findViewById(R.id.buttonRight);

//        if (Strings.isEmptyOrWhitespace(PreferenceManager.getDefaultSharedPreferences(this).getString("USER_ID", ""))) {
        generateFirebaseRegistrationToken();
//        } else {
//            populateView();
//        }
    }

    private void generateFirebaseRegistrationToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }
                String token = task.getResult().getToken();
                // Log and toast
                String msg = "Your token: " + token;
                Log.d(TAG, msg);
                Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                registerUser(token);
            }
        });
    }

    private void registerUser(String token) {
        final User user = new User(token);

        RetrofitHttpClient.getPathServiceInstance().createUser(user).enqueue(new Callback<UserCreationResponse>() {
            @Override
            public void onResponse(Call<UserCreationResponse> call, Response<UserCreationResponse> response) {
                UUID generatedUserId = response.body().getUserId();
                SharedPrefsLocalManager.storeUserId(MainActivity.this, generatedUserId.toString());
                Log.d(MainActivity.class.getName(), "Generated userId: " + generatedUserId);
                populateView();
            }

            @Override
            public void onFailure(Call<UserCreationResponse> call, Throwable t) {
                Log.e(MainActivity.class.getName(), "Api call failed:", t);
            }
        });
    }

    private void populateView() {
        RetrofitHttpClient.getPathServiceInstance().getEntryRequest().enqueue(new Callback<EntryRequest>() {
            @Override
            public void onResponse(Call<EntryRequest> call, Response<EntryRequest> response) {
                EntryRequest entryRequest = response.body();
                if (entryRequest != null && entryRequest.getEntryRequestStatus().equals(EntryRequest.EntryRequestStatus.PENDING)) {
                    populateView(entryRequest);
                }
            }

            @Override
            public void onFailure(Call<EntryRequest> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Call to server for entryRequest failed", t);
            }
        });
    }

    private void populateDefaultView() {
        mImageView.setImageResource(R.drawable.ic_launcher_background);
        mAcceptButton.setOnClickListener(null);
        mRejectButton.setOnClickListener(null);
    }

    private void populateView(final EntryRequest entryRequest) {
        String imageUrl = entryRequest.getPictureUrl();
        Glide.with(this).load(imageUrl).into(mImageView);

        final UUID userId = UUID.fromString(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("USER_ID", ""));
        mAcceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitHttpClient.getPathServiceInstance().updateEntryRequest(entryRequest.getRequestId(), userId, EntryRequestStatusResponse.EntryRequestStatus.GRANTED).enqueue(new Callback<EntryRequestUpdateResponse>() {
                    @Override
                    public void onResponse(Call<EntryRequestUpdateResponse> call, Response<EntryRequestUpdateResponse> response) {
                        if (response.body().getUpdated()) {
                            Toast.makeText(MainActivity.this, "Entry granted", Toast.LENGTH_SHORT).show();
                            populateDefaultView();
                        } else {
                            Toast.makeText(MainActivity.this, "Entry granting failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EntryRequestUpdateResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Entry granting failed", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Call to server for entry granting failed", t);
                    }
                });
            }
        });

        mRejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RetrofitHttpClient.getPathServiceInstance().updateEntryRequest(entryRequest.getRequestId(), userId, EntryRequestStatusResponse.EntryRequestStatus.REJECTED).enqueue(new Callback<EntryRequestUpdateResponse>() {
                    @Override
                    public void onResponse(Call<EntryRequestUpdateResponse> call, Response<EntryRequestUpdateResponse> response) {
                        if (response.body().getUpdated()) {
                            Toast.makeText(MainActivity.this, "Entry rejected", Toast.LENGTH_SHORT).show();
                            populateDefaultView();
                        } else {
                            Toast.makeText(MainActivity.this, "Entry rejection failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<EntryRequestUpdateResponse> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Entry rejection failed", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Call to server for entry rejection failed", t);
                    }
                });
            }
        });
    }
}
