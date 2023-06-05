package com.example.inclass04;

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
import com.example.inclass04.databinding.FragmentRegisterBinding;
/*
a. Assignment #. InClass04
b. File Name : RegisterFragment.java
c. Full name of the student: Krithika Kasaragod
*/

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding registerBinding;
    FragmentInterface mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_register_fragment_title));
        return registerBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerBinding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = registerBinding.editTextName.getText().toString();
                String emailName = registerBinding.editTextEmailAddress.getText().toString();
                String password = registerBinding.editTextPassword.getText().toString();

                DataServices.AccountRequestTask task = DataServices.register(name, emailName, password);
                if (task.isSuccessful()) { //successful
                    DataServices.Account account = task.getAccount();
                    mListener.sendAccountFromRegister(account);

                } else { //not successful
                    String error = task.getErrorMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerBinding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoLoginFragment();
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