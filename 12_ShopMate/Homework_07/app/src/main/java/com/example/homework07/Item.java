package com.example.homework07;

import java.util.ArrayList;

/*
Assignment #: Homework07
File Name: Item.java
Full Name of Student2: Krithika Kasaragod
 */

public class Item {
    String uid;
    String documentId;
    String userName;
    String itemName;
    String itemPrice;

    boolean checked;

    public Item() {

    }

    public Item(String uid, String documentId, String userName, String itemName, String itemPrice, boolean checked) {
        this.uid = uid;
        this.documentId = documentId;
        this.userName = userName;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemPrice='" + itemPrice + '\'' +
                '}';
    }
}
