package com.example.inclass04;
/*
a. Assignment #. InClass04
b. File Name : DataServices.java
c. Full name of the student: Krithika Kasaragod
*/
import android.content.Context;

import java.io.Serializable;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataServices {
    private static HashMap<String, Account> accounts = new HashMap<String, Account>() {{
        put("a@a.com", new Account("Alice Smith", "a@a.com", "test123"));
        put("b@b.com", new Account("Bob Smith", "b@b.com", "test123"));
        put("c@c.com", new Account("Charles Smith", "c@c.com", "test123"));
    }};
    private static Context mContext;

    public DataServices(Context context) {
        this.mContext = context;
    }

    //have to change hard coded strings
    public static AccountRequestTask login(String email, String password) {

        if (email == null || email.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorEmail));
        }

        //Added by us
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            return new AccountRequestTask(mContext.getString(R.string.label_error_email_format));
        }

        if (password == null || password.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorPassword));
        }


        if (!accounts.containsKey(email.trim().toLowerCase())) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorNoUser));
        }

        Account account = accounts.get(email.trim().toLowerCase());


        if (account == null || !account.getPassword().equals(password)) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorNoMatch));
        }

        return new AccountRequestTask(account);
    }

    public static AccountRequestTask register(String name, String email, String password) {

        if (name == null || name.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorName));
        }

        if (email == null || email.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorEmail));
        }
        //Added by us
        Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
        Matcher emailMatcher = emailPattern.matcher(email);
        if (!emailMatcher.matches()) {
            return new AccountRequestTask(mContext.getString(R.string.label_error_email_format));
        }

        if (password == null || password.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorPassword));
        }

        if (accounts.containsKey(email.trim().toLowerCase())) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorEmailExists));
        }

        Account account = new Account(name, email.trim().toLowerCase(), password);
        accounts.put(email.trim().toLowerCase(), account);
        return new AccountRequestTask(account);
    }

    public static AccountRequestTask update(Account oldAccount, String name, String password) {
        if (oldAccount == null) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorValidAccount));
        }

        if (name == null || name.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorName));
        }

        if (password == null || password.isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorPassword));
        }

        if (oldAccount.getEmail() == null || oldAccount.getEmail().isEmpty()) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorEmail));
        }

        if (!accounts.containsKey(oldAccount.getEmail().trim().toLowerCase())) {
            return new AccountRequestTask(mContext.getString(R.string.label_errorInValidAccount));
        }

        String email = oldAccount.getEmail().trim().toLowerCase();

        Account account = new Account(name, email, password);
        accounts.put(email, account);

        return new AccountRequestTask(account);
    }

    public static class Account implements Serializable {
        private String name, email, password;

        public Account(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
        }

        public String getName() {
            return name;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

    }

    public static class AccountRequestTask {
        private boolean isSuccessful;
        private String errorMessage;
        private Account account;

        public AccountRequestTask(String error) {
            this.isSuccessful = false;
            this.errorMessage = error;
            this.account = null;
        }

        public AccountRequestTask(Account account) {
            this.isSuccessful = true;
            this.errorMessage = null;
            this.account = account;
        }

        public boolean isSuccessful() {
            return isSuccessful;
        }


        public String getErrorMessage() {
            return errorMessage;
        }


        public Account getAccount() {
            return account;
        }

    }
}

