package com.kml.controlPanel;

public class Volunteer
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
}
