package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: IServices.java
c. Full name of the student : Krithika Kasaragod
*/
public interface IServices {
    void gotoPostListFromLogin(DataService.LoginDetails loginDetails);
    void gotoCreateNewAccountFragment();
    void gotoPostListFromRegister(DataService.LoginDetails loginDetails);
    void cancel();
    void gotoCreatePostFragment();
    void refreshPostList();
    void logout();
}
