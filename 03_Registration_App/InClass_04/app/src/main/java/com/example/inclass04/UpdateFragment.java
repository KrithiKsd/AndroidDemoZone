package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : UpdateFragment.java
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
import com.example.inclass04.databinding.FragmentUpdateBinding;


public class UpdateFragment extends Fragment {


    private static final String ARG_ACCOUNT = "Account";
    private DataServices.Account mAccount;
    FragmentUpdateBinding updateBinding;
    FragmentInterface mListener;

    // TODO: Rename and change types and number of parameters
    public static UpdateFragment newInstance(DataServices.Account account) {
        UpdateFragment fragment = new UpdateFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ACCOUNT, account);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAccount = (DataServices.Account) getArguments().getSerializable(ARG_ACCOUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        updateBinding = FragmentUpdateBinding.inflate(inflater, container, false);
        getActivity().setTitle(getString(R.string.label_update_fragment_title));
        return updateBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateBinding.textViewUpdateEmail.setText(mAccount.getEmail());

        updateBinding.buttonUpdateSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailName = updateBinding.editTextUpdateName.getText().toString();
                String password = updateBinding.editTextUpdatePassword.getText().toString();

                DataServices.AccountRequestTask task = DataServices.update(mAccount, emailName, password);

                if (task.isSuccessful()) { //successful
                    DataServices.Account account = task.getAccount();

                    mListener.gotoAccountFragmentWithAccount(account);

                } else { //not successful
                    String error = task.getErrorMessage();
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            }

        });

        updateBinding.buttonUpdateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoAccountFragment();
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