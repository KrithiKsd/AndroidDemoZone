package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : FragmentInterface.java
c. Full name of the student: Krithika Kasaragod
*/
public interface FragmentInterface {
    void sendAccount(DataServices.Account account);

    void sendAccountFromRegister(DataServices.Account account);

    void gotoRegisterFragment();

    void gotoLoginFragment();

    void gotoUpdateFragment(DataServices.Account account);

    void gotoAccountFragment();

    void gotoAccountFragmentWithAccount(DataServices.Account account);

    void logoutAccount();
}
