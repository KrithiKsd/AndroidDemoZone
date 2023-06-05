package com.example.inclass_07;
/*
a. Assignment #. InClass07
b. File Name : UpdateContactFragment.class
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

import com.example.inclass_07.databinding.FragmentUpdateContactBinding;

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

public class UpdateContactFragment extends Fragment {

    IContactList iListener;
    private final OkHttpClient client = new OkHttpClient();
    FragmentUpdateContactBinding binding;
    private static final String ARG_CONTACT = "CONTACT";
    String missingField;

    private Contacts mContact;

    public UpdateContactFragment() {
        // Required empty public constructor
    }

    public static UpdateContactFragment newInstance(Contacts contact) {
        UpdateContactFragment fragment = new UpdateContactFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_CONTACT, contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContact = (Contacts) getArguments().getSerializable(ARG_CONTACT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUpdateContactBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_contact_update));
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mContact != null) {
            binding.edNameValue.setText(mContact.getName());
            binding.edEmailValue.setText(mContact.getEmail());
            binding.edPhoneValue.setText(mContact.getPhone());
            binding.edTypeValue.setText(mContact.getType());
        }

        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToContactList();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((binding.edNameValue.getText().toString().equals("")) && (binding.edEmailValue.getText().toString().equals("")) &&
                        (binding.edPhoneValue.getText().toString().equals("")) && (binding.edTypeValue.getText().toString().equals(""))) {
                    missingField = getString(R.string.label_all);
                    Log.d("TAG", "onClick: misingField if" + missingField);
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
                .add("id", mContact.getId())
                .add("name", name)
                .add("email", email)
                .add("phone", phone)
                .add("type", type)
                .build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/json/update")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onResponse: Failure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {


                    try {
                        ResponseBody responseBody = response.body();
                        String responseValue = responseBody.string();
                        Log.d("TAG", "onResponse:Update" + responseValue);

                        JSONObject jsonObject = new JSONObject(responseValue);
                        Contacts contact = new Contacts();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("contact");

                        contact.setId(jsonObject1.getString("Cid"));
                        contact.setName(jsonObject1.getString("Name"));
                        contact.setEmail(jsonObject1.getString("Email"));
                        contact.setPhone(jsonObject1.getString("Phone"));
                        contact.setType(jsonObject1.getString("PhoneType"));
                        Log.d("TAG", "onResponse:After Update " + contact);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                iListener.goToContactListFromUpdate(contact);
                            }
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } else {
                        //No code required
                }
            }
        });

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