package com.kml.data.models;

import com.google.gson.annotations.SerializedName;

public class Work
{
    @SerializedName("nazwaZadania")
    private String workName;

    @SerializedName("opisZadania")
    private String workDescription;

    @SerializedName("data")
    private String workDate;

    @SerializedName("czasWykonania")
    private String executionTime;

    public Work(String workName, String workDescription, String workDate, String executionTime)
    {
        this.workName = workName;
        this.workDescription = workDescription;
        this.workDate = workDate;
        this.executionTime = executionTime;
    }

    public String getWorkName()
    {
        return workName;
    }

    public String getWorkDescription()
    {
        return workDescription;
    }

    public String getWorkDate()
    {
        return workDate;
    }

    public String getExecutionTime()
    {
        return executionTime;
    }
}
