package com.example.homework07;

/*
Assignment #: Homework07
File Name: ListUser.java
Full Name of Student2: Krithika Kasaragod
 */

public class ListUser {
    String docId, UserId, name, userMail;

    public ListUser() {

    }

    public ListUser(String docId, String userId, String name, String userMail) {
        this.docId = docId;
        UserId = userId;
        this.name = name;
        this.userMail = userMail;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

  /*  @Override
    public String toString() {
        return "User{" +
                "docId='" + docId + '\'' +
                ", UserId='" + UserId + '\'' +
                ", name='" + name + '\'' +
                ", userMail='" + userMail + '\'' +
                '}';
    }*/

    @Override
    public String toString() {
        return name;
    }
}
