package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: RegisterFragment.java
Full Name of Student 1: Krithika Kasaragod
*/
import static com.example.homework_06.MainActivity.UID;
import static com.example.homework_06.MainActivity.creatorName;
import static com.example.homework_06.MainActivity.myPreference;

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

import com.example.homework_06.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding registerBinding;
    String missingField;
    private FirebaseAuth mAuth;
    IService mListener;
    SharedPreferences sharedpreferences;
    String fullName;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.title_register));
        return registerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        registerBinding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullName = registerBinding.editTextName.getText().toString();
                String emailName = registerBinding.editTextEmailAddress.getText().toString();
                String password = registerBinding.editTextPassword.getText().toString();
                boolean success = validation(fullName, emailName, password);
                if (success) {
                    mAuth = FirebaseAuth.getInstance();

                    mAuth.createUserWithEmailAndPassword(emailName, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        sharedpreferences = getActivity().getSharedPreferences(myPreference,
                                                Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString(creatorName, fullName);
                                        editor.putString(UID, mAuth.getCurrentUser().getUid());
                                        editor.commit();


                                        FirebaseUser user = mAuth.getCurrentUser();
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(fullName)
                                                .build();

                                        user.updateProfile(profileUpdates)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Log.d("TAG", "User profile updated.");
                                                        }
                                                    }
                                                });

                                        mListener.gotoForumsFragment();

                                    } else {
                                        displayAlert(task.getException().getMessage());
                                    }
                                }
                            });


                } else {
                    displayAlert(missingField);
                }
            }
        });


        registerBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoPreviousFragment();
            }
        });
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

    private boolean validation(String fullName, String emailName, String password) {
        if (fullName.isEmpty() && emailName.isEmpty() && password.isEmpty()) {
            missingField = getString(R.string.label_full_name_email_password_field);
            return false;
        } else if (fullName == null || fullName.isEmpty()) {
            missingField = getString(R.string.label_full_name_field);
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