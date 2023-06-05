package com.example.homework07;

/*
Assignment #: Homework07
File Name: ShoppingList.java
Full Name of Student2: Krithika Kasaragod
 */

import java.util.ArrayList;


public class ShoppingList {
    String DID, UID, name, owner, dateTime;
    ArrayList<String> invitedUsers;

    public ShoppingList() {

    }

    public ShoppingList(String DID, String UID, String name, String owner, String dateTime, ArrayList<String> invitedUsers) {
        this.DID = DID;
        this.UID = UID;
        this.name = name;
        this.owner = owner;
        this.dateTime = dateTime;
        this.invitedUsers = invitedUsers;
    }


    public ArrayList<String> getInvitedUsers() {
        return invitedUsers;
    }

    public void setInvitedUsers(ArrayList<String> invitedUsers) {
        this.invitedUsers = invitedUsers;
    }


    public String getDID() {
        return DID;
    }

    public void setDID(String DID) {
        this.DID = DID;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "ShoppingList{" +
                "DID='" + DID + '\'' +
                ", UID='" + UID + '\'' +
                ", name='" + name + '\'' +
                ", owner='" + owner + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", invitedUsers=" + invitedUsers +
                '}';
    }
}
