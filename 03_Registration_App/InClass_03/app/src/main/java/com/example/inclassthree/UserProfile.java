package com.example.inclassthree;
/*
a. Assignment InClass03.
b. File Name: UserProfile.java
c. Full name of the student: Krithika Kasaragod
*/

import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {

    String nameUser, emailUser, deptUser;
    int idUser;

    public UserProfile(String nameUser, String emailUser, String deptUser, int idUser) {
        this.nameUser = nameUser;
        this.emailUser = emailUser;
        this.deptUser = deptUser;
        this.idUser = idUser;
    }

    public UserProfile() {

    }

    protected UserProfile(Parcel in) {
        nameUser = in.readString();
        emailUser = in.readString();
        deptUser = in.readString();
        idUser = in.readInt();
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(nameUser);
        parcel.writeString(emailUser);
        parcel.writeString(deptUser);
        parcel.writeInt(idUser);
    }
}
