package com.example.howmework_05;
/*
a. Assignment Homework 05.
b. File Name: DataService.java
c. Full name of the student : Krithika Kasaragod
*/
import java.io.Serializable;

public class DataService {

    public static class LoginDetails implements Serializable {
        private String status, token, userId, userFullName;

        @Override
        public String toString() {
            return "LoginDetails{" +
                    "status='" + status + '\'' +
                    ", token='" + token + '\'' +
                    ", userId='" + userId + '\'' +
                    ", userFullName='" + userFullName + '\'' +
                    '}';
        }

        public LoginDetails() {
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserFullName() {
            return userFullName;
        }

        public void setUserFullName(String userFullName) {
            this.userFullName = userFullName;
        }
    }

    public static class Post {
        String createdByName, postId, createdByUId, postText, createdAt;
        String page, pageSize, totalCount;

        public Post() {
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public String getCreatedByName() {
            return createdByName;
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
        }

        public String getPostId() {
            return postId;
        }

        public void setPostId(String postId) {
            this.postId = postId;
        }

        public String getCreatedByUId() {
            return createdByUId;
        }

        public void setCreatedByUId(String createdByUId) {
            this.createdByUId = createdByUId;
        }

        public String getPostText() {
            return postText;
        }

        public void setPostText(String postText) {
            this.postText = postText;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public String getTotalCount() {
            return totalCount;
        }

        public void setTotalCount(String totalCount) {
            this.totalCount = totalCount;
        }

        @Override
        public String toString() {
            return "Post{" +
                    "createdByName='" + createdByName + '\'' +
                    ", postId='" + postId + '\'' +
                    ", createdByUId='" + createdByUId + '\'' +
                    ", postText='" + postText + '\'' +
                    ", createdAt='" + createdAt + '\'' +
                    ", page='" + page + '\'' +
                    ", pageSize='" + pageSize + '\'' +
                    ", totalCount='" + totalCount + '\'' +
                    '}';
        }
    }
}
