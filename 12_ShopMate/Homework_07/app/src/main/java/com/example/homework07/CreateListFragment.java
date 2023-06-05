package com.example.homework07;

/*
Assignment #: Homework07
File Name: CreateListFragment.java
Full Name of Student2: Krithika Kasaragod
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.homework07.databinding.FragmentCreateListBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class CreateListFragment extends Fragment {

    public ArrayList<String> invitedUsers;
    FragmentCreateListBinding newList;
    IService mListener;
    FirebaseAuth mAuth;
    SharedPreferences sharedpreferences;

    public CreateListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        newList = FragmentCreateListBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_new_list));
        return newList.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        newList.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewList();
            }
        });

        newList.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPreviousFragment();
            }
        });

    }

    private void createNewList() {
        String listTitle = newList.editTextTitle.getText().toString();

        if (listTitle.isEmpty()) {
            displayAlert(getString(R.string.label_error_forum_title));
        } else {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy h:mm a");
            String createdDateTime = dateFormatter.format(Calendar.getInstance().getTime());

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            invitedUsers = new ArrayList<>();
            DocumentReference newDoc = db.collection("ShoppingList").document();

            Log.d("TAG", "UserList createNewList: " + invitedUsers);
            String DID = newDoc.getId();

            Log.d("TAG", "createNewList: UID "+mAuth.getCurrentUser().getUid());
            Log.d("TAG", "createNewList: display name"+mAuth.getCurrentUser().getDisplayName());
            newDoc.set(new ShoppingList(DID, mAuth.getCurrentUser().getUid(), listTitle, mAuth.getCurrentUser().getDisplayName(), createdDateTime, invitedUsers))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            mListener.goToShoppingListFragment();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("TAG", "onFailure: CreateList");
                }
            });

        }

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IService) {
                mListener = (IService) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException exception) {

        }
    }

    public void displayAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.label_alert))
                .setMessage(message)
                .setPositiveButton(getString(R.string.label_ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        builder.show();
    }

}