package com.example.inclass_07;
/*
a. Assignment #. InClass07
b. File Name : ContactDetailsFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.inclass_07.databinding.FragmentContactDetailsBinding;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ContactDetailsFragment extends Fragment {

    IContactList iListener;
    private final OkHttpClient client = new OkHttpClient();
    FragmentContactDetailsBinding fragmentContactDetailsBinding;
    private static final String ARG_CONTACT = "CONTACT";

    public Contacts mContact;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    public static ContactDetailsFragment newInstance(Contacts contact) {
        ContactDetailsFragment fragment = new ContactDetailsFragment();
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
        fragmentContactDetailsBinding= FragmentContactDetailsBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_contact_detail));
        return fragmentContactDetailsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mContact!=null){
            fragmentContactDetailsBinding.tvCNameValue.setText(mContact.getName());
            fragmentContactDetailsBinding.tvCEmailValue.setText(mContact.getEmail());
            fragmentContactDetailsBinding.tvCPhoneValue.setText(mContact.getPhone());
            fragmentContactDetailsBinding.tvCTypeValue.setText(mContact.getType());
        }

        fragmentContactDetailsBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteContact(mContact.getId());
            }
        });

        fragmentContactDetailsBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iListener.goToUpdateContact(mContact);
            }
        });
    }

    public void deleteContact(String id){
        FormBody formBody= new FormBody.Builder()
                .add("id",id).build();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/contact/json/delete")
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if(response.isSuccessful()){
                    ResponseBody responseBody = response.body();
                    String responseValue= responseBody.string();
                    Log.d("TAG", "onResponse:delete"+responseValue);
                } else{
                    Log.d("TAG", "onResponse: failed:delete"+response.body());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),getString(R.string.label_contact_deleted),Toast.LENGTH_LONG).show();
                        iListener.goToContactListFromAdd();
                    }
                });
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