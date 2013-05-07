package com.irishsportanalyser.entities;

/**
 * Created with IntelliJ IDEA.
 * User: ola
 * Date: 05/05/13
 * Time: 17:36
 * To change this template use File | Settings | File Templates.
 */

import com.google.gwt.user.client.rpc.IsSerializable;
import com.irishsportanalyser.enums.Region;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Competition implements IsSerializable
{

    @PrimaryKey
    @Persistent(valueStrategy=IdGeneratorStrategy.IDENTITY)
    private Long id;

    @Persistent
    private String competitionType;

    @Persistent
    private Region region;

    @Persistent
    private String name;

    @Persistent
    private String venue;

    @Persistent
    private String date;

    @Persistent
    private String season;

    public Competition()
    {
    }

    public Competition(String competitionType, Region region, String name, String venue, String date, String season)
    {
        setCompetitionType(competitionType);
        setName(name);
        setRegion(region);
        setVenue(venue);
        setDate(date);
        setSeason(season);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Region getRegion() {
        return this.region;
    }

    void setRegion(Region region) {
        this.region = region;
    }

    public String getName() {
        return this.name;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return this.date;
    }

    void setDate(String date) {
        this.date = date;
    }

    public String getVenue() {
        return this.venue;
    }

    void setVenue(String venue) {
        this.venue = venue;
    }

    public String getSeason() {
        return this.season;
    }

    void setSeason(String season) {
        this.season = season;
    }

    public String getCompetitionType()
    {
        return this.competitionType;
    }

    void setCompetitionType(String competitionType)
    {
        this.competitionType = competitionType;
    }
}
