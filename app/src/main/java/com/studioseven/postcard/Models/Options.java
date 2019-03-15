package com.studioseven.postcard.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Options implements Parcelable{
    private String option;
    private int thumbnail;
    private int id;

    public Options(String option, int thumbnail, int id) {
        this.option = option;
        this.thumbnail = thumbnail;
        this.id = id;
    }

    protected Options(Parcel in) {
        option = in.readString();
        thumbnail = in.readInt();
    }

    public static final Creator<Options> CREATOR = new Creator<Options>() {
        @Override
        public Options createFromParcel(Parcel in) {
            return new Options(in);
        }

        @Override
        public Options[] newArray(int size) {
            return new Options[size];
        }
    };

    public String getOption() {
        return option;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(option);
        parcel.writeInt(thumbnail);
    }
}
