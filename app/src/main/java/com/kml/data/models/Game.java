package com.kml.data.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gameTable")
public class Game implements Parcelable
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private String requirements;
    private String numberOfKids;
    private String kidsAge;
    private String place;
    private String typeOfGame;
    private String category;
    public Game(String name, String description, String requirements, String numberOfKids, String kidsAge, String place, String typeOfGame, String category)
    {
        this.name = name;
        this.description = description;
        this.requirements = requirements;
        this.numberOfKids = numberOfKids;
        this.kidsAge = kidsAge;
        this.place = place;
        this.typeOfGame = typeOfGame;
        this.category = category;
    }

    protected Game(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        requirements = in.readString();
        numberOfKids = in.readString();
        kidsAge = in.readString();
        place = in.readString();
        typeOfGame = in.readString();
        category = in.readString();
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel in) {
            return new Game(in);
        }

        @Override
        public Game[] newArray(int size) {
            return new Game[size];
        }
    };

    public void setId(int id)
    {
        this.id = id;
    }
    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }
    public String getDescription()
    {
        return description;
    }
    public String getNumberOfKids()
    {
        return numberOfKids;
    }

    public String getRequirements()
    {
        return requirements;
    }
    public String getKidsAge()
    {
        return kidsAge;
    }
    public String getTypeOfGame()
    {
        return typeOfGame;
    }
    public String getPlace()
    {
        return place;
    }
    public String getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(requirements);
        dest.writeString(numberOfKids);
        dest.writeString(kidsAge);
        dest.writeString(place);
        dest.writeString(typeOfGame);
        dest.writeString(category);
    }
}