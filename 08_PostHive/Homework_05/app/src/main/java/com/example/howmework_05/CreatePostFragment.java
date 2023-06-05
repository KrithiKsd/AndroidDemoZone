package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: CreatePostFragment.java
c. Full name of the student : Krithika Kasaragod
*/
import static com.example.howmework_05.MainActivity.myPreference;
import static com.example.howmework_05.MainActivity.tokenId;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.howmework_05.databinding.FragmentCreatePostBinding;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class CreatePostFragment extends Fragment {


    FragmentCreatePostBinding createPostBinding;
    IServices mListener;
    private final OkHttpClient client = new OkHttpClient();
    String errorMessage="";

    public CreatePostFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createPostBinding = FragmentCreatePostBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_create_post_fragment));
        return createPostBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createPostBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Implementation of cancel button
                mListener.cancel();
            }
        });

        createPostBinding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String post = createPostBinding.etEnterPost.getText().toString();

                //Validations and creation of post in API
                boolean success = validation(post);
                if(success){
                    SharedPreferences sharedpreferences = getActivity().getSharedPreferences(myPreference,
                            Context.MODE_PRIVATE);
                    if (sharedpreferences.contains(tokenId)) {
                        String tokenIdValue = sharedpreferences.getString(MainActivity.tokenId, null);
                        createPostAPI(post, tokenIdValue);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_please_fill_data))
                            .setMessage(errorMessage)
                            .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                    builder.show();
                }
            }
        });

    }

    private boolean validation(String post) {
        if (post == null || post.isEmpty()) {
            errorMessage=getString(R.string.label_enter_post);
            return false;
        }else if(post.length()>140){
            errorMessage=getString(R.string.error_post_length);
            return false;
        }
        return true;
    }

    private void createPostAPI(String post, String tokenIdValue) {
        Log.d("TAG", "createPostAPI: TOKEN*** " + tokenIdValue);
        FormBody formBody = new FormBody.Builder()
                .add("post_text", post).build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/create")
                .addHeader("Authorization", "BEARER " + tokenIdValue)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure:******************* While login");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: Success" + responseValue);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //successful creation of post pop from back stack from UI thread
                            mListener.refreshPostList();
                        }
                    });

                } else {
                    Log.d("TAG", "onResponse: Failure" + responseValue);
                    try {
                        JSONObject jsonObject = new JSONObject(responseValue);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                builder.setTitle(status)
                                        .setMessage(message)
                                        .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                            }
                                        });
                                builder.show();
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IServices) {
                mListener = (IServices) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {
        }
    }
}