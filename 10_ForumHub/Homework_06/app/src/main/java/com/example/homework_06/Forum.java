package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: Forum.java
Full Name of Student 1: Krithika Kasaragod
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Forum implements Serializable {
    String DocId,UID,forumTitle, forumDescription, forumCreator, forumDateTime;
    ArrayList<String> listUID;
    String likesCount;

    HashMap<String,Boolean> mapLikes= new HashMap<String, Boolean>();

    public Forum() {
    }

    public Forum(String docId, String UID, String forumTitle, String forumDescription, String forumCreator, String forumDateTime, ArrayList<String> listUID) {
        DocId = docId;
        this.UID = UID;
        this.forumTitle = forumTitle;
        this.forumDescription = forumDescription;
        this.forumCreator = forumCreator;
        this.forumDateTime = forumDateTime;
        this.listUID = listUID;
    }

    public String getForumTitle() {
        return forumTitle;
    }

    public void setForumTitle(String forumTitle) {
        this.forumTitle = forumTitle;
    }

    public String getForumDescription() {
        return forumDescription;
    }

    public void setForumDescription(String forumDescription) {
        this.forumDescription = forumDescription;
    }

    public String getForumCreator() {
        return forumCreator;
    }

    public void setForumCreator(String forumCreator) {
        this.forumCreator = forumCreator;
    }

    public String getForumDateTime() {
        return forumDateTime;
    }

    public void setForumDateTime(String forumDateTime) {
        this.forumDateTime = forumDateTime;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
    public String getDocumentId() {
        return DocId;
    }

    public void setDocumentId(String documentId) {
        this.DocId = documentId;
    }

    public ArrayList<String> getListUID() {
        return listUID;
    }

    public void setListUID(ArrayList<String> listUID) {
        this.listUID = listUID;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "UID='" + UID + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                ", forumDescription='" + forumDescription + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", forumDateTime='" + forumDateTime + '\'' +
                ", documentId='" + DocId + '\'' +
                '}';
    }

    //    ", listUID=" + listUID +
}
