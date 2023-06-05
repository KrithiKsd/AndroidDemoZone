package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : LoginFragment.java
c. Full name of the student: Krithika Kasaragod
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

import com.example.inclass04.databinding.FragmentLoginBinding;


public class LoginFragment extends Fragment {

    FragmentLoginBinding loginBinding;
    FragmentInterface mListener;

    public LoginFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_login_fragment_title));
        return loginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        loginBinding.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailName = loginBinding.editTextEmail.getText().toString();
                String password = loginBinding.editTextPswd.getText().toString();

                DataServices.AccountRequestTask task = DataServices.login(emailName, password);

                if (task.isSuccessful()) { //successful
                    DataServices.Account account = task.getAccount();
                    mListener.sendAccount(account);

                } else { //not successful
                    String error = task.getErrorMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        loginBinding.buttonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoRegisterFragment();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof FragmentInterface) {
                mListener = (FragmentInterface) context;
            } else {
                throw new RuntimeException();
            }

        } catch (RuntimeException e) {
        }
    }
}