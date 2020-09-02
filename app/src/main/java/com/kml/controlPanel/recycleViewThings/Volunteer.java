package com.kml.controlPanel.recycleViewThings;

import android.os.Parcel;
import android.os.Parcelable;

public class Volunteer implements Parcelable
{
    private int id;
    private String firstName;
    private String lastName;
    private boolean isChecked;

    public Volunteer(int id, String firstName, String lastName, boolean isChecked)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isChecked = isChecked;
    }

    protected Volunteer(Parcel in)
    {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        isChecked = in.readByte() != 0;
    }

    public static final Creator<Volunteer> CREATOR = new Creator<Volunteer>()
    {
        @Override
        public Volunteer createFromParcel(Parcel in)
        {
            return new Volunteer(in);
        }

        @Override
        public Volunteer[] newArray(int size)
        {
            return new Volunteer[size];
        }
    };

    public String getFirstName()
    {
        return firstName;
    }

    public int getId()
    {
        return id;
    }

    public String getLastName()
    {
        return lastName;
    }

    public boolean isChecked()
    {
        return isChecked;
    }

    public void setChecked(boolean checked)
    {
        isChecked = checked;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(id);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeByte((byte) (isChecked ? 1 : 0));
    }
}
