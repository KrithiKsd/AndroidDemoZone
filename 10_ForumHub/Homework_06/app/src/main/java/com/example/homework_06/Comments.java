package com.example.homework_06;
/*
Assignment #: Homework 06
File Name: Comments.java
Full Name of Student 1: Krithika Kasaragod
*/
public class Comments {

    String commentDetails, dateTime, commentDocId, UID, creatorName, forumID;

    public Comments() {
    }

    public Comments(String commentDetails, String dateTime, String commentDocId, String UID, String creatorName, String forumID) {
        this.commentDetails = commentDetails;
        this.dateTime = dateTime;
        this.commentDocId = commentDocId;
        this.UID = UID;
        this.creatorName = creatorName;
        this.forumID = forumID;
    }

    public String getForumID() {
        return forumID;
    }

    public void setForumID(String forumID) {
        this.forumID = forumID;
    }

    public String getCommentDetails() {
        return commentDetails;
    }

    public void setCommentDetails(String commentDetails) {
        this.commentDetails = commentDetails;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getCommentDocId() {
        return commentDocId;
    }

    public void setCommentDocId(String commentDocId) {
        this.commentDocId = commentDocId;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    @Override
    public String toString() {
        return "Comments{" +
                "commentDetails='" + commentDetails + '\'' +
                ", dateTime='" + dateTime + '\'' +
                ", commentDocId='" + commentDocId + '\'' +
                ", UID='" + UID + '\'' +
                ", creatorName='" + creatorName + '\'' +
                '}';
    }
}
