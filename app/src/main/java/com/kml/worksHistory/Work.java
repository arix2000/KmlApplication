package com.kml.worksHistory;

public class Work
{
    private String workName;
    private String workDescription;
    private String workDate;

    public Work(String workName, String workDescription, String workDate)
    {
        this.workName = workName;
        this.workDescription = workDescription;
        this.workDate = workDate;
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
}
