package com.example.inclass09;

public class Forum {
    String UID,forumTitle, forumDescription, forumCreator, forumDateTime;
    String documentId;

    Forum(){

    }
    public Forum(String UID, String forumTitle, String forumDescription, String forumCreator, String forumDateTime) {
        this.UID = UID;
        this.forumTitle = forumTitle;
        this.forumDescription = forumDescription;
        this.forumCreator = forumCreator;
        this.forumDateTime = forumDateTime;

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
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    @Override
    public String toString() {
        return "Forums{" +
                "UID='" + UID + '\'' +
                ", forumTitle='" + forumTitle + '\'' +
                ", forumDescription='" + forumDescription + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", forumDateTime='" + forumDateTime + '\'' +
                '}';
    }
}
