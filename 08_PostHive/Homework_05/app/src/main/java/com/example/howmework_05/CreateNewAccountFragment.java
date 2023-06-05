package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: CreateNewAccountFragment.java
c. Full name of the student : Krithika Kasaragod
*/
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.howmework_05.databinding.FragmentCreateNewAccountBinding;
import com.example.howmework_05.databinding.FragmentLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class CreateNewAccountFragment extends Fragment {


    FragmentCreateNewAccountBinding createAccountBinding;
    String missingField;
    private final OkHttpClient client = new OkHttpClient();
    IServices mListener;

    public CreateNewAccountFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        createAccountBinding = FragmentCreateNewAccountBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_create_new_account_fragment));
        return createAccountBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        createAccountBinding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fullName = createAccountBinding.editTextName.getText().toString();
                String emailName = createAccountBinding.editTextEmailAddress.getText().toString();
                String password = createAccountBinding.editTextPassword.getText().toString();
                boolean success = validation(fullName, emailName, password);
                if (success) {
                    createNewAccount(fullName, emailName, password);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle(getString(R.string.label_please_fill_data))
                            .setMessage(getString(R.string.label_enter) + " " + missingField)
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

        createAccountBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Implementation of cancel button
                mListener.cancel();
            }
        });
    }

    private void createNewAccount(String fullName, String emailName, String password) {
        FormBody formBody = new FormBody.Builder()
                .add("email", emailName)
                .add("password", password)
                .add("name", fullName).build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/posts/signup")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure:******************* While register");
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                ResponseBody responseBody = response.body();
                String responseValue = responseBody.string();
                if (response.isSuccessful()) {
                    Log.d("TAG", "onResponse: Success" + responseValue);
                    DataService.LoginDetails loginDetails = new DataService.LoginDetails();

                    try {
                        JSONObject jsonObject = new JSONObject(responseValue);
                        loginDetails.setStatus(jsonObject.getString("status"));
                        loginDetails.setToken(jsonObject.getString("token"));
                        loginDetails.setUserId(jsonObject.getString("user_id"));
                        loginDetails.setUserFullName(jsonObject.getString("user_fullname"));

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListener.gotoPostListFromRegister(loginDetails);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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

    private boolean validation(String fullName, String emailName, String password) {
        if (fullName.isEmpty() && emailName.isEmpty() && password.isEmpty()) {
            missingField = getString(R.string.label_full_name_email_password_field);
            return false;
        } else if (fullName == null || fullName.isEmpty()) {
            missingField = getString(R.string.label_full_name_field);
            return false;
        } else if (fullName.length() > 20) {
            missingField = getString(R.string.label_valid_name);
            return false;
        } else if (emailName == null || emailName.isEmpty()) {
            missingField = getString(R.string.label_email_field);
            return false;
        } else if (!validEmailPattern(emailName)) {
            missingField = getString(R.string.label_proper_email);
            return false;
        } else if (password == null || password.isEmpty()) {
            missingField = getString(R.string.label_password_field);
            return false;
        }
        return true;
    }

    private boolean validEmailPattern(String email) {
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(email);
        return emailMatcher.matches();
    }
}