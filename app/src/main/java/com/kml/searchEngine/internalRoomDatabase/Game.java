package com.kml.searchEngine.internalRoomDatabase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "gameTable")
public class Game
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
}