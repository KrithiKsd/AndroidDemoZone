package com.example.homework_04;
/*
a. Assignment Homework04.
b. File Name: IUserInterface.java
c. Full name of the student : Krithika Kasaragod
*/

public interface IUserInterface {

    void gotoFilterByStateFragment();

    void gotoUsersFragmentWithSelectedCriteria(DataServices.User user);

    void gotoUsersFragmentWithSelectedCriteriaNew(DataServices.User user);

    void gotoSortFragment();

}
