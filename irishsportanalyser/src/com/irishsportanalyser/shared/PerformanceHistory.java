package com.irishsportanalyser.shared;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:44
 * To change this template use File | Settings | File Templates.
 */
import com.google.gwt.user.client.rpc.IsSerializable;

public class PerformanceHistory
        implements IsSerializable
{
    private String performance;
    private String competition;
    private String venue;
    private String date;

    public PerformanceHistory()
    {
    }

    public PerformanceHistory(String performance, String competition, String venue)
    {
        setPerformance("Empty");
        setCompetition("Empty");
        setVenue("Empty");
        setDate("Empty");
    }

    public String getPerformance()
    {
        return this.performance;
    }

    public void setPerformance(String performance)
    {
        this.performance = performance;
    }

    public String getCompetition()
    {
        return this.competition;
    }

    public void setCompetition(String competition)
    {
        this.competition = competition;
    }

    public String getVenue()
    {
        return this.venue;
    }

    public void setVenue(String venue)
    {
        this.venue = venue;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}