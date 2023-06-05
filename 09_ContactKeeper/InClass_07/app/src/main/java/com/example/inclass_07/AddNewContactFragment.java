package com.example.inclass_07;
/*
a. Assignment #. InClass07
b. File Name : AddNewContactFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inclass_07.databinding.FragmentAddNewContactBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class AddNewContactFragment extends Fragment {

    FragmentAddNewContactBinding binding;
    IContactList iListener;
    String missingField;
    private final OkHttpClient client = new OkHttpClient();

    public AddNewContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAddNewContactBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_add_new_contact));
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToContactList();
            }
        });


        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((binding.edNameValue.getText().toString().equals("")) && (binding.edEmailValue.getText().toString().equals("")) &&
                        (binding.edPhoneValue.getText().toString().equals("")) && (binding.edTypeValue.getText().toString().equals(""))) {
                    missingField = getString(R.string.label_all);
                } else {

                    if (binding.edNameValue.getText().toString().equals(""))
                        missingField = getString(R.string.label_name_hint);
                    else if (binding.edEmailValue.getText().toString().equals(""))
                        missingField = getString(R.string.label_email_hint);
                    else if (binding.edPhoneValue.getText().toString().equals(""))
                        missingField = getString(R.string.label_phone_hint);
                    else if (binding.edTypeValue.getText().toString().equals(""))
                        missingField = getString(R.string.label_type_hint);
                }

                if (missingField != null) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                    alertDialogBuilder.setMessage(getString(R.string.label_please) + missingField + getString(R.string.label_field));
                    alertDialogBuilder.setPositiveButton(getString(R.string.label_ok),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.dismiss();
                                }
                            });
                    alertDialogBuilder.show();
                    missingField = null;
                } else if (missingField == null) {

                    addContact(binding.edNameValue.getText().toString(), binding.edEmailValue.getText().toString()
                            , binding.edPhoneValue.getText().toString(), binding.edTypeValue.getText().toString());

                }
            }
        });


    }

    public void addContact(String name, String email, String phone, String type) {

        FormBody formBody = new FormBody.Builder()
                .add("name", name)
                .add("email", email)
                .add("phone", phone)
                .add("type", type)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/json/create")
                .post(formBody)
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                    ResponseBody responseBody = response.body();
                    String responseValue = responseBody.string();
                    if (response.isSuccessful()) {
                        Log.d("TAG", "onResponse: success" + responseValue);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getActivity(), getString(R.string.label_contact_added), Toast.LENGTH_LONG).show();
                                iListener.goToContactListFromAdd();

                            }
                        });

                    } else {
                        Log.d("TAG", "onResponse:" + responseValue);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IContactList) {
                iListener = (IContactList) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }
}