package com.kml.worksHistory;

public class Work
{
    private String workName;
    private String workDescription;
    private String workDate;
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
